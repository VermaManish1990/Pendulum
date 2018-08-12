package com.pend.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Sushil on 04/04/18.
 */

public class RegularFontButton extends AppCompatButton {

    public RegularFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularFontButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/montserrat.ttf");
        setTypeface(tf ,1);

    }

}

