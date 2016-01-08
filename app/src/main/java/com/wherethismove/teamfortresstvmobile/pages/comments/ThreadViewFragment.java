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
public class ThreadViewFragment extends Fragment {
    protected static final String ARG_URL = "url";

    protected String mUrl;
    protected Document document;
    private ArrayList<ThreadComment> listItems;
    protected commentFiller mListener;

    public ThreadViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url Parameter 1.
     * @return A new instance of fragment ThreadViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThreadViewFragment newInstance(String url) {
        ThreadViewFragment fragment = new ThreadViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_thread_view, container, false);
        final View v2 = v;
        // Inflate the layout for this fragment
        // TODO Move MyTask to its own independent class,
        // TODO and make initializeList(View v) an abstract function in PageViewFragment
        // TODO Make this class extend PageViewFragment, and have MyTask call initializeList
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
                populateList(v2);
            }
        }
        new MyTask().execute();

        // Loading list items when list is scrolled to the bottom
        // This class has a callback function in it which is passed a document for the next page to load comments from
            // This callback gets all the threads, and adds them to the list
        // This occurs when the callback is called from GetNewPageDataTask
        // GetNewPageDataTask will be called by a LoadListItemOnScrollListener

        return v;
    }

    protected void populateList(View v)
    {
        // Set the thread title
        Element headerTitle = document.select("div.thread-header-title").first();
        TextView threadTitle = (TextView) v.findViewById(R.id.thread_header);
        threadTitle.setText(headerTitle.text());

        Elements comments = document.select("div.post");
        listItems = new ArrayList<>();

        final ListView lv = (ListView) v.findViewById(R.id.comments_list);

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

        CommentAdapter adapter = new CommentAdapter(v.getContext(), listItems);
        lv.setAdapter(adapter);
        lv.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
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
