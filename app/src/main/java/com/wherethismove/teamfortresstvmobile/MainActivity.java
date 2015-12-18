package com.wherethismove.teamfortresstvmobile;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, HomePageFragment.OnFragmentInteractionListener, ArticleWebViewFragment.OnFragmentInteractionListener
{
	Document document;
	String siteRoot = "http://www.teamfortress.tv";
	WebViewFragment articleFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		/* Auto generated stuff */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);


		/*Populate the home screen*****************************************************************/

		HomePageFragment home = new HomePageFragment();

		// Add a fragment
		FragmentManager fm = getSupportFragmentManager();
		//home.setArguments(getIntent().getExtras());
		fm.beginTransaction().add(R.id.fragment_container, home, "home_page_fragment").commit();

		class MyTask extends AsyncTask<Void, Void, Document>
		{
			@Override
			protected Document doInBackground(Void... params)
			{
				try
				{
					String url = siteRoot;
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
			}
		}
		new MyTask().execute();
	}

	public void openFeature(View v)
	{
		// TODO Animate the image condensing and contracting

		// TODO Open the article fragment, and populate it with the article stuff
		// id: article-wrapper
		Element element = document.select("#features-container").first();
		Element elemFeature = element.select("a#main-feature").first();
		String articleSubdomain = elemFeature.attr("href");

		Bundle args = new Bundle();
		args.putString("url", siteRoot+articleSubdomain);
		ArticleWebViewFragment article = new ArticleWebViewFragment();
		article.setArguments(args);

		// Replace fragment
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.fragment_container,article, null).addToBackStack(null).commit();
	}

	public void openSubFeature1()
	{
		// Animate the image condensing and contracting
	}

	public void openSubFeature2()
	{
		// Animate the image condensing and contracting
	}

	public void openSubFeature3()
	{
		// Animate the image condensing and contracting
	}

	public void updateArticleList(Document document)
	{

	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}

		else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStack();
		}
//		else if
//		{
//			this.finish();
//		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home)
		{
			// Handle the camera action
		}
		else if (id == R.id.nav_threads)
		{

		}
		else if (id == R.id.nav_forums)
		{

		}
		else if (id == R.id.nav_schedule)
		{

		}
		else if (id == R.id.nav_news)
		{

		}
		else if (id == R.id.nav_galleries)
		{

		}
		else if (id == R.id.nav_streams)
		{

		}
		else if (id == R.id.nav_about)
		{

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onFragmentInteraction(Uri uri)
	{

	}

}
