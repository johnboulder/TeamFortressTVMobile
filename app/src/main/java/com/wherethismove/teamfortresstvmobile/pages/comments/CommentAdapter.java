package com.wherethismove.teamfortresstvmobile.pages.comments;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.utils.HtmlTagHandler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class CommentAdapter
        extends BaseAdapter
{

    private final int ITEM_TITLE = 0;
    private final int ITEM_COMMENT = 1;
    protected Context mContext;
    protected ArrayList< ThreadComment > mData;
    protected static LayoutInflater inflater = null;

    public CommentAdapter( Context context,
                           ArrayList< ThreadComment > data )
    {
        this.mContext = context;
        this.mData = data;
        inflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getItemViewType( int position )
    {
        return (position == 0) ? ITEM_TITLE : ITEM_COMMENT;
    }

    @Override
    public int getCount( )
    {
        return mData.size( );
    }

    @Override
    public Object getItem( int position )
    {
        return mData.get( position );
    }

    @Override
    public long getItemId( int position )
    {
        return position;
    }

    @Override
    public View getView( int position,
                         View convertView,
                         ViewGroup parent )
    {
        int viewType = getItemViewType( position );

        View vi = convertView;

        // Create a comment list item
        if( viewType == 1 )
        {
            //ListView recycles views and will pass the wrong vi...
            vi = inflater.inflate( R.layout.list_item_comment, null );

            ThreadComment current = mData.get( position );

            TextView posts = vi.findViewById( R.id.comment_header_text );
            posts.setText( current.getHeaderText( ) );

            TextView frags = vi.findViewById( R.id.comment_frag_count );
            frags.setText( current.getFragCount( ) );

            // BODY
            TextView body = vi.findViewById( R.id.comment_body );
            body.setText( Html.fromHtml( current.getBody( ), null, new HtmlTagHandler( mContext ) ) );
            body.setAutoLinkMask( Linkify.WEB_URLS );
            // BODY END

            TextView footer = vi.findViewById( R.id.comment_footer );
            footer.setText( current.getFooter( ) );
        }
        // Create the title list item
        else
        {
            //ListView recycles views and will pass the wrong vi...
            vi = inflater.inflate( R.layout.list_item_title, null );

            ThreadComment current = mData.get( position );
            TextView title = vi.findViewById( R.id.thread_title );
            title.setText( current.getHeaderText( ) );
        }
        return vi;
    }
}
