package com.pend.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Sushil on 03/04/18.
 */

public class RegularFontTextView extends AppCompatTextView {

        public RegularFontTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public RegularFontTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public RegularFontTextView(Context context) {
            super(context);
            init();
        }

        private void init() {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/montserrat.ttf");
            setTypeface(tf ,1);

        }

    }

