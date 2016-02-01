package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/24/2015.
 *
 * I don't understand why people criticize politicians for flip-flopping. Shouldn't we want politicians to change their stance when voters change their stance?
 */
public class ThreadAdapter extends BaseAdapter
{
    private Context mContext;
    private ThreadListViewFragment.OnThreadSelectedListener mButtonCallback;
    private ArrayList<ForumThread> mData;
    private static LayoutInflater inflater = null;

    public ThreadAdapter(Context context, ThreadListViewFragment.OnThreadSelectedListener buttonCallback, ArrayList<ForumThread> data)
    {
        this.mContext = context;
        this.mButtonCallback = buttonCallback;
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

        View vi = inflater.inflate(R.layout.list_item_thread, null);

        final ForumThread current = mData.get(position);
        TextView posts = (TextView) vi.findViewById(R.id.posts);
        posts.setText(current.getNumberOfPosts());

        TextView pages = (TextView) vi.findViewById(R.id.pages);
        pages.setText(current.getNumberOfPages());

//        TextView poster = (TextView) vi.findViewById(R.id.poster);
//        poster.setText(current.getOPName());

        TextView frags = (TextView) vi.findViewById(R.id.thread_frag_count);
        frags.setText(current.getFrags());

        TextView title = (TextView) vi.findViewById(R.id.title);
        title.setText(current.getTitle());

        TextView post_time = (TextView) vi.findViewById(R.id.post_time);
        post_time.setText(current.getSubmissionTime());

        ImageButton viewThread = (ImageButton) vi.findViewById(R.id.b_view_thread);
        viewThread.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mButtonCallback.openThread(current.getThreadUrl());
            }
        });

        return vi;
    }
}
