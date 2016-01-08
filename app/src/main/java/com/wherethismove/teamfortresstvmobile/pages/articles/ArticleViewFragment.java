package com.wherethismove.teamfortresstvmobile.pages.articles;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadComment;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ArticleViewFragment extends ThreadViewFragment
{
	private ArrayList<ThreadComment> listItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_article_web_view, container, false);
		final View v2 = v;

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
				populateList(v2);
			}
		}
		new MyTask().execute();
		return v;
	}

	// TODO find a way to use ThreadViewFragment initializeList for the comments section
	@Override
	protected void populateList(View v)
	{
		Element content = document.select("#content").first();
		// Set the thread title
		Element title = content.select("div.thread-header-title").first();
		Element author = content.select("div#article-meta").first();
		Element body = content.select("div#article-body").first();
		Element frags = document.select("span#thread-frag-count").first();
		Element forum = document.select("div.thread-header-desc").first();

		ThreadComment article = new ThreadComment(title.text(), frags.text(), author.text() + forum.text(), body.html(), null, null, mUrl);

		Elements comments = document.select("div.post");
		listItems = new ArrayList<>();

		listItems.add(article);

		final ListView lv = (ListView) v.findViewById(R.id.list_view_article_body_and_comments);

		for(int i = 0; i<comments.size(); i++)
		{
			Element curComment = comments.get(i);

			// Get the post number. Alternate selector "a.post-anchor"
			Element postNumber = curComment.select("div.post-num").first();
			// Get the header
			Element header = curComment.select("a.post-author").first();
			// Get the body
			body = curComment.select("div.post-body").first();
			// Get the footer
			Element footer = curComment.select("div.post-footer").first();

			frags = curComment.select("span.post-frag-count").first();

			//public ThreadComment(String header, String frags, String forum, String body, String footer, String postNumber, String url)
			ThreadComment tc = new ThreadComment(postNumber.text()+" "+ header.text(), frags.text(), forum.text(),body.html(), footer.text(), postNumber.text(), mUrl+postNumber.text());
			listItems.add(tc);
		}

		ArticleAdapter adapter = new ArticleAdapter(v.getContext(), listItems);
		lv.setAdapter(adapter);
	}
}
