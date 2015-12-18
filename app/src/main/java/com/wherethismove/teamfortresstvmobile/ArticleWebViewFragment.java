package com.wherethismove.teamfortresstvmobile;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleWebViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticleWebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleWebViewFragment extends Fragment
{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_URL = "url";

	// TODO: Rename and change types of parameters
	private String mUrl;
	private Document document;
	WebView wv;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param url Parameter 1.
	 * @return A new instance of fragment ArticleWebView.
	 */
	// TODO: Rename and change types and number of parameters
	public static ArticleWebViewFragment newInstance(String url)
	{
		ArticleWebViewFragment fragment = new ArticleWebViewFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	public ArticleWebViewFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			mUrl = getArguments().getString(ARG_URL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_article_web_view, container, false);
		wv = (WebView) view.findViewById(R.id.webViewFragment);

		class MyTask extends AsyncTask<Void, Void, Document>
		{
			@Override
			protected Document doInBackground(Void... params)
			{
				try
				{
					document = Jsoup.connect(mUrl).get();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return null;
				}

				return document;
			}

			@Override
			protected void onPostExecute(Document result)
			{
				Element ele = document.select("#content").first();
				String html = ele.toString();
				String htmlData = "<head><link rel='stylesheet' type='text/css' href='http://www.wherethismove.com/css/main.css%3fv=29' />"
						+"<link rel='stylesheet' type='text/css' href='http://www.wherethismove.com/css/article.css%3fv=3' />"
						+"</head>";
				//String htmlData1 = "<link rel='stylesheet' type='text/css' href='http://www.teamfortress.tv/css/tf/main.css?v=29' />";
				String mime = "text/html";
				String encoding = "utf-8";
				//wv.loadData(html, mime, encoding);
				wv.loadDataWithBaseURL(null,htmlData+html, mime, encoding, null);
			}
		}
		new MyTask().execute();
		wv.setWebViewClient(new WebViewClient());
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().

		//wv.loadUrl(mUrl);
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null)
		{
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = (OnFragmentInteractionListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}


	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener
	{
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
