package com.wherethismove.teamfortresstvmobile.utils;

import android.widget.AbsListView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

/**
 * Created by stockweezie on 1/4/2016.
 * TODO when a GetNewPageDataTask returns a 404 we need to restrict any more tasks from being created
 */
public class LoadListItemOnScrollListener implements AbsListView.OnScrollListener
{
    private int previousLastItem;
    private PageViewFragment.RefreshFragmentListCallback mFragmentCallback;
    private String mUrl;
    private Integer mPage;

    public LoadListItemOnScrollListener(PageViewFragment.RefreshFragmentListCallback callback, String url)
    {
        mFragmentCallback = callback;
        mUrl = url;
        mPage = 2;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch(view.getId())
        {
            case R.id.thread_list:
                int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    if(previousLastItem!=lastItem){
                        //to avoid multiple calls for last item
                        previousLastItem = lastItem;

                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                        mPage++;
                    }
                }
                break;

            case R.id.list_view_article_body_and_comments:
                lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    if(previousLastItem!=lastItem){
                        //to avoid multiple calls for last item
                        previousLastItem = lastItem;

                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                        mPage++;
                    }
                }
                break;

            case R.id.comments_list:
                lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    if(previousLastItem!=lastItem){
                        //to avoid multiple calls for last item
                        previousLastItem = lastItem;

                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                        mPage++;
                    }
                }
                break;

        }
    }
}
