package com.wherethismove.teamfortresstvmobile.pages.threads;

/**
 * Created by John Walter Stockwell on 12/24/2015.
 */
public class ThreadListItem
{
    private String mUpvotes;
    private String mNumberResponses;
    private String mNumberPages;
    private String mOriginalPoster;
    private String mThreadTitle;
    private String mThreadSubmissionTime;
    private String mForum;
    private String mLatestResponseTime;
    private String mLatestResponse;
    private String mUrl;


    public ThreadListItem( String responses, String pages, String op, String title, String postTime, String frags, String url)
    {
        mNumberResponses = "Posts: "+responses;
        mNumberPages = "Pages: "+pages;
        mOriginalPoster = op;
        mThreadTitle = title;
        mThreadSubmissionTime = postTime;
        mUpvotes = frags;
        //TODO check for malformed url
        mUrl = url;
    }

    public String getNumberOfPosts()
    {
        return mNumberResponses;
    }

    public String getNumberOfPages()
    {
        return mNumberPages;
    }

    public String getOPName()
    {
        return mOriginalPoster;
    }

    public String getTitle()
    {
        return mThreadTitle;
    }

    public String getSubmissionTime()
    {
        return mThreadSubmissionTime;
    }

    public String getPageUrl(Integer pageNumber)
    {
        return mUrl+"/?page="+pageNumber.toString();
    }

    public String getThreadUrl()
    {
        return mUrl;
    }

    public String getFrags()
    {
        return mUpvotes;
    }
}
