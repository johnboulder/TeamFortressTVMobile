package com.wherethismove.teamfortresstvmobile.pages;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * A Fragment intended to be used as a base class for the various fragments which will be
 * implemented in this project
 */
public abstract class PageViewFragment extends Fragment
{
    public static final String ARG_URL = "url";
    public static final String ARG_LAYOUT = "layout";
    protected Document document;
    protected String mUrl;
    protected String mBaseUrl;
    protected int mLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    // TODO make a parent object for ThreadComment, ForumThread, and Forum to use here in listItems
    // This way initialization of listItems can be done here
    //protected ArrayList<Object> listItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mBaseUrl = getArguments().getString(ARG_URL);
            mUrl = mBaseUrl+"/?page=1";
            mLayout = getArguments().getInt(ARG_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(mLayout, container, false);
        final View v2 = v;
        // Inflate the layout for this fragment
        class MyTask extends AsyncTask<Void, Void, Document>
        {
            @Override
            protected Document doInBackground(Void... params)
            {
                try
                {
                    document = Jsoup.connect(mUrl).get();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return null;
                }

                return document;
            }

            @Override
            protected void onPostExecute(Document result)
            {
                initializeList(v2);
            }
        }
        new MyTask().execute();
        return v;
    }

    // Called from onCreateView after the document is downloaded
    abstract protected void initializeList(View v);

    // Called from initialize list for each initialized list
    // Called from listeners when list_items are added to the list
    abstract protected void populateList();

    public interface RefreshFragmentListCallback {
        void refreshList(Document document);
    }
}
