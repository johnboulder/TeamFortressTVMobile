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
public class ForumListAdapter
        extends BaseAdapter
{
    private Context mContext;
    private ArrayList< ForumListItem > mData;
    private static LayoutInflater inflater = null;

    public ForumListAdapter( Context context,
                             ArrayList< ForumListItem > data )
    {
        this.mContext = context;
        this.mData = data;
        inflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount( )
    {
        return mData.size( );
    }

    @Override
    public Object getItem( int position )
    {
        return mData.get( position );
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
        if( convertView == null )
        {
            convertView = inflater.inflate( R.layout.list_item_forum, null );
        }

        ForumListItem current = mData.get( position );
        TextView posts = convertView.findViewById( R.id.posts );
        posts.setText( current.getNumberOfPosts( ) );

        TextView threads = convertView.findViewById( R.id.threads );
        threads.setText( current.getNumberOfThreads( ) );

        TextView title = convertView.findViewById( R.id.title );
        title.setText( current.getTitle( ) );

        TextView last_active = convertView.findViewById( R.id.last_activity );
        last_active.setText( current.getLastActive( ) );

        TextView description = convertView.findViewById( R.id.description );
        description.setText( current.getLastActive( ) );

        return convertView;
    }
}
