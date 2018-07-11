package com.wherethismove.teamfortresstvmobile.pages.articles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadComment;
import com.wherethismove.teamfortresstvmobile.utils.GetPageDataTask;
import com.wherethismove.teamfortresstvmobile.utils.LoadListItemOnScrollListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

// TODO make this class a child of ThreadViewFragment
// TODO make enable users to open links to pages
public class ArticleViewFragment
        extends PageViewFragment
{
    private ArrayList< ThreadComment > listItems;
    private ArticleAdapter mAdapter;

    public static ArticleViewFragment newInstance( String url,
                                                   int layout )
    {
        ArticleViewFragment fragment = new ArticleViewFragment( );
        Bundle args = new Bundle( );
        args.putString( ARG_URL, url );
        args.putInt( ARG_LAYOUT, layout );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    protected void initializeList( )
    {
        final ListView lv = getView( ).findViewById( R.id.list_view_article_body_and_comments );

        populateArticle( );
        populateList( );

        mAdapter = new ArticleAdapter( getView( ).getContext( ), listItems );
        lv.setAdapter( mAdapter );

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
                // Just get the page again, the site already refreshes the data
                new GetPageDataTask( new GetDocumentCallback( ) {

                    // TODO refactor so only the changed portions of each listItem are changed
                    @Override
                    public void callback( Document doc )
                    {
                        listItems.clear( );
                        mAdapter.notifyDataSetChanged( );
                        document = doc;
                        populateArticle( );
                        populateList( );
                        mAdapter.notifyDataSetChanged( );
                        swipeRefreshLayout.setRefreshing( false );
                    }
                } ).execute( urlUnchanging );
            }
        } );
    }

    private void populateArticle( )
    {
        Element content = document.select( "#content" )
                .first( );
        // Set the thread title
        Element title = content.select( "div.thread-header-title" )
                .first( );
        Element author = content.select( "div#article-meta" )
                .first( );
        Element body = content.select( "div#article-body" )
                .first( );
        Element frags = document.select( "span#thread-frag-count" )
                .first( );
        Element forum = document.select( "div.thread-header-desc" )
                .first( );

        ThreadComment article = new ThreadComment( title.text( ), frags.text( ), author.text( ) + forum.text( ), body.html( ), null, null, urlUnchanging );

        if( listItems == null )
        {
            listItems = new ArrayList<>( );
        }

        listItems.add( article );
    }

    // TODO find a way to use ThreadViewFragment initializeList for the comments section
    @Override
    protected void populateList( )
    {
        Element forum = document.select( "div.thread-header-desc" )
                .first( );
        Elements comments = document.select( "div.post" );

        for( int i = 0; i < comments.size( ); i++ )
        {
            Element curComment = comments.get( i );

            // Get the post number. Alternate selector "a.post-anchor"
            Element postNumber = curComment.select( "div.post-num" )
                    .first( );
            // Get the header
            Element header = curComment.select( "a.post-author" )
                    .first( );
            // Get the body
            Element body = curComment.select( "div.post-body" )
                    .first( );
            // Get the commentPostTime
            Element footer = curComment.select( "div.post-footer" )
                    .first( );
            String footer_s = footer.text( );
            // Split the commentPostTime text to remove the "quote" link and everything after it
            footer_s = footer_s.split( "quote" )[0];

            Element frags = curComment.select( "span.post-frag-count" )
                    .first( );

            //public ThreadComment(String header, String frags, String forum, String body, String commentPostTime, String postNumber, String url)
            ThreadComment tc = new ThreadComment( postNumber.text( ) + " " + header.text( ), frags.text( ), forum.text( ), body.html( ), footer_s, postNumber.text( ), urlUnchanging + postNumber.text( ) );
            listItems.add( tc );
        }
    }

}
