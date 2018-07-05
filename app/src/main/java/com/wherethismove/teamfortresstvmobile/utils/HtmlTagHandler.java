package com.wherethismove.teamfortresstvmobile.utils;

/**
 * Created by stockweezie on 1/28/2016.
 * Found here: http://stackoverflow.com/questions/34692666/android-custom-quotespan-issue
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;

import org.xml.sax.XMLReader;

import com.wherethismove.teamfortresstvmobile.R;
import com.wherethismove.teamfortresstvmobile.pages.comments.CustomQuoteSpan;

public class HtmlTagHandler implements Html.TagHandler
{
    private Context mContext;
    private static final float[] HEADER_SIZES = {1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f};

    public HtmlTagHandler()
    {
    }

    public HtmlTagHandler(Context context)
    {
        mContext = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader)
    {
        // Handles strikethroughs, this came with the code
        if (tag.equalsIgnoreCase("del"))
        {
            if (opening) {
                start(output, new Strikethrough());
            } else {
                end(output, Strikethrough.class, new StrikethroughSpan());
            }
        }
        // Handles monospacing? This came with the code
        else if (tag.equalsIgnoreCase("pre"))
        {
            if (opening)
            {
                start(output, new Monospace());
            } else {
                end(output, Monospace.class, new TypefaceSpan("monospace"));
            }
        }
        // Handles quoting of comments
        else if (tag.equalsIgnoreCase("q"))
        {
            if (opening)
            {
                //handleP(output);
                start(output, new BlockQuote());
            }
            else
            {
                handleP(output);
                end(output, BlockQuote.class, new CustomQuoteSpan());
                append(output, CustomQuoteSpan.class, new TextAppearanceSpan(mContext, R.style.AppTheme_QuoteText));
            }
        }
        // Handles the usernames within quotes
        else if (tag.equalsIgnoreCase("quote-username"))
        {
            if (opening)
            {
                start(output, new UserName());
            }
            // End of the span tag
            else
            {
                output.append("\n");
                // TODO add this color to the color codes so it can be referenced elsewhere
                end(output, UserName.class, new ForegroundColorSpan(0xFF007099));
                append(output, ForegroundColorSpan.class, new StyleSpan(Typeface.BOLD));
            }
        }
        // Handles spoiler content
        else if(tag.equalsIgnoreCase("spoiler"))
        {
            if (opening)
            {
                start(output, new Spoiler());
            }
            // End of the span tag
            else
            {
                output.append("\n");
                end(output, Spoiler.class, new TextAppearanceSpan(mContext, R.style.AppTheme));
            }
        }
    }

    private static void handleP(Editable text) {
        int len = text.length();

        if (len >= 1 && text.charAt(len - 1) == '\n')
        {
            if (len >= 2 && text.charAt(len - 2) == '\n')
            {
                return;
            }

            text.append("\n");
            return;
        }

        if (len != 0)
        {
            text.append("\n\n");
        }
    }

    private static Object getLast(Spanned text, Class kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0)
        {
            return null;
        }
        else
        {
            return objs[objs.length - 1];
        }
    }

    private static void start(Editable text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
    }

    private static void end(Editable text, Class kind, Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len)
        {
            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static void append(Editable text, Class kind, Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);

        if (where != len)
        {
            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static class Strikethrough {}
    public static class Monospace {}
    public static class BlockQuote {}
    public static class UserName {}
    public static class Spoiler {}
}