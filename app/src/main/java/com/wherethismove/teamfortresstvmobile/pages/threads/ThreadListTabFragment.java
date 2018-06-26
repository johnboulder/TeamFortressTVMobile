package com.wherethismove.teamfortresstvmobile.pages.threads;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.wherethismove.teamfortresstvmobile.MainActivity;
import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.utils.GetNewPageDataTask;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 * TODO make abstract class?
 */
public class ThreadListTabFragment
        extends PageViewFragment
{
    protected ArrayList< ThreadListItem > listItems;
    private ThreadListAdapter mAdapter;
    private OnThreadSelectedListener mListener;
    private String mTitle;

    private int position;
    public static final String ARG_POSITION = "position";

    public ThreadListTabFragment( )
    {
        // Required empty public constructor
    }

    public static ThreadListTabFragment newInstance( int position )
    {
        ThreadListTabFragment fragment = new ThreadListTabFragment( );
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
            layout = R.layout.fragment_thread_list_tab;
            // Always the same
            url = "http://www.teamfortress.tv/threads";
        }
    }

    // TODO: refactor, View v param is unnecessary
    @Override
    protected void initializeList( View v )
    {
        switch( position )
        {
            case 0:
                url = url + "/?sort=hot";
                break;
            case 1:
                url = url + "/?sort=active";
                break;
            case 2:
                url = url + "/?sort=new";
                break;
            case 3:
                url = url + "/?sort=top";
                break;
        }
        final ListView lv = ( ListView ) v.findViewById( R.id.thread_list );

        // Fill the thread list with threads
        populateList( );

        mAdapter = new ThreadListAdapter( v.getContext( ), mListener, listItems );
        lv.setAdapter( mAdapter );

        // Setup the scroll listener which updates the contents of the listView whenever the last item in the list becomes visible
        // NOTE: This changes the value of document.
        // TODO get rid of calculations that occur in LoadListItemOnScrollListener
        // TODO set this up so that a callback is passed to LoadListItemOnScrollListener
        // and any calculations are done within the callback. Make it so the callback can be used
        // here or setOnRefreshListener. Consider making a URL object
        onScrollListener = new LoadListItemOnScrollListener(
                new RefreshFragmentListCallback( ) {
                    @Override
                    public void refreshList( Document doc )
                    {
                        document = doc;
                        populateList( );
                        mAdapter.notifyDataSetChanged( );
                    }
                },
                url
        );
        lv.setOnScrollListener( onScrollListener );

        // TODO find a way to merge this functionality with what LoadListItemOnScrollListener does
        View rootView = getView( );
        if( rootView != null )
        {
            swipeRefreshLayout = ( SwipeRefreshLayout ) rootView.findViewById( R.id.swipe_refresh );
            swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener( ) {

                @Override
                public void onRefresh( )
                {
                    // Just get the page again, the site already refreshes the data
                    new GetNewPageDataTask( new RefreshFragmentListCallback( ) {
                        // TODO refactor so only the changed portions of each listItem are changed
                        @Override
                        public void refreshList( Document doc )
                        {
                            listItems.clear( );
                            mAdapter.notifyDataSetChanged( );
                            document = doc;
                            populateList( );
                            mAdapter.notifyDataSetChanged( );
                            swipeRefreshLayout.setRefreshing( false );
                        }
                    } ).execute( url );
                }
            } );
        }

        mTitle = document.select( "#content > div:nth-child(4) > div > div:nth-child(2)" )
                .text( );
        if( mTitle == "" )
        {
            mTitle = "Threads";
        }
        getActivity( ).setTitle( mTitle );
    }

    @Override
    protected void populateList( )
    {
        if( listItems == null )
        {
            listItems = new ArrayList<>( );
        }

        Elements threads = document.select( "div.thread" );

        for( int i = 0; i < threads.size( ); i++ )
        {
            Element curThread = threads.get( i );
            Element frags = curThread.select( ".frag-count" )
                    .first( );

            Element mainData = curThread.select( "div.block.main" )
                    .first( );

            // Get the thread title
            String title = mainData.select( "a.title" )
                    .first( )
                    .text( );
            // Get the link to the thread
            String threadURL = mainData.select( "a.title" )
                    .first( )
                    .attr( "href" );
            String threadURLWithoutPage = MainActivity.URL_HOSTNAME + threadURL;
            // Get the ForumListItem it's in
            // Only appears when in the "Threads" section of the website
            Element description = mainData.select( "div.description" )
                    .first( );
            // Get the posts
            String posts = curThread.select( "span.post-count" )
                    .first( )
                    .text( );
            // Get the pages
            Integer pages = curThread.select( "a.thread-pages" )
                    .size( );
            // Get the OP TODO refer to ForumListViewFragment on getting usernames
            String op = "username";
            // Get the postTime (use div.description for username+postTime+forum)
            String postTime = mainData.select( "span.date-eta" )
                    .text( );

            ThreadListItem ft = new ThreadListItem( posts, pages.toString( ), op, title, description.text( ), frags.text( ), threadURLWithoutPage );

            listItems.add( ft );
        }
    }

    public void onThreadSelected( String url )
    {
        if( mListener != null )
        {
            mListener.openThread( url );
        }
    }

    @Override
    public void onResume( )
    {
        super.onResume( );
        getActivity( ).setTitle( mTitle );
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if( context instanceof OnThreadSelectedListener )
        {
            mListener = ( OnThreadSelectedListener ) context;
        }
        else
        {
            throw new RuntimeException( context.toString( )
                                                + " must implement OnThreadSelectedListener" );
        }
    }

    @Override
    public void onDetach( )
    {
        super.onDetach( );
        mListener = null;
    }

    public interface OnThreadSelectedListener {
        // TODO: Update argument type and name
        void openThread( String url );
    }
}
