package com.wherethismove.teamfortresstvmobile.pages.comments;

/**
 * Created by stockweezie on 1/28/2016.
 * Found here: http://stackoverflow.com/questions/34692666/android-custom-quotespan-issue
 */

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;

import com.wherethismove.teamfortresstvmobile.R;

public class CustomQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
    private static final int STRIPE_WIDTH = 5;
    private static final int GAP_WIDTH = 8;

    private final int mBackgroundColor;
    private final int mColor;

    public CustomQuoteSpan()
    {
        super();
        mBackgroundColor = 0xFFD4D4D4;
        mColor = 0xFF515151;
    }

    public int getLeadingMargin(boolean first)
    {
        return STRIPE_WIDTH + GAP_WIDTH;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end,
                                  boolean first, Layout layout)
    {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(mColor);

        c.drawRect(x, top, x + dir * STRIPE_WIDTH, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum)
    {
        int paintColor = p.getColor();
        p.setColor(mBackgroundColor);
        c.drawRect(left, top, right, bottom, p);
        p.setColor(paintColor);
    }
}