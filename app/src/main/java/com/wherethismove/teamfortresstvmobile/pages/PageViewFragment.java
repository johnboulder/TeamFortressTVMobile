package com.wherethismove.teamfortresstvmobile.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wherethismove.teamfortresstvmobile.utils.GetPageDataTask;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;

import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * A Fragment intended to be used as a base class for the various fragments which will be
 * implemented in this project
 */
public abstract class PageViewFragment extends Fragment {
    public static final String ARG_URL = "url";
    public static final String ARG_URL_OBJECT = "url_object";
    public static final String ARG_LAYOUT = "layout";
    public GetDocumentCallback callback;
    protected Document document;
    protected URL _url;
    protected String url;
    protected String urlUnchanging;
    protected int layout;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected LoadListItemOnScrollListener onScrollListener;

    // TODO make a parent object for ThreadComment, ThreadListItem, and ForumListItem to use here in listItems
    // This way initialization of listItems can be done here
    //protected ArrayList<Object> listItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _url = (URL) getArguments().get(ARG_URL_OBJECT);

            urlUnchanging = getArguments().getString(ARG_URL);
            url = urlUnchanging;
            layout = getArguments().getInt( ARG_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate( layout, container, false);
        final View v2 = v;

        // Needs to return a document
        // Needs to received a url
        // Needs to recieve and pass a view to initializeList
        GetPageDataTask task = new GetPageDataTask(
                new GetDocumentCallback() {
                    @Override
                    public void refreshList(Document document) {
                        /* Unimplemented intentionally. Maybe don't extend RefreshFragmentListCallback
                         * in GetDocumentCallback
                         */
                    }

                    @Override
                    public void callback(View view, Document result) {
                        document = result;
                        initializeList(view);
                    }
                },
                v2);
        task.execute(url);

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

    public interface GetDocumentCallback extends RefreshFragmentListCallback {
        void callback(View view, Document result);
    }
}
