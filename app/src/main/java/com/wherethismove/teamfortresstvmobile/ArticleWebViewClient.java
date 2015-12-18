package com.wherethismove.teamfortresstvmobile;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by John Walter Stockwell on 12/7/2015.
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
				//+ "$('#wrapper').css({'width': 'inherit', 'padding-bottom': '0px', 'padding-top': '0px', 'padding': '0px'});"
				//+ "$('body').css({'minwidth': '0px'});"
				//+"$('document').width( $(window).width() );"
//				+"var NewStyles = document.createElement('link');"
//				+"NewStyles.href = 'http://www.wherethismove.com/css/article.css%3fv=3';"
//				+"NewStyles.rel='stylesheet';"
//				+"NewStyles.type='text/css';"
//				//+"document.getElementsByTagName('head')[0].appendChild(NewStyles);"
//				+"$(\"link[href='/css/base/article.css?v=3']\").replaceWith(NewStyles);"//.attr('href', 'http://www.wherethismove.com/css/article.css%3fv=3');
//				+"NewStyles.href = 'http://www.wherethismove.com/css/main.css%3fv=29';"
//				+"$(\"link[href='/css/tf/main.css?v=29']\").replaceWith(NewStyles);"
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