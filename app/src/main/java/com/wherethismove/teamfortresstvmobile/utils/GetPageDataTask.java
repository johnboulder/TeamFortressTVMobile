package com.wherethismove.teamfortresstvmobile.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;

/* TODO What this class needs to do
 * Replace every localized instance where an asyncTask is made to load a document
 *
 * Receive a url, receive a view, receive a fragment?
 * Return a document, and call a view
 * TODO change the fragment to a child of the callback listener object interface in pageView fragment
 * TODO make the type PageViewFragment.RefreshFragmentListCallback, and we can use any child callback with the function "callback
 * TODO to implement this function. That way there will only ever be on implementation
 *
 * TODO decide if this should recieve a progressbar or not
 */

public class GetPageDataTask
        extends AsyncTask< String, Void, Document >
{

    private PageViewFragment.GetDocumentCallback fragmentCallback;

    public GetPageDataTask( PageViewFragment.GetDocumentCallback fragment )
    {
        fragmentCallback = fragment;
    }

    @Override
    protected Document doInBackground( String... params )
    {
        Document document = null;
        try
        {
            document = Jsoup.connect( params[0] )
                    .get( );
        }
        catch( IOException e )
        {
            e.printStackTrace( );
            Log.e( null, e.getLocalizedMessage( ) );

            if( e instanceof SocketTimeoutException )
            {
                document = this.tryAgain( params );
            }
        }

        return document;
    }

    protected Document tryAgain( String... params )
    {
        Document document = null;
        try
        {
            document = Jsoup.connect( params[0] )
                    .get( );
        }
        catch( IOException e )
        {
            e.printStackTrace( );
            Log.e( null, e.getLocalizedMessage( ) );
        }

        return document;
    }

    @Override
    protected void onProgressUpdate( Void... values )
    {

    }

    @Override
    protected void onPostExecute( Document result )
    {
        if( fragmentCallback != null )
        {
            fragmentCallback.callback( result );
        }
    }
}
