package com.pend.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

public class RegularFontRadioButton extends AppCompatRadioButton {

    public RegularFontRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularFontRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularFontRadioButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/montserrat.ttf");
        setTypeface(tf ,1);

    }
}