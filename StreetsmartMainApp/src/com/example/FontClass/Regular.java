package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Regular extends TextView {

	public Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Regular(Context context) {
        super(context);
        init();
    }
    
    public void init() {

    	
    	
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Regular.otf");;
        setTypeface(tf);
        setTextColor(Color.parseColor("#999999"));
        //setLineSpacing(10f, 1f);
    }
}
