package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wherethismove.teamfortresstvmobile.MainActivity;
import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
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
            try{String forum = mainData.select("div.description").first().text();}
            catch(NullPointerException e){}
            // Get the posts
            String posts = curThread.select("span.post-count").first().text();
            // Get the pages
            Integer pages = curThread.select("a.thread-pages").size();
            // Get the OP TODO refer to ForumsViewFragment on getting usernames
            String op = "username";
            // Get the postTime (use div.description for username+postTime+forum)
            String postTime = mainData.select("span.date-eta").text();

            ForumThread ft = new ForumThread(posts, pages.toString(), op, title, postTime, threadURLWithoutPage);

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
