package com.wherethismove.teamfortresstvmobile.pages.forums;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wherethismove.teamfortresstvmobile.MainActivity;
import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * TODO Implement this class with static forum list values, where when onSwipeRefresh occurs
 * the only values that are updated are the recent activity in each subforum
 */
public class ForumListTabFragment
        extends PageViewFragment
{
    private OnTabForumSelectedListener mListener;
    private int position;
    protected ArrayList< ForumListItem > listItems;
    public static final String ARG_POSITION = "position";


    public ForumListTabFragment( )
    {
        // Required empty public constructor
    }

    public static ForumListTabFragment newInstance( int position )
    {
        ForumListTabFragment fragment = new ForumListTabFragment( );
        Bundle args = new Bundle( );
        args.putInt( ARG_POSITION, position );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if( getArguments( ) != null )
        {
            position = getArguments( ).getInt( ARG_POSITION );
            // Always the same
            layout = R.layout.fragment_forum_tab;
            // Always the same
            url = "http://www.teamfortress.tv/forums";
        }
    }

    @Override
    protected void initializeList( View v )
    {
        // TODO This function is called many, many times when switching between tabs in the view.
        // TODO Make sure that it's not downloading it from the website every time.
        listItems = new ArrayList<>( );
        final ListView lv = ( ListView ) v.findViewById( R.id.list_view_forum );
        Elements forums = document.select( "div.forum-container" );
        Element subForum = forums.get( position );
        Elements subForums = subForum.select( "div.subforum" );


        for( int j = 0; j < subForums.size( ); j++ )
        {
            Element curSubForum = subForums.get( j );
            // Get the description
            Element desc = curSubForum.select( "div.block.main" )
                    .first( );
            // Get the post number
            Element posts = curSubForum.select( "div.blockr.post-count" )
                    .first( );
            // Get the Thread count
            Element threads = curSubForum.select( "div.blockr.thread-count" )
                    .first( );
            // Get latest activity
            Element lastActivity = curSubForum.select( "div.blockr.last-info" )
                    .first( );
            // Get the title
            Element title = curSubForum.select( "a.subforum-title" )
                    .first( );

            ForumListItem f = new ForumListItem( posts.text( ), threads.text( ), lastActivity.text( ), title.text( ), desc.text( ), MainActivity.URL_HOSTNAME + title.attr( "href" ) );
            listItems.add( f );
        }

        ForumListAdapter adapter = new ForumListAdapter( v.getContext( ), listItems );
        lv.setAdapter( adapter );

        lv.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {

            @Override
            public void onItemClick( AdapterView< ? > parent,
                                     View v,
                                     int position,
                                     long id )
            {

                // ListView Clicked item value
                ForumListItem itemValue = ( ForumListItem ) lv.getItemAtPosition( position );
                forumSelected( itemValue );
            }

        } );
    }

    @Override
    protected void populateList( )
    {

    }

    public void forumSelected( ForumListItem f )
    {
        if( mListener != null )
        {
            mListener.onTabForumSelected( f );
        }
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if( context instanceof OnTabForumSelectedListener )
        {
            mListener = ( OnTabForumSelectedListener ) context;
        }
        else
        {
            throw new RuntimeException( context.toString( )
                                                + " must implement OnTabForumSelectedListener" );
        }
    }

    @Override
    public void onDetach( )
    {
        super.onDetach( );
        mListener = null;
    }

    // Implemented by the activity that contains this fragment.
    // Used for swapping out the forum fragment, and swapping in the
    public interface OnTabForumSelectedListener {
        void onTabForumSelected( ForumListItem f );
    }
}
