package com.wherethismove.teamfortresstvmobile.pages.forums;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by stockweezie on 1/8/2016.
 */
public class ForumListPagerAdapter
        extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"TF2", "CSGO", "General", "Site"};
    private Context context;

    public ForumListPagerAdapter( FragmentManager fm,
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
        return ForumTabFragment.newInstance( position );
    }

    @Override
    public CharSequence getPageTitle( int position )
    {
        // Generate title based on item position
        return tabTitles[position];
    }
}