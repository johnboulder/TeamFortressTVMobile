package com.wherethismove.teamfortresstvmobile.pages.articles;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by John Walter Stockwell on 12/7/2015.
 * @deprecated
 * No longer used, found an alternate means of achieving the same result.
 */

public class ArticleWebViewClient extends WebViewClient
{

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override

	public void onPageFinished(WebView view, String url)
	{
		// Obvious next step is: document.forms[0].submit()
		//$('#navSub').children().hide();
		//$(name).show();

		// Change body
		// minwidth from 1000px to 0px

		// Changes #wrapper
		// width from 1310 to inherit
		// padding-bottom from 60px to 0px
		// padding-top from 17px to 0px
		// padding to 0px
		/**the below works, but I was unable to get the inclusion of the new css to work*/
		view.loadUrl("javascript:(function() { "
				+ "$('#article-wrapper').show().parentsUntil('content').andSelf().siblings().hide();"
				+"})()");

		// My attempt to return a string object containing the html data so I could debug it
		view.evaluateJavascript(
				"(function() { return ('<html>'+document.getElementsByTagName('link')[0].innerHTML+'</html>'); })();",
				new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String html) {
						Log.d("HTML", html);
						// code here
					}
				});
		//view.zoomIn();
	}

}