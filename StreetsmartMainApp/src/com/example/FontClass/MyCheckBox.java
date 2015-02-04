package com.example.FontClass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MyCheckBox extends CheckBox {

	public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCheckBox(Context context) {
        super(context);
        init();
    }
    
    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"ProximaNova-Regular.otf");;
        setTypeface(tf);
        setTextColor(Color.parseColor("#373737"));
        //setLineSpacing(10f, 1f);
    }
}
