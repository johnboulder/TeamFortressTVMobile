package com.wherethismove.teamfortresstvmobile.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

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
//		System.setProperty("https.agent", "");
//		System.setProperty("http.agent", "");

		try
		{

//            SSLContext sslcontext = SSLContext.getInstance("TLSv1");
//
//            sslcontext.init(null,
//                    null,
//                    null);
//            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
//            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

			URL urlConnection = new URL(url);
			URLConnection connection = urlConnection.openConnection();
			//connection.setRequestProperty("User-Agent","User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
            connection.getContent();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				String[] urlParts = url.split("/");
				String newUrl = "http://i.imgur.com/"+urlParts[urlParts.length-1];
				URL urlConnection = new URL(newUrl);
				URLConnection connection = urlConnection.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				connection.getContent();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
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
		else if(result == null)
		{
			params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
			params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
