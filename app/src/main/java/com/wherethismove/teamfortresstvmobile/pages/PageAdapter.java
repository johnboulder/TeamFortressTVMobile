package com.wherethismove.teamfortresstvmobile.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wherethismove.teamfortresstvmobile.pages.threads.ForumThread;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 * Intended as a base class for the various list adapter variants that will be used in this
 * project.
 */
public abstract class PageAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<ForumThread> mData;
    private static LayoutInflater inflater = null;

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
