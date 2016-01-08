package com.wherethismove.teamfortresstvmobile.pages;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.utils.ImageLoadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 * TODO handle when website not responding
 * TODO implement opening of sub_feature articles
 * TODO serialize the article data
 *
 */
public class HomePageFragment extends Fragment
{
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_URL = "url";

	private String mUrl;
	private Document document;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment HomePageFragment.
	 */
	public static HomePageFragment newInstance(String url)
	{
		HomePageFragment fragment = new HomePageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	public HomePageFragment()
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
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		else
			mUrl = "http://www.teamfortress.tv";

		populate();
	}

	public void populate()
	{
		// Populate the fragment
		class MyTask extends AsyncTask<Void, Void, Document>
		{
			@Override
			protected Document doInBackground(Void... params)
			{
				try
				{
					String url = mUrl;
					document = Jsoup.connect(url).get();
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
				//do stuff
                try
                {
                    updateFeaturedArticles(result);
                }
                catch(java.lang.NullPointerException e)
                {
                    //TODO tell users website is not responding and kill at
                    e.printStackTrace();
                }
			}
		}
		new MyTask().execute();
	}

	public void updateFeaturedArticles(Document document)
	{
		Element element = document.select("#features-container").first();
		Element elemMainImage = element.select("#main-feature img").first();
		Element elemFeatureTitle = element.select("#main-feature-title-wrapper").first();
		Element elemFeatureDesc = element.select("#main-feature-desc").first();

		Elements subFeatureImages = element.select("a.sub-feature img");
		Elements subFeatureTitles = element.select("a.sub-feature .sub-feature-title");

		Element elemSubFeatureImage1 = subFeatureImages.get(0);
		Element elemSubFeatureTitle1 = subFeatureTitles.get(0);

		Element elemSubFeatureImage2 = subFeatureImages.get(1);
		Element elemSubFeatureTitle2 = subFeatureTitles.get(1);

		Element elemSubFeatureImage3 = subFeatureImages.get(2);
		Element elemSubFeatureTitle3 = subFeatureTitles.get(2);

		//id = main-feature-title-wrapper
		//id = main-feature-desc

		//span class=sub-feature-title
		String url = elemMainImage.attr("src");

		if(!element.equals(null))
		{
			// Add the main feature image
			ImageView mainFeature = (ImageView) getView().findViewById(R.id.feature_main);
			ImageLoadTask task = new ImageLoadTask(url, mainFeature);
			task.execute();

			// Add the sub features image
			url = elemSubFeatureImage1.attr("src");
			ImageView subFeature1 = (ImageView) getView().findViewById(R.id.feature_sub1);
			new ImageLoadTask(url, subFeature1, 480, 270).execute();
			url = elemSubFeatureImage2.attr("src");
			ImageView subFeature2 = (ImageView) getView().findViewById(R.id.feature_sub2);
			new ImageLoadTask(url, subFeature2, 480, 270).execute();
			url = elemSubFeatureImage3.attr("src");
			ImageView subFeature3 = (ImageView) getView().findViewById(R.id.feature_sub3);
			new ImageLoadTask(url, subFeature3, 480, 270).execute();

			TextView featureTitle = (TextView) getView().findViewById(R.id.title_feature_main);
			featureTitle.setText(elemFeatureTitle.text());

			TextView featureDesc = (TextView) getView().findViewById(R.id.desc_feature_main);
			featureDesc.setText(elemFeatureDesc.text());
		}
		else
		{
			System.out.println("Null pointer returned");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home_page, container, false);
		populate();
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
