package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/24/2015.
 * <p>
 * I don't understand why people criticize politicians for flip-flopping. Shouldn't we want politicians to change their stance when voters change their stance?
 */
public class ThreadListAdapter
        extends BaseAdapter
{
    private Context context;
    private ThreadListTabFragment.OnThreadSelectedListener buttonCallback;
    private ArrayList< ThreadListItem > data;
    private static LayoutInflater inflater = null;

    public ThreadListAdapter( Context context,
                              ThreadListTabFragment.OnThreadSelectedListener buttonCallback,
                              ArrayList< ThreadListItem > data )
    {
        this.context = context;
        this.buttonCallback = buttonCallback;
        this.data = data;
        inflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount( )
    {
        return data.size( );
    }

    @Override
    public Object getItem( int position )
    {
        return data.get( position );
    }

    @Override
    public long getItemId( int position )
    {
        return position;
    }

    @Override
    public View getView( int position,
                         View convertView,
                         ViewGroup parent )
    {
        ViewHolder viewHolder;
        final ThreadListItem current = data.get( position );

        if( convertView != null && convertView.getTag( ) != null )
        {
            viewHolder = ( ViewHolder ) convertView.getTag( );
        }
        else
        {
            convertView = inflater.inflate( R.layout.list_item_thread, null );
            viewHolder = new ViewHolder( );

            viewHolder.posts = convertView.findViewById( R.id.posts );

            viewHolder.pages = convertView.findViewById( R.id.pages );

            viewHolder.frags = convertView.findViewById( R.id.thread_frag_count );

            viewHolder.title = convertView.findViewById( R.id.title );

            viewHolder.post_time = convertView.findViewById( R.id.post_time );

            viewHolder.viewThread = convertView.findViewById( R.id.b_view_thread );

            convertView.setTag( viewHolder );
        }

        viewHolder.posts.setText( current.getNumberOfPosts( ) );
        viewHolder.pages.setText( current.getNumberOfPages( ) );
        viewHolder.frags.setText( current.getFrags( ) );
        viewHolder.title.setText( current.getTitle( ) );
        viewHolder.post_time.setText( current.getSubmissionTime( ) );
        viewHolder.viewThread.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v )
            {
                buttonCallback.openThread( current.getThreadUrl( ) );
            }
        } );

        return convertView;
    }

    static class ViewHolder {
        TextView posts;
        TextView pages;
        TextView frags;
        TextView title;
        TextView post_time;
        ImageButton viewThread;
    }
}
