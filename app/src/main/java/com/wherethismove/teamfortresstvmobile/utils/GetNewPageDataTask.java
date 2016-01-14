package com.wherethismove.teamfortresstvmobile.utils;

import android.os.AsyncTask;

import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by stockweezie on 1/5/2016.
 * This class is explicitly intended for finding whether
 *
 * TODO figure out how to implement the second paramType of AsyncTask (the progress type) so that the
 * list being updated shows a list_item at the bottom with the words "Loading" animated
 *
 * TODO add a safety net for when the result is null
 */
public class GetNewPageDataTask extends AsyncTask<String, Void, Document>{

    private PageViewFragment.RefreshFragmentListCallback mFragmentCallback;

    public GetNewPageDataTask(PageViewFragment.RefreshFragmentListCallback callback)
    {
        mFragmentCallback = callback;
    }

    @Override
    protected Document doInBackground(String... params) {
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
    protected void onPostExecute(Document result) {
        mFragmentCallback.refreshList(result);
    }
}
