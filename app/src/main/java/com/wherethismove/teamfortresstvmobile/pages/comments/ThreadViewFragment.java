package com.wherethismove.teamfortresstvmobile.pages.comments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.utils.GetPageDataTask;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * TODO streamline class so it can be called from ArticleViewFragment for filling comments
 */
public class ThreadViewFragment
        extends PageViewFragment
{

    private CommentAdapter mAdapter;
    private ArrayList< ThreadComment > listItems;
    protected commentFiller mListener;

    public ThreadViewFragment( )
    {
        // Required empty public constructor
    }

    public static ThreadViewFragment newInstance( String url,
                                                  int layout )
    {
        ThreadViewFragment fragment = new ThreadViewFragment( );
        Bundle args = new Bundle( );
        args.putInt( ARG_LAYOUT, layout );
        args.putString( ARG_URL, url );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    protected void initializeList( )
    {

        // Set the thread title
        listItems = new ArrayList<>( );
        Element headerTitle = document.select( "div.thread-header-title" )
                .first( );
        listItems.add( new ThreadComment( headerTitle.text( ), null, null, null, null, null, null ) );

        populateList( );

        final ListView lv = getView( ).findViewById( R.id.comments_list );
        mAdapter = new CommentAdapter( getView( ).getContext( ), listItems );
        lv.setAdapter( mAdapter );
        lv.setDescendantFocusability( FOCUS_BLOCK_DESCENDANTS );

        // Setup the scroll listener which updates the contents of the listView whenever the last
        // item in the list becomes visible
        // NOTE: This changes the value of document.
        lv.setOnScrollListener( new LoadListItemOnScrollListener(
                new GetDocumentCallback( ) {
                    @Override
                    public void callback( Document doc )
                    {
                        document = doc;
                        populateList( );
                        mAdapter.notifyDataSetChanged( );
                    }
                },
                urlUnchanging
        ) );

        //TODO find a way to merge this functionality with what LoadListItemOnScrollListener does
        swipeRefreshLayout = getView( ).findViewById( R.id.swipe_refresh );
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener( ) {

            @Override
            public void onRefresh( )
            {
                Integer page;
                Integer totalItemCount = listItems.size( ) - 1;
                if( totalItemCount % 30 == 0 )
                {
                    page = (listItems.size( ) / 30) + 1;
                }
                else
                {
                    // Ensures totalItemCount is a rounded off number, like 30, 60, 90, etc.
                    page = ((totalItemCount + (30 - totalItemCount % 30)) / 30) + 1;
                }
                // Take listItems.size()%30, if there's a remainder, stay on the page we have
                new GetPageDataTask( new GetDocumentCallback( ) {
                    @Override
                    public void callback( Document doc )
                    {
                        document = doc;
                        populateList( );
                        mAdapter.notifyDataSetChanged( );
                        swipeRefreshLayout.setRefreshing( false );
                    }
                } ).execute( urlUnchanging + "/?page=" + page.toString( ) );
            }
        } );
    }

    @Override
    protected void populateList( )
    {
        if( listItems == null )
        {
            listItems = new ArrayList<>( );
        }

        Element forum = document.select( "div.thread-header-desc" )
                .first( );
        Elements comments = document.select( "div.post" );

        for( int i = 0; i < comments.size( ); i++ )
        {
            Element curComment = comments.get( i );
            Element frags;
            if( curComment.className( )
                    .equals( "post self" ) )
            {
                frags = document.select( "span#thread-frag-count" )
                        .first( );
            }
            else
            {
                frags = curComment.select( "span.post-frag-count" )
                        .first( );
            }

            // Get the post number. Alternate selector "a.post-anchor"
            Element postNumber = curComment.select( "div.post-num" )
                    .first( );
            // Get the header
            Element header = curComment.select( "a.post-author" )
                    .first( );
            // Get the body
            Element body = curComment.select( "div.post-body" )
                    .first( );

            // Set the spoiler tags so they can be retrieved by HtmlTagHandler
            Elements spoilers = body.select( "div.spoiler-content" );
            spoilers.tagName( "spoiler" );
            // Remove all the spoilerbuttons so their text doesn't appear in things
            Elements spoilerButtons = body.select( "span.btn.spoiler.js-spoiler" );
            spoilerButtons.remove( );

            // Get the footer
            Element footer = curComment.select( "div.post-footer" )
                    .first( );
            String footer_s = footer.text( );
            // Split the commentPostTime text to remove the "quote" link and everything after it
            footer_s = footer_s.split( "quote" )[0];

            ThreadComment tc = new ThreadComment( postNumber.text( ) + " " + header.text( ), frags.text( ), forum.text( ), body.html( ), footer_s, postNumber.text( ), urlUnchanging + postNumber.text( ) );
            listItems.add( tc );
        }
    }

    public void populateComments( String s )
    {
        if( mListener != null )
        {
            mListener.fillComments( s, document );
        }
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if( context instanceof commentFiller )
        {
            mListener = ( commentFiller ) context;
        }
        else
        {
            throw new RuntimeException( context.toString( )
                                                + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach( )
    {
        super.onDetach( );
        mListener = null;
    }

    public interface commentFiller {
        // TODO: Update argument type and name
        void fillComments( String url,
                           Document d );
    }
}
