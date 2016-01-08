package com.wherethismove.teamfortresstvmobile.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dell on 11/25/2015.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap>
{

	private String    url;
	private ImageView imageView;
	private int mViewWidth;
	private int mViewHeight;
    /*DNE = DOES NOT EXIST*/
    private final int DNE = -3;

	public ImageLoadTask(String url, ImageView imageView)
	{
		this.url = url;
		this.imageView = imageView;
		this.mViewWidth = DNE;
		this.mViewHeight = DNE;
	}

	public ImageLoadTask(String url, ImageView imageView, int width, int height)
	{
		this.url = url;
		this.imageView = imageView;
		this.mViewWidth = width;
		this.mViewHeight = height;
	}

	@Override
	protected Bitmap doInBackground(Void... params)
	{
		try
		{
			URL urlConnection = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlConnection
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		imageView.setImageBitmap(result);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setAdjustViewBounds(true);
		ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (mViewHeight>0 && mViewWidth>0)
        {
            imageView.setMaxWidth(mViewWidth);
            imageView.setMaxHeight(mViewHeight);
        }
        else if(mViewHeight!=DNE && mViewWidth!=DNE)
		{
			params.width = mViewWidth;
			params.height = mViewHeight;
		}
		else
		{
			params.width = ViewGroup.LayoutParams.MATCH_PARENT;
			params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		imageView.setLayoutParams(params);
		//imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		//ViewGroup p = imageView.getParent();
	}

}
