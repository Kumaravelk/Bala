package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

public class MyButtonView extends Button {

	public MyButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonView(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Regular.otf");;
        setTypeface(tf);
        setTextColor(Color.parseColor("#ffffff"));
        //setLineSpacing(10f, 1f);
    }
}
