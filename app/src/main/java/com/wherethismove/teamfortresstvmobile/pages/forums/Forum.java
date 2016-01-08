package com.wherethismove.teamfortresstvmobile.pages.forums;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class Forum
{
    private String mNumberResponses;
    private String mNumberThreads;
    private String mLastActive;
    private String mDescription;
    private String mThreadTitle;
    private String mUrl;


    public Forum(String posts, String threads, String lastActive, String title, String description, String url)
    {
        mNumberResponses = "Posts: "+posts;
        mNumberThreads = "Threads: "+threads;
        mLastActive = lastActive;
        mThreadTitle = title;
        mDescription = description;
        //TODO check for malformed url
        mUrl = url;
    }

    public String getNumberOfPosts()
    {
        return mNumberResponses;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public String getNumberOfThreads()
    {
        return mNumberThreads;
    }

    public String getLastActive()
    {
        return mLastActive;
    }

    public String getTitle()
    {
        return mThreadTitle;
    }

    public String getPageUrl(Integer pageNumber)
    {
        return mUrl+"/?page="+pageNumber.toString();
    }
}