package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Bold extends TextView {

	public Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bold(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Extrabold.otf");;
        setTypeface(tf);
        setTextColor(Color.parseColor("#999999"));
        //setLineSpacing(10f, 1f);
    }
}
