package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class AutoTextView extends AutoCompleteTextView {

	public AutoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoTextView(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Light.otf");;
        setTypeface(tf);
        setTextColor(Color.parseColor("#999999"));
        //setLineSpacing(10f, 1f);
    }
}
