package com.wherethismove.teamfortresstvmobile.pages.forums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class ForumAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Forum> mData;
    private static LayoutInflater inflater = null;

    public ForumAdapter(Context context, ArrayList<Forum> data)
    {
        this.mContext = context;
        this.mData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.forum_list_item, null);

        Forum current = mData.get(position);
        TextView posts = (TextView) vi.findViewById(R.id.posts);
        posts.setText(current.getNumberOfPosts());

        TextView threads = (TextView) vi.findViewById(R.id.threads);
        threads.setText(current.getNumberOfThreads());

        TextView title = (TextView) vi.findViewById(R.id.title);
        title.setText(current.getTitle());

        TextView last_active = (TextView) vi.findViewById(R.id.last_activity);
        last_active.setText(current.getLastActive());

        TextView description = (TextView) vi.findViewById(R.id.description);
        description.setText(current.getLastActive());

        return vi;
    }
}
