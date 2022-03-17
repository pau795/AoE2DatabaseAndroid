package com.aoedb.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.widgets.CivQuizDialog;


public class NewQuizActivity extends DrawerActivity{

    Context ctx;

    int numQuestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_new_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.orange_background));
        ctx=this;
        initialize_buttons();
    }

    private void initialize_buttons(){
        numQuestions=10;
        LinearLayout b1 = findViewById(R.id.tech_tree_quiz);
        LinearLayout b2 = findViewById(R.id.civ_theme_quiz);
        LinearLayout b3 = findViewById(R.id.unit_stats_quiz);
        LinearLayout b4 = findViewById(R.id.unit_recognition_quiz);
        LinearLayout b5 = findViewById(R.id.technology_stats_quiz);
        LinearLayout b6 = findViewById(R.id.unique_techs_quiz);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumQuestions(1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                civQuizSetup();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumQuestions(3);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumQuestions(4);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumQuestions(5);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumQuestions(6);
            }
        });

    }

    public void civQuizSetup(){
        CivQuizDialog cdd = new CivQuizDialog(this);
        cdd.show();
    }

    public void getNumQuestions(final int q) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        TextView title = new TextView(this);
        title.setText(getString(R.string.quiz_select_questions));
        title.setPadding(10, 20, 10, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getColor(R.color.black));
        title.setTextSize(20);
        builder.setCustomTitle(title);
        String[] items = {getString(R.string.quiz_10_questions), getString(R.string.quiz_20_questions), getString(R.string.quiz_30_questions)};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i1) {
                if (i1 == 0) numQuestions = 10;
                else if (i1 == 1) numQuestions = 20;
                else numQuestions = 30;
            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                switch (q){
                    case 1:
                        Intent i = new Intent(ctx, TechTreeQuizActivity.class);
                        i.putExtra("questions", numQuestions);
                        startActivity(i);
                        break;
                    case 2:
                        Intent i2 = new Intent(ctx, CivilizationQuizActivity.class);
                        i2.putExtra("questions", numQuestions);
                        startActivity(i2);
                        break;
                    case 3:
                        Intent i3 = new Intent(ctx, UnitStatsQuizActivity.class);
                        i3.putExtra("questions", numQuestions);
                        startActivity(i3);
                        break;
                    case 4:
                        Intent i4 = new Intent(ctx, UnitRecognitionQuizActivity.class);
                        i4.putExtra("questions", numQuestions);
                        startActivity(i4);
                        break;
                    case 5:
                        Intent i5 = new Intent(ctx, TechnologyStatsQuizActivity.class);
                        i5.putExtra("questions", numQuestions);
                        startActivity(i5);
                        break;
                    case 6:
                        Intent i6 = new Intent(ctx, UniqueTechnologiesQuizActivity.class);
                        i6.putExtra("questions", numQuestions);
                        startActivity(i6);
                        break;
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null);
        dialog = builder.create();
        dialog.show();
    }

}