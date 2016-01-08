package com.wherethismove.teamfortresstvmobile.pages.comments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * TODO streamline class so it can be called from ArticleViewFragment for filling comments
 */
public class ThreadViewFragment extends PageViewFragment {

    private CommentAdapter mAdapter;
    private ArrayList<ThreadComment> listItems;
    protected commentFiller mListener;

    public ThreadViewFragment() {
        // Required empty public constructor
    }

    public static ThreadViewFragment newInstance(String url, int layout) {
        ThreadViewFragment fragment = new ThreadViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT, layout);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializeList(View v)
    {
        // Set the thread title
        Element headerTitle = document.select("div.thread-header-title").first();
        TextView threadTitle = (TextView) v.findViewById(R.id.thread_header);
        threadTitle.setText(headerTitle.text());

        populateList();

        final ListView lv = (ListView) v.findViewById(R.id.comments_list);
        mAdapter = new CommentAdapter(v.getContext(), listItems);
        lv.setAdapter(mAdapter);
        lv.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

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

        Elements comments = document.select("div.post");
        Element forum = document.select("div.thread-header-desc").first();

        for(int i = 0; i<comments.size(); i++)
        {
            Element curComment = comments.get(i);
            Element frags;
            if(curComment.className().equals("post self"))
                frags = document.select("span#thread-frag-count").first();
            else
                frags = curComment.select("span.post-frag-count").first();

            // Get the post number. Alternate selector "a.post-anchor"
            Element postNumber = curComment.select("div.post-num").first();
            // Get the header
            Element header = curComment.select("a.post-author").first();
            // Get the body
            Element body = curComment.select("div.post-body").first();
            // Get the footer
            Element footer = curComment.select("div.post-footer").first();

            //public ThreadComment(String header, String frags, String forum, String body, String footer, String postNumber, String url)
            ThreadComment tc = new ThreadComment(postNumber.text()+" "+ header.text(), frags.text(), forum.text(),body.html(), footer.text(), postNumber.text(), mUrl+postNumber.text());
            listItems.add(tc);
        }
    }

    public void populateComments(String s) {
        if (mListener != null) {
            mListener.fillComments(s, document);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof commentFiller) {
            mListener = (commentFiller) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface commentFiller {
        // TODO: Update argument type and name
        void fillComments(String url, Document d);
    }
}
