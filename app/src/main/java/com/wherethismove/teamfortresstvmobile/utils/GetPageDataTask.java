package com.wherethismove.teamfortresstvmobile.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/* TODO What this class needs to do
 * Replace every localized instance where an asyncTask is made to load a document
 *
 * Receive a url, receive a view, receive a fragment?
 * Return a document, and call a view
 * TODO change the fragment to a child of the callback listener object interface in pageView fragment
 * TODO make the type PageViewFragment.RefreshFragmentListCallback, and we can use any child callback with the function "callback
 * TODO to implement this function. That way there will only ever be on implementation
 *
 * TODO decide if this should recieve a progressbar or not
 */

public class GetPageDataTask extends AsyncTask<String, Void, Document>
{

    private PageViewFragment.GetDocumentCallback mFragmentCallBack;
    private View mView;

    public GetPageDataTask(PageViewFragment.GetDocumentCallback fragment, View view)
    {
        mFragmentCallBack = fragment;
        mView = view;
    }

    @Override
    protected Document doInBackground(String... params)
    {
        Document document;
        try
        {
            document = Jsoup.connect(params[0]).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return document;
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {

    }

    @Override
    protected void onPostExecute(Document result)
    {
        if(mFragmentCallBack != null)
        {
            mFragmentCallBack.callback(mView, result);
        }
    }
}
