package com.wherethismove.teamfortresstvmobile.utils;

import android.widget.AbsListView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

/**
 * Created by stockweezie on 1/4/2016.
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
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    if(previousLastItem!=lastItem){
                        //to avoid multiple calls for last item
                        previousLastItem = lastItem;

                        new GetNewPageDataTask(mFragmentCallback).execute(mUrl+"/?page="+mPage.toString());
                        mPage++;
                    }
                }
        }
    }
}
