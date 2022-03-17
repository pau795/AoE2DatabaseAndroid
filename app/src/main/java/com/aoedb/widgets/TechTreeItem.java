package com.aoedb.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aoedb.R;

public abstract class TechTreeItem extends LinearLayout {

    AttributeSet attrs;
    boolean topConnector, bottomConnector;
    boolean topLeftConnector, topRightConnector;
    boolean leftMargin, rightMargin;
    public TechTreeItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract void initView();

    protected void setupWidget(){
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TechTreeItem, 0,0);
        leftMargin = array.getBoolean(R.styleable.TechTreeItem_left_margin, false);
        rightMargin = array.getBoolean(R.styleable.TechTreeItem_right_margin, false);
        topConnector = array.getBoolean(R.styleable.TechTreeItem_top_connector, false);
        bottomConnector = array.getBoolean(R.styleable.TechTreeItem_bottom_connector, false);
        topLeftConnector = array.getBoolean(R.styleable.TechTreeItem_top_left_connector, false);
        topRightConnector = array.getBoolean(R.styleable.TechTreeItem_top_right_connector, false);
        connectors();
        margin();
    }

    public void setTopConnector(boolean b){
        topConnector = b;
        connectors();
    }

    public void setBottomConnector(boolean b){
        bottomConnector = b;
        connectors();
    }

    protected void margin(){
        View leftMarginView = findViewById(R.id.tt_box_left_margin);
        View rightMarginView = findViewById(R.id.tt_box_right_margin);
        if (leftMargin) leftMarginView.setVisibility(VISIBLE);
        else leftMarginView.setVisibility(GONE);
        if (rightMargin) rightMarginView.setVisibility(VISIBLE);
        else rightMarginView.setVisibility(GONE);
    }

    private void connectors(){
        View top = findViewById(R.id.tt_box_top_connector);
        View bottom = findViewById(R.id.tt_box_bottom_connector);
        View topLeft = findViewById(R.id.tt_box_top_left_connector);
        View topRight = findViewById(R.id.tt_box_top_right_connector);

        View topLeftAux = findViewById(R.id.tt_box_top_left_connector_aux);
        View topRightAux = findViewById(R.id.tt_box_top_right_connector_aux);



        if (topConnector) top.setVisibility(VISIBLE);
        else top.setVisibility(INVISIBLE);
        if (bottomConnector) bottom.setVisibility(VISIBLE);
        else bottom.setVisibility(INVISIBLE);

        if (topLeftConnector) {
            topLeft.setVisibility(VISIBLE);
            topLeftAux.setVisibility(VISIBLE);
        }
        else {
            topLeft.setVisibility(INVISIBLE);
            topLeftAux.setVisibility(GONE);
        }
        if (topRightConnector) {
            topRight.setVisibility(VISIBLE);
            topRightAux.setVisibility(VISIBLE);
        }
        else {
            topRight.setVisibility(INVISIBLE);
            topRightAux.setVisibility(GONE);
        }
    }
}
