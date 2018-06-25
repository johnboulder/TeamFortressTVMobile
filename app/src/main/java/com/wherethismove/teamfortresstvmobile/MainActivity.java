package com.wherethismove.teamfortresstvmobile;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.wherethismove.teamfortresstvmobile.pages.AboutPageFragment;
import com.wherethismove.teamfortresstvmobile.pages.ErrorPageFragment;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.articles.ArticleViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.forums.Forum;
import com.wherethismove.teamfortresstvmobile.pages.forums.ForumTabFragment;
import com.wherethismove.teamfortresstvmobile.pages.forums.ForumsViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.HomePageFragment;
import com.wherethismove.teamfortresstvmobile.pages.threads.ThreadListViewFragment;
import com.wherethismove.teamfortresstvmobile.utils.GetPageDataTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomePageFragment.OnFragmentInteractionListener,
        ForumTabFragment.OnTabForumSelectedListener,
        ThreadListViewFragment.OnThreadSelectedListener,
        ThreadViewFragment.commentFiller,
        AboutPageFragment.OnFragmentInteractionListener
{
    Document document;
    public static URL WEBSITE_HOSTNAME;
    private final String PATH_FORUMS = "/forums";
    private final String PATH_THREADS = "/threads";
    private final String PATH_SCHEDULE = "/schedule";
    private final String PATH_NEWS = "/news";
    private final String PATH_GALLERIES = "/galleries";

    public final String ERROR_FRAGMENT_BSTACK_NAME = "error_fragment";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        /* Auto generated stuff */
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        /* End Auto generated stuff */

        try
        {
            WEBSITE_HOSTNAME = new URL( "http://www.teamfortress.tv" );
        }
        catch( MalformedURLException e )
        {
            e.printStackTrace( );
        }

        // TODO used to possibly ensure that fragments do not overlay eachother
        FrameLayout container = ( FrameLayout ) findViewById( R.id.fragment_container );
        container.removeAllViews( );

        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState( );

        NavigationView navigationView = ( NavigationView ) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        getSupportFragmentManager( ).addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener( ) {
                    public void onBackStackChanged( )
                    {
                        Fragment f = getSupportFragmentManager( ).findFragmentById( R.id.fragment_container );
                        if( f instanceof ThreadListViewFragment )
                        {
                            updateNavigationSelection( 1 );
                        }
                        else if( f instanceof ForumsViewFragment )
                        {
                            setTitle( "Forums" );
                            updateNavigationSelection( 2 );
                        }
                        else if( f instanceof HomePageFragment )
                        {
                            setTitle( "TF.TV Mobile" );
                            updateNavigationSelection( 0 );
                        }
                    }
                } );

        openHome( null );
    }

    private void updateNavigationSelection( int index )
    {
        NavigationView navigationView = ( NavigationView ) findViewById( R.id.nav_view );
        Menu menu = navigationView.getMenu( );
        for( int i = 0; i < menu.size( ); i++ )
        {
            MenuItem menuItem = menu.getItem( i );
            if( index == i )
            {
                menuItem.setChecked( true );
            }
            else
            {
                menuItem.setChecked( false );
            }
        }
    }

    /**
     * Populate the home screen with articles and images
     */
    public void openHome( View v )
    {
        HomePageFragment home = new HomePageFragment( );

        // Add a fragment
        FragmentManager fragmentManager = getSupportFragmentManager( );
        //home.setArguments(getIntent().getExtras());
        fragmentManager.beginTransaction( )
                .add( R.id.fragment_container,
                      home,
                      "home_page_fragment" )
                .commit( );

        new GetPageDataTask( new PageViewFragment.GetDocumentCallback( ) {
            @Override
            public void refreshList( Document document )
            {
                /**/
            }

            @Override
            public void callback( View view,
                                  Document result )
            {
                document = result;
            }
        },
                             v ).execute( WEBSITE_HOSTNAME.toString( ) );

    }

    public void onToggle( View view )
    {
        (( RadioGroup ) view.getParent( )).check( view.getId( ) );
    }

    public void openFeature( View v )
    {
        // TODO Animate the image condensing and contracting

        // TODO Open the article fragment, and populate it with the article stuff
        // id: article-wrapper
        Element element = document.select( "#features-container" )
                .first( );
        Element elemFeature = element.select( "a#main-feature" )
                .first( );
        String articleSubdomain = elemFeature.attr( "href" );

        openArticle( articleSubdomain );
    }

    public void openSubFeature1( View v )
    {
        // Animate the image condensing and contracting
        Element element = document.select( "a.sub-feature.first" )
                .first( );
        String articleSubdomain = element.attr( "href" );

        openArticle( articleSubdomain );
    }

    public void openSubFeature2( View v )
    {
        // Animate the image condensing and contracting
        Element element = document.select( "a.sub-feature" )
                .get( 1 );
        String articleSubdomain = element.attr( "href" );

        openArticle( articleSubdomain );
    }

    public void openSubFeature3( View v )
    {
        // Animate the image condensing and contracting
        Element element = document.select( "a.sub-feature" )
                .get( 2 );
        String articleSubdomain = element.attr( "href" );

        openArticle( articleSubdomain );
    }

    public void openArticle( String articleSubdomain )
    {
        Bundle args = new Bundle( );
        args.putString( PageViewFragment.ARG_URL,
                        WEBSITE_HOSTNAME + articleSubdomain );
        args.putInt( PageViewFragment.ARG_LAYOUT,
                     R.layout.fragment_article_view );
        ArticleViewFragment article = new ArticleViewFragment( );
        article.setArguments( args );

        // Replace fragment
        FragmentManager fm = getSupportFragmentManager( );
        fm.beginTransaction( )
                .replace( R.id.fragment_container,
                          article,
                          null )
                .addToBackStack( null )
                .commit( );
    }

    public void updateArticleList( Document document )
    {

    }

    @Override
    public void onBackPressed( )
    {
        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        if( drawer.isDrawerOpen( GravityCompat.START ) )
        {
            drawer.closeDrawer( GravityCompat.START );
        }
        else if( getSupportFragmentManager( ).getBackStackEntryCount( ) > 0 )
        {
            getSupportFragmentManager( ).popBackStack( );
        }
        else
        {
            this.finish( );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater( ).inflate( R.menu.main,
                                    menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId( );

        //noinspection SimplifiableIfStatement
        //		if (id == R.id.action_settings)
        //		{
        //			return true;
        //		}

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item )
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId( );

        if( id == R.id.nav_home )
        {
            setTitle( "TF.TV Mobile" );
            //http://www.teamfortress.tv/
            Bundle args = new Bundle( );
            args.putString( "url",
                            WEBSITE_HOSTNAME.toString() );
            args.putInt( PageViewFragment.ARG_LAYOUT,
                         R.layout.fragment_home_page );
            HomePageFragment home = new HomePageFragment( );
            home.setArguments( args );

            FragmentManager fragmentManager = getSupportFragmentManager( );
            fragmentManager.beginTransaction( )
                    .replace( R.id.fragment_container,
                              home,
                              null )
                    .addToBackStack( null )
                    .commit( );
        }
        else if( id == R.id.nav_threads )
        {
            setTitle( "Threads" );
            //http://www.teamfortress.tv/threads
            // Replace fragment
            Bundle args = new Bundle( );
            args.putString( "url",
                            WEBSITE_HOSTNAME.toString() + PATH_THREADS );
            args.putInt( PageViewFragment.ARG_LAYOUT,
                         R.layout.fragment_thread_list_view );
            ThreadListViewFragment threads = new ThreadListViewFragment( );
            threads.setArguments( args );

            FragmentManager fragmentManager = getSupportFragmentManager( );
            fragmentManager.beginTransaction( )
                    .replace( R.id.fragment_container,
                              threads,
                              "Threads" )
                    .addToBackStack( null )
                    .commit( );
        }
        else if( id == R.id.nav_forums )
        {
            setTitle( "Forums" );
            //http://www.teamfortress.tv/forums
            Bundle args = new Bundle( );
            args.putString( "url",
                            WEBSITE_HOSTNAME.toString() + PATH_FORUMS );
            args.putInt( PageViewFragment.ARG_LAYOUT,
                         R.layout.fragment_forums_tabbed_view );
            ForumsViewFragment forums = new ForumsViewFragment( );
            forums.setArguments( args );

            FragmentManager fragmentManager = getSupportFragmentManager( );
            fragmentManager.beginTransaction( )
                    .replace( R.id.fragment_container,
                              forums,
                              null )
                    .addToBackStack( null )
                    .commit( );
        }
        else if( id == R.id.nav_about )
        {
            // Open a page with contact info
            setTitle( "About" );
            Bundle args = new Bundle( );
            AboutPageFragment about = new AboutPageFragment( );
            about.setArguments( args );

            FragmentManager fragmentManager = getSupportFragmentManager( );
            fragmentManager.beginTransaction( )
                    .replace( R.id.fragment_container,
                              about,
                              null )
                    .addToBackStack( null )
                    .commit( );
        }
        //		else if (id == R.id.nav_schedule)
        //		{
        //            //http://www.teamfortress.tv/schedule
        //		}
        //		else if (id == R.id.nav_news)
        //		{
        //            //http://www.teamfortress.tv/news
        //		}
        //		else if (id == R.id.nav_galleries)
        //		{
        //
        //		}
        //		else if (id == R.id.nav_streams)
        //		{
        //
        //		}

        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    public void onFragmentInteraction( Uri uri )
    {

    }

    @Override
    public void openThread( String url )
    {
        String[] title = url.split( "/" );
        setTitle( title[4] );
        Bundle args = new Bundle( );
        args.putString( "url",
                        url );
        args.putInt( PageViewFragment.ARG_LAYOUT,
                     R.layout.fragment_thread_view );
        ThreadViewFragment thread = new ThreadViewFragment( );
        thread.setArguments( args );

        FragmentManager fm = getSupportFragmentManager( );
        fm.beginTransaction( )
                .replace( R.id.fragment_container,
                          thread,
                          null )
                .addToBackStack( null )
                .commit( );
    }

    @Override
    public void fillComments( String url,
                              Document d )
    {
    }


    //    public void errorLoadingPage() {
    //        // Change the current fragment to an appropriate image
    //        ErrorPageFragment errorFragment = new ErrorPageFragment();
    //        FragmentManager fm = getSupportFragmentManager();
    //        fm.beginTransaction().replace(R.id.fragment_container, errorFragment, ERROR_FRAGMENT_BSTACK_NAME).addToBackStack(ERROR_FRAGMENT_BSTACK_NAME).commit();
    //    }

    @Override
    public void onTabForumSelected( Forum f )
    {
        setTitle( f.getTitle( ) );
        // TODO merge the code for opening thread_list_views from this method and the navigation_drawer above
        Bundle args = new Bundle( );
        args.putString( "url",
                        f.getForumUrl( ) );
        args.putInt( "layout",
                     R.layout.fragment_thread_list_view );
        ThreadListViewFragment threads = new ThreadListViewFragment( );
        threads.setArguments( args );

        FragmentManager fm = getSupportFragmentManager( );
        fm.beginTransaction( )
                .replace( R.id.fragment_container,
                          threads,
                          f.getTitle( ) )
                .addToBackStack( null )
                .commit( );
    }
}
