package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
        final ListView lv = (ListView) v.findViewById(R.id.thread_list);

        // Fill the thread list with threads
        populateList();

        mAdapter = new ThreadAdapter(v.getContext(), listItems);
        lv.setAdapter(mAdapter);

        // Setup the onClickListener for the list so that when an item is clicked
        // the visibility of various sub views are toggled
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Show extended information for the thread
            // Set a number of different items in the view from "gone" to "visible"
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // ListView Clicked item value
                final ForumThread  itemValue = (ForumThread) lv.getItemAtPosition(position);
                Button b = (Button) v.findViewById(R.id.b_view_thread);

                /* When a list item is clicked, initialize the click listener for the button*/
                b.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        onThreadSelected(itemValue.getThreadUrl());
                    }
                });

                //Toggle the button's visibility
                int visibility = b.getVisibility();

                if(visibility == Button.GONE)
                    b.setVisibility(Button.VISIBLE);
                else
                    b.setVisibility(Button.GONE);
            }

        });

        // Setup the scroll listener which updates the contents of the listView whenever the last
        // item in the list becomes visible
        // NOTE: This changes the value of document.
        lv.setOnScrollListener(new LoadListItemOnScrollListener(
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
                mUrl
        ));

        //TODO find a way to merge this functionality with what LoadListItemOnScrollListener does
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }).execute(mUrl);
            }
        });

        RadioGroup rg = (RadioGroup) getView().findViewById(R.id.radio_group_sort);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {

                String url = mBaseUrl;
                for (int j = 0; j < radioGroup.getChildCount(); j++)
                {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                    view.setChecked(view.getId() == i);
                }
                switch(i)
                {
                    case R.id.button_sort_active:
                        url = mBaseUrl+"/?sort=active";
                        break;
                    case R.id.button_sort_hot:
                        url = mBaseUrl+"/?sort=hot";
                        break;
                    case R.id.button_sort_new:
                        url = mBaseUrl+"/?sort=new";
                        break;
                    case R.id.button_sort_top:
                        url = mBaseUrl+"/?sort=top";
                        break;
                }

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }).execute(url);
            }
        });
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
            String threadURLWithoutPage = MainActivity.siteRoot+threadURL;
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
