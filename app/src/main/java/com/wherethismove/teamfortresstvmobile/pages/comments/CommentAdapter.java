package com.wherethismove.teamfortresstvmobile.pages.comments;

import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.utils.HtmlTagHandler;

import java.util.ArrayList;

/**
 * Created by stockweezie on 12/29/2015.
 */
public class CommentAdapter
        extends BaseAdapter
{

    private final int ITEM_TITLE = 0;
    private final int ITEM_COMMENT = 1;
    protected Context context;
    protected ArrayList< ThreadComment > data;
    protected static LayoutInflater inflater = null;

    public CommentAdapter( Context context,
                           ArrayList< ThreadComment > data )
    {
        this.context = context;
        this.data = data;
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
        return data.size( );
    }

    @Override
    public Object getItem( int position )
    {
        return data.get( position );
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

        // Create a comment list item
        if( viewType == 1 )
        {
            ViewHolder viewHolder;
            if( convertView == null )
            {
                //ListView recycles views and will pass the wrong vi...
                convertView = inflater.inflate( R.layout.list_item_comment, null );
                viewHolder = new ViewHolder( );

                viewHolder.header = convertView.findViewById( R.id.comment_header_text );

                viewHolder.fragCount = convertView.findViewById( R.id.comment_frag_count );


                viewHolder.body = convertView.findViewById( R.id.comment_body );

                viewHolder.footer = convertView.findViewById( R.id.comment_footer );

                convertView.setTag( viewHolder );
            }
            else
            {
                viewHolder = ( ViewHolder ) convertView.getTag( );
            }

            ThreadComment current = data.get( position );
            viewHolder.header.setText( current.getHeaderText( ) );
            viewHolder.fragCount.setText( current.getFragCount( ) );
            if( Integer.valueOf( current.getFragCount( ) ) > 0 )
            {
                viewHolder.fragCount.setTextColor( convertView.getResources( )
                                                           .getColor( R.color.frag_green ) );
            }
            else if( Integer.valueOf( current.getFragCount( ) ) < 0 )
            {
                viewHolder.fragCount.setTextColor( convertView.getResources( )
                                                           .getColor( R.color.colorAccent ) );
            }
            viewHolder.body.setText( Html.fromHtml( current.getBody( ), null, new HtmlTagHandler( context ) ) );
            viewHolder.body.setAutoLinkMask( Linkify.WEB_URLS );

            viewHolder.footer.setText( current.getFooter( ) );
        }
        // Create the title list item
        else
        {
            //ListView recycles views and will pass the wrong vi...
            convertView = inflater.inflate( R.layout.list_item_title, null );

            ThreadComment current = data.get( position );
            TextView title = convertView.findViewById( R.id.thread_title );
            title.setText( current.getHeaderText( ) );
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
