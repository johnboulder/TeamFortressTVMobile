package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.wherethismove.teamfortresstvmobile.MainActivity;
import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.utils.GetNewPageDataTask;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 *
 * TODO Add functionality for sorting threads
 * TODO Add functionality for getting more pages
 * TODO make abstract class?
 */
public class ThreadListViewFragment extends PageViewFragment
{
    protected ArrayList<ForumThread> listItems;
    private ThreadAdapter mAdapter;
    private OnThreadSelectedListener mListener;
    private String mTitle;

    public ThreadListViewFragment() {
        // Required empty public constructor
    }

    public static ThreadListViewFragment newInstance(String url, int layout) {
        ThreadListViewFragment fragment = new ThreadListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putInt(ARG_LAYOUT, layout);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: refactor, View v param is unnecessary
    @Override
    protected void initializeList(View v){
        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.radio_group_sort);
        for (int j = 0; j < radioGroup.getChildCount(); j++)
        {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            if(view.isChecked())
            {
                switch(j)
                {
                    case 0:
                        url = url +"/?sort=hot";
                        break;
                    case 1:
                        url = url +"/?sort=active";
                        break;
                    case 2:
                        url = url +"/?sort=new";
                        break;
                    case 3:
                        url = url +"/?sort=top";
                        break;
                }
            }
        }
        final ListView lv = (ListView) v.findViewById(R.id.thread_list);

        // Fill the thread list with threads
        populateList();

        // Pass a
        mAdapter = new ThreadAdapter(v.getContext(), mListener, listItems);
        lv.setAdapter(mAdapter);

        // Setup the scroll listener which updates the contents of the listView whenever the last
        // item in the list becomes visible
        // NOTE: This changes the value of document.
        // TODO get rid of calculations that occur in LoadListItemOnScrollListener
        // TODO set this up so that a callback is passed to LoadListItemOnScrollListener
        // and any calculations are done within the callback. Make it so the callback can be used
        // here or setOnRefreshListener. Consider making a URL object
        onScrollListener = new LoadListItemOnScrollListener(
                new RefreshFragmentListCallback()
                {
                    @Override
                    public void refreshList(Document doc)
                    {
                        document = doc;
                        populateList();
                        mAdapter.notifyDataSetChanged();
                    }
                },
                url
        );
        lv.setOnScrollListener(onScrollListener);

        // TODO find a way to merge this functionality with what LoadListItemOnScrollListener does
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh()
            {
                // Just get the page again, the site already refreshes the data
                new GetNewPageDataTask(new RefreshFragmentListCallback()
                {
                    // TODO refactor so only the changed portions of each listItem are changed
                    @Override
                    public void refreshList(Document doc)
                    {
                        listItems.clear();
                        mAdapter.notifyDataSetChanged();
                        document = doc;
                        populateList();
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).execute(url);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {

                for (int j = 0; j < radioGroup.getChildCount(); j++)
                {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                    view.setChecked(view.getId() == i);
                }
                switch(i)
                {
                    case R.id.button_sort_active:
                        url = urlUnchanging +"/?sort=active";
                        break;
                    case R.id.button_sort_hot:
                        url = urlUnchanging +"/?sort=hot";
                        break;
                    case R.id.button_sort_new:
                        url = urlUnchanging +"/?sort=new";
                        break;
                    case R.id.button_sort_top:
                        url = urlUnchanging +"/?sort=top";
                        break;
                }
                new GetNewPageDataTask(new RefreshFragmentListCallback()
                {
                    // TODO refactor so only the changed portions of each listItem are changed
                    @Override
                    public void refreshList(Document doc)
                    {
                        listItems.clear();
                        listItems.add(new ForumThread("Loading", "Loading", "Loading", "Loading", "Loading", "Loading", "Loading"));
                        mAdapter.notifyDataSetChanged();
                        listItems.clear();
                        onScrollListener.resetItemCount();
                        onScrollListener = new LoadListItemOnScrollListener(
                                new RefreshFragmentListCallback()
                                {
                                    @Override
                                    public void refreshList(Document doc)
                                    {
                                        document = doc;
                                        populateList();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                },
                                url
                        );
                        lv.setOnScrollListener(onScrollListener);
                        onScrollListener.resetItemCount();
                        document = doc;
                        populateList();
                        mAdapter.notifyDataSetChanged();
                    }
                }).execute(url);
            }
        });

        mTitle = document.select("#content > div:nth-child(4) > div > div:nth-child(2)").text();
        if(mTitle == "")
            mTitle = "Threads";
        getActivity().setTitle(mTitle);
    }

    @Override
    protected void populateList()
    {
        if(listItems == null)
            listItems = new ArrayList<>();

        Elements threads = document.select("div.thread");

        for(int i = 0; i<threads.size(); i++)
        {
            Element curThread = threads.get(i);
            Element frags = curThread.select(".frag-count").first();

            Element mainData = curThread.select("div.block.main").first();

            // Get the thread title
            String title = mainData.select("a.title").first().text();
            // Get the link to the thread
            String threadURL = mainData.select("a.title").first().attr("href");
            String threadURLWithoutPage = MainActivity.URL_HOSTNAME +threadURL;
            // Get the Forum it's in
            // Only appears when in the "Threads" section of the website
            Element description = mainData.select("div.description").first();
            // Get the posts
            String posts = curThread.select("span.post-count").first().text();
            // Get the pages
            Integer pages = curThread.select("a.thread-pages").size();
            // Get the OP TODO refer to ForumsViewFragment on getting usernames
            String op = "username";
            // Get the postTime (use div.description for username+postTime+forum)
            String postTime = mainData.select("span.date-eta").text();

            ForumThread ft = new ForumThread(posts, pages.toString(), op, title, description.text(), frags.text(), threadURLWithoutPage);

            listItems.add(ft);
        }
    }

    public void onThreadSelected(String url) {
        if (mListener != null) {
            mListener.openThread(url);
        }
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        getActivity().setTitle(mTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnThreadSelectedListener) {
            mListener = (OnThreadSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnThreadSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnThreadSelectedListener {
        // TODO: Update argument type and name
        void openThread(String url);
    }
}
