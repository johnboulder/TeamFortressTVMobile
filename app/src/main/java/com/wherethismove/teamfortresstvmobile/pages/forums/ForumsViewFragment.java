package com.wherethismove.teamfortresstvmobile.pages.forums;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

/**
 *
 */
public class ForumsViewFragment extends PageViewFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            url = getArguments().getString(ARG_URL);
            mLayout = getArguments().getInt(ARG_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager(), getContext()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    @Override
    protected void initializeList(View v)
    {
    }

    @Override
    protected void populateList()
    {
    }
}
