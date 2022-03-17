package com.aoedb.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aoedb.R;

public class TechTreeEmptyBox extends TechTreeItem {


    public TechTreeEmptyBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        initView();
        setupWidget();
        setLine();
    }

    protected void initView(){
        inflate(getContext(), R.layout.tech_tree_empty_box, this);
    }

    private void setLine(){
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TechTreeEmptyBox, 0,0);
        boolean line = array.getBoolean(R.styleable.TechTreeBox_enabled, false);
        View v = findViewById(R.id.tt_box_line);
        if (line) v.setVisibility(VISIBLE);
        else v.setVisibility(INVISIBLE);
        setBottomConnector(line);
        setTopConnector(line);
    }

}
