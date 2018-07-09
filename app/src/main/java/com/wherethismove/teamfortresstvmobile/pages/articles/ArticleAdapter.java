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
public class ArticleAdapter
        extends CommentAdapter
{
    private final int ITEM_ARTICLE = 0;
    private final int ITEM_COMMENT = 1;

    public ArticleAdapter( Context context,
                           ArrayList< ThreadComment > data )
    {
        super( context, data );
    }

    @Override
    public int getItemViewType( int position )
    {
        return (position == 0) ? ITEM_ARTICLE : ITEM_COMMENT;
    }

    @Override
    public View getView( int position,
                         View convertView,
                         ViewGroup parent )
    {
        int viewType = getItemViewType( position );

        if( viewType == 1 )
        {
            // TODO find best way to do this, may be using CommentAdapter.getView()?
            // to do so, must check that layout id is list_item_comment, must initialize
            // the class, and must ensure that none of those things lead to UI irregularities

            ViewHolder viewHolder;

            if( convertView != null && convertView.getTag( ) != null )
            {
                viewHolder = ( ViewHolder ) convertView.getTag( );
            }
            else
            {
                convertView = inflater.inflate( R.layout.list_item_comment, null );
                viewHolder = new ViewHolder( );

                viewHolder.header = convertView.findViewById( R.id.comment_header_text );
                viewHolder.fragCount = convertView.findViewById( R.id.comment_frag_count );
                viewHolder.body = convertView.findViewById( R.id.comment_body );
                viewHolder.footer = convertView.findViewById( R.id.comment_footer );

                convertView.setTag( viewHolder );
            }

            ThreadComment current = data.get( position );
            viewHolder.header.setText( current.getHeaderText( ) );
            viewHolder.fragCount.setText( current.getFragCount( ) );
            viewHolder.body.setText( Html.fromHtml( current.getBody( ) ) );
            viewHolder.body.setAutoLinkMask( Linkify.WEB_URLS );
            viewHolder.footer.setText( current.getFooter( ) );
        }
        else
        {
            convertView = inflater.inflate( R.layout.list_item_article, null );
            ThreadComment current = data.get( position );

            TextView title = convertView.findViewById( R.id.article_title );
            title.setText( current.getHeaderText( ) );

            TextView author = convertView.findViewById( R.id.article_author );
            author.setText( current.getForum( ) );

            TextView body = convertView.findViewById( R.id.article_body );
            body.setText( Html.fromHtml( current.getBody( ) ) );
        }

        return convertView;
    }

    static class ViewHolder {
        TextView header;
        TextView fragCount;
        TextView body;
        TextView footer;
    }
}
