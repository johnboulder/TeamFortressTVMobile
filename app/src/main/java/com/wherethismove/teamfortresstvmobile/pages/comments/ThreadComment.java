package com.wherethismove.teamfortresstvmobile.pages.comments;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class ThreadComment
{
    private String mHeaderText;
    private String mFrags;
    private String mForum;
    private String mBody;
    private String mFooter;
    private String mPostNumber;
    private String mUrl;
    private Element mBodyElement;

    public ThreadComment(String header, String frags, String forum, String body, String footer, String postNumber, String url)
    {
        //mBodyElement = bodyElement;

        mHeaderText = header;
        mFrags = frags;
        mForum = forum;

        // Added for workaround related to html parsing see links:
        // http://stackoverflow.com/questions/23568481/weird-taghandler-behavior-detecting-opening-and-closing-tags/25836810#25836810
        // http://stackoverflow.com/questions/34692666/android-custom-quotespan-issue
        if(body != null)
            body.replace("<q>", "&zwj;<q>");
        mBody = body;

        mFooter = footer;
        mPostNumber = postNumber;

        //TODO check for malformed url
        // example: http://www.teamfortress.tv/28679/new-tribes-ascend#27
        mUrl = url;
    }

    public String getHeaderText()
    {
        return mHeaderText;
    }

    public String getFragCount()
    {
        return mFrags;
    }

    public String getForum()
    {
        return mForum;
    }

    public String getBody()
    {
        return mBody;
    }

    public String getFooter()
    {
        return mFooter;
    }

    public String getNumber()
    {
        return mPostNumber;
    }

    public String getUrl()
    {
        return mUrl+"#"+mPostNumber;
    }

    public Element getBodyElement()
    {
        return mBodyElement;
    }

}
