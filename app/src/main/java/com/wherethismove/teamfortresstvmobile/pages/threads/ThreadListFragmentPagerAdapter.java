package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.wherethismove.teamfortresstvmobile.pages.forums.ForumListTabFragment;

/**
 * Created by stockweezie on 1/8/2016.
 */
public class ThreadListFragmentPagerAdapter
        extends FragmentPagerAdapter
{
    private final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"HOT", "ACTIVE", "NEW", "TOP"};
    private Context context;

    public ThreadListFragmentPagerAdapter( FragmentManager fm,
                                           Context context )
    {
        super( fm );
        this.context = context;
    }

    @Override
    public int getCount( )
    {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem( int position )
    {
        return ForumListTabFragment.newInstance( position );
    }

    @Override
    public CharSequence getPageTitle( int position )
    {
        // Generate title based on item position
        return tabTitles[position];
    }
}