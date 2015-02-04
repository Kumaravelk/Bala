package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Lightgray extends TextView {

	public Lightgray(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Lightgray(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Lightgray(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Light.otf");
        setTypeface(tf);
        setTextColor(Color.parseColor("#373737"));
        //setLineSpacing(10f, 1f);
    }
}
