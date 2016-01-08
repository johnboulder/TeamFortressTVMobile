package com.wherethismove.teamfortresstvmobile.pages.comments;

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


    public ThreadComment(String header, String frags, String forum, String body, String footer, String postNumber, String url)
    {
        mHeaderText = header;
        mFrags = frags;
        mForum = forum;
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
}
