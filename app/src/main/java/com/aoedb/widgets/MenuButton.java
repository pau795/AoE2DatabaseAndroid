package com.aoedb.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aoedb.R;
import com.aoedb.database.Utils;

public class MenuButton extends LinearLayout {

    String title;
    int iconID;
    String intentClass;
    AttributeSet attrs;
    boolean hasBorder;
    Context ctx;

    public MenuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        ctx = context;
        initView();

    }

    protected void initView() {
        inflate(getContext(), R.layout.menu_button, this);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MenuButton, 0,0);
        title = array.getString(R.styleable.MenuButton_buttonTittle);
        iconID = array.getResourceId(R.styleable.MenuButton_buttonIcon, 0);
        intentClass = array.getString(R.styleable.MenuButton_buttonIntentClass);
        hasBorder = array.getBoolean(R.styleable.MenuButton_hasBorder, true);
        TextView nameText = findViewById(R.id.button_name);
        final ImageView icon = findViewById(R.id.button_icon);
        nameText.setText(title);
        icon.setImageResource(iconID);
        if (!hasBorder) icon.setBackground(null);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, getContext(), title, iconID, !hasBorder, "green");
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getContext(), Class.forName(intentClass));
                    getContext().startActivity(i);
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
