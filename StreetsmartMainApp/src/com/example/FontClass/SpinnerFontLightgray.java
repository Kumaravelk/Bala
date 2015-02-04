package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerFontLightgray extends Spinner {

	public SpinnerFontLightgray(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SpinnerFontLightgray(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpinnerFontLightgray(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Light.otf");;
       // setTypeface(tf);
       // setTextColor(Color.parseColor("#373737"));
        
    }
	
}