package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class SemiBoldWhite extends TextView {

	public SemiBoldWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SemiBoldWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemiBoldWhite(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Semibold.otf");
        setTypeface(tf);
        setTextColor(Color.parseColor("#ffffff"));
        //setLineSpacing(10f, 1f);
    }
}
