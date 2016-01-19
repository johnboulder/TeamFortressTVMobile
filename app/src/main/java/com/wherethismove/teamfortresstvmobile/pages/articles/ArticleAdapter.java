package com.wherethismove.teamfortresstvmobile.pages.articles;

import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.comments.CommentAdapter;
import com.wherethismove.teamfortresstvmobile.pages.comments.ThreadComment;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class ArticleAdapter extends CommentAdapter
{
    private final int ITEM_ARTICLE = 0;
    private final int ITEM_COMMENT = 1;

    public ArticleAdapter(Context context, ArrayList<ThreadComment> data)
    {
        super(context, data);
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == 0)? ITEM_ARTICLE : ITEM_COMMENT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi;
        int viewType = getItemViewType(position);
        if ( viewType == 1)
        {
            // TODO find best way to do this, may be using CommentAdapter.getView()?
            // to do so, must check that layout id is list_item_comment, must initialize
            // the class, and must ensure that none of those things lead to UI irregularities

            vi = inflater.inflate(R.layout.list_item_comment, null);

            ThreadComment current = mData.get(position);
            TextView posts = (TextView) vi.findViewById(R.id.comment_header);

            posts.setText(current.getHeaderText());

            TextView frags = (TextView) vi.findViewById(R.id.comment_frag_count);
            frags.setText(current.getFragCount());

            TextView body = (TextView) vi.findViewById(R.id.comment_body);
            body.setText(Html.fromHtml(current.getBody()));
            body.setAutoLinkMask(Linkify.WEB_URLS);

            TextView footer = (TextView) vi.findViewById(R.id.comment_footer);
            footer.setText(current.getFooter());

        }
        else
        {
            vi = inflater.inflate(R.layout.list_item_article, null);
            ThreadComment current = mData.get(position);

            TextView title = (TextView) vi.findViewById(R.id.article_title);
            title.setText(current.getHeaderText());

            TextView author = (TextView) vi.findViewById(R.id.article_author);
            author.setText(current.getForum());

            TextView body = (TextView) vi.findViewById(R.id.article_body);
            body.setText(Html.fromHtml(current.getBody()));
        }
        return vi;
    }
}
