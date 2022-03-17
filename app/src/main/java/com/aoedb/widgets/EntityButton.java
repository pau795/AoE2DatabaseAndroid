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
import com.aoedb.activities.BuildingTabbedActivity;
import com.aoedb.activities.TechnologyTabbedActivity;
import com.aoedb.activities.UnitTabbedActivity;
import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

public class EntityButton extends LinearLayout {

    String title;
    AttributeSet attrs;

    public EntityButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        initView();

    }

    protected void initView() {
        inflate(getContext(), R.layout.entity_button, this);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.EntityButton, 0,0);
        title = array.getString(R.styleable.EntityButton_buttonTittle);
        TextView titleText = findViewById(R.id.button_title);
        titleText.setText(title);
    }

    public void setupButton(final Entity entity, final boolean click){
        TextView nameText = findViewById(R.id.button_name);
        ImageView icon = findViewById(R.id.button_icon);

        final EntityElement element = entity.getEntityElement(title);
        final String type = element.getType();


        nameText.setText(element.getName());
        icon.setImageResource(element.getImage());
        if(element.getImage() == R.drawable.t_white) icon.setBackground(null);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showPopupIcon(v, getContext(), element.getName(), element.getImage(), false, "blue");
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.getId() != 0 && click) {
                    Intent i;
                    switch (type) {
                        case Database.UNIT:
                            i = new Intent(getContext(), UnitTabbedActivity.class);
                            break;
                        case Database.BUILDING:
                            i = new Intent(getContext(), BuildingTabbedActivity.class);
                            break;
                        case Database.TECH:
                            i = new Intent(getContext(), TechnologyTabbedActivity.class);
                            break;
                        default:
                            i = new Intent();
                            break;
                    }
                    i.putExtra(Database.ENTITY, element.getId());
                    if (!type.isEmpty()) {
                        int civID = Database.getSelectedCiv(entity.getType(), entity.getEntityID()); //get current entity civID
                        Database.setSelectedCiv(type, element.getId(), civID); //set clicked element civID
                    }
                    getContext().startActivity(i);
                }
            }
        });
    }

}
