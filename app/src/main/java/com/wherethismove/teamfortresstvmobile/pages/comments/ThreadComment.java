package com.wherethismove.teamfortresstvmobile.pages.comments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class ThreadComment {
    private String headerText;
    private String upVotes;
    private String forum;
    private String body;
    private String commentPostTime;
    private String postNumber;
    private String url;
    private Element mBodyElement;

    public ThreadComment( String header,
                          String frags,
                          String forum,
                          String body,
                          String commentPostTime,
                          String postNumber,
                          String url )
    {
        //mBodyElement = bodyElement;

        headerText = header;
        upVotes = frags;
        this.forum = forum;

        // Added for workaround related to html parsing see links:
        // http://stackoverflow.com/questions/23568481/weird-taghandler-behavior-detecting-opening-and-closing-tags/25836810#25836810
        // http://stackoverflow.com/questions/34692666/android-custom-quotespan-issue
        if( body != null )
        {
            body = body.replace( "<q>", "&zwj;<q>" );

            Document bodyDocument = Jsoup.parse( body );
            Elements elements = bodyDocument.select( ".quote-attr" );
            elements.tagName( "quote-username" );
            body = bodyDocument.toString( );
        }

        this.body = body;

        // Footer usually only consists of something like "posted 20 hours ago"
        // Removing the "posted" part makes it a little cleaner in the app.
        if( commentPostTime != null )
        {
            commentPostTime = commentPostTime.replace( "posted", "" );
        }

        this.commentPostTime = commentPostTime;

        this.postNumber = postNumber;

        //TODO check for malformed url
        // example: http://www.teamfortress.tv/28679/new-tribes-ascend#27
        this.url = url;
    }

    public String getHeaderText( )
    {
        return headerText;
    }

    public String getFragCount( )
    {
        return upVotes;
    }

    public String getForum( )
    {
        return forum;
    }

    public String getBody( )
    {
        return body;
    }

    public String getCommentPostTime( )
    {
        return commentPostTime;
    }

    public String getNumber( )
    {
        return postNumber;
    }

    public String getUrl( )
    {
        return url + "#" + postNumber;
    }

    public Element getBodyElement( )
    {
        return mBodyElement;
    }

}
