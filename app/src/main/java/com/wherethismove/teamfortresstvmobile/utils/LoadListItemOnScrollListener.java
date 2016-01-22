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
    }

    public void resetItemCount()
    {
        previousLastItem = 0;
    }

    public void setUrl(String url)
    {
        mUrl = url;
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
                        if(totalItemCount%30 == 0)
                        {
                            mPage = (totalItemCount/30)+1;
                        }
                        else
                        {
                            // Ensures totalItemCount is a rounded off number, like 30, 60, 90, etc.
                            mPage = ((totalItemCount+(30-totalItemCount%30))/30)+1;
                        }
                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"&page="+mPage.toString());
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
                        // Ensures we're not counting the article item in the following calculations
                        totalItemCount-=1;
                        if((totalItemCount)%30 == 0)
                        {
                            mPage = (totalItemCount/30)+1;
                        }
                        else
                        {
                            // Ensures totalItemCount is a rounded off number, like 30, 60, 90, etc.
                            mPage = ((totalItemCount+(30-totalItemCount%30))/30)+1;
                        }
                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                    }
                }
                break;

            case R.id.comments_list:
                lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    //TODO find better solution for limiting when the page attempts to load more comments
                    // totalItemCount>=30 is included here for threads with less than 30 comments trying to load the next page when
                    // the last comment is immediately visible
                    if(previousLastItem!=lastItem && totalItemCount>=30){
                        //to avoid multiple calls for last item
                        previousLastItem = lastItem;
                        // Ensures we're not counting the title item in the following calculations
                        totalItemCount-=1;
                        if((totalItemCount)%30 == 0)
                        {
                            mPage = (totalItemCount/30)+1;
                        }
                        else
                        {
                            // Ensures totalItemCount is a rounded off number, like 30, 60, 90, etc.
                            mPage = ((totalItemCount+(30-totalItemCount%30))/30)+1;
                        }
                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                    }
                }
                break;

        }
    }
}
