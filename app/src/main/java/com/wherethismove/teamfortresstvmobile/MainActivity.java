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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.wherethismove.teamfortresstvmobile.pages.PageViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.articles.ArticleViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.forums.Forum;
import com.wherethismove.teamfortresstvmobile.pages.forums.ForumTabFragment;
import com.wherethismove.teamfortresstvmobile.pages.forums.ForumsViewFragment;
import com.wherethismove.teamfortresstvmobile.pages.HomePageFragment;
import com.wherethismove.teamfortresstvmobile.pages.threads.ThreadListViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * TODO add functionality for updating the current fragment's data
 */
public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
        HomePageFragment.OnFragmentInteractionListener,
        ForumTabFragment.OnTabForumSelectedListener,
        ThreadListViewFragment.OnThreadSelectedListener,
		//ForumsViewFragment.OnForumSelectedListener,
		ThreadViewFragment.commentFiller
{
	Document document;
    public static String siteRoot = "http://www.teamfortress.tv";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String FORUMS = "/forums";
    private final String THREADS = "/threads";
    private final String SCHEDULE = "/schedule";
    private final String NEWS = "/news";
    private final String GALLERIES = "/galleries";

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

		openHome(null);
	}

	public void openHome(View v)
	{
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
		args.putString(PageViewFragment.ARG_URL, siteRoot+articleSubdomain);
		args.putInt(PageViewFragment.ARG_LAYOUT, R.layout.fragment_article_view);
		ArticleViewFragment article = new ArticleViewFragment();
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

		else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
			getSupportFragmentManager().popBackStack();
		}
		else
		{
			this.finish();
		}
//		else
//		{
//			super.onBackPressed();
//		}
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

	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home)
		{
            //http://www.teamfortress.tv/
			Bundle args = new Bundle();
			args.putString("url", siteRoot);
            args.putInt(PageViewFragment.ARG_LAYOUT, R.layout.fragment_home_page);
			HomePageFragment home = new HomePageFragment();
            home.setArguments(args);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_container,home, null).addToBackStack(null).commit();
		}
		else if (id == R.id.nav_threads)
		{
			//http://www.teamfortress.tv/threads
			// Replace fragment
            Bundle args = new Bundle();
            args.putString("url", siteRoot+THREADS);
			args.putInt(PageViewFragment.ARG_LAYOUT, R.layout.fragment_thread_list_view);
            ThreadListViewFragment threads = new ThreadListViewFragment();
            threads.setArguments(args);

			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().replace(R.id.fragment_container,threads, null).addToBackStack(null).commit();
		}
		else if (id == R.id.nav_forums)
		{
            //http://www.teamfortress.tv/forums
			Bundle args = new Bundle();
			args.putString("url", siteRoot+FORUMS);
            args.putInt(PageViewFragment.ARG_LAYOUT, R.layout.fragment_forums_tabbed_view);
			ForumsViewFragment forums = new ForumsViewFragment();
			forums.setArguments(args);

			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().replace(R.id.fragment_container,forums, null).addToBackStack(null).commit();
		}
		else if (id == R.id.nav_schedule)
		{
            //http://www.teamfortress.tv/schedule
		}
		else if (id == R.id.nav_news)
		{
            //http://www.teamfortress.tv/news
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

    @Override
    public void openThread(String url)
    {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt(PageViewFragment.ARG_LAYOUT, R.layout.fragment_thread_view);
        ThreadViewFragment thread = new ThreadViewFragment();
        thread.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container,thread, null).addToBackStack(null).commit();
    }

    @Override
    public void fillComments(String url, Document d)
    {
    }

//    @Override
//    public void onForumSelected(Forum f)
//    {
//
//    }

	@Override
	public void onTabForumSelected(Forum f)
	{
		// TODO merge the code for opening thread_list_views from this method and the navigation_drawer above
		Bundle args = new Bundle();
		args.putString("url", f.getPageUrl(1));
		args.putInt("layout", R.layout.fragment_thread_list_view);
		ThreadListViewFragment threads = new ThreadListViewFragment();
		threads.setArguments(args);

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.fragment_container,threads, null).addToBackStack(null).commit();
	}
}
