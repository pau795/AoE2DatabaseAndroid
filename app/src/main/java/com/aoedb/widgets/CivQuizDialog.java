package com.aoedb.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.aoedb.R;
import com.aoedb.activities.CivilizationQuizActivity;

public class CivQuizDialog extends Dialog {

    AppCompatCheckBox cBonuses;
    AppCompatCheckBox cUniqueUnits;
    AppCompatCheckBox cThemes;
    AppCompatCheckBox cEmblems;
    RadioGroup radioGroup;
    int numQuestions;
    int numChecked;
    boolean[] quizSetup;
    Context ctx;


    public CivQuizDialog(Context context) {
        super(context);
        ctx = context;
        setContentView(R.layout.dialog_new_civ_quiz);
        radioGroup = findViewById(R.id.newcivquiz_questions);
        cBonuses = findViewById(R.id.newcivquiz_bonuses_checkbox);
        cUniqueUnits = findViewById(R.id.newcivquiz_uniqueunits_checkbox);
        cThemes = findViewById(R.id.newcivquiz_themes_checkbox);
        cEmblems = findViewById(R.id.newcivquiz_emblems_checkbox);
        numChecked = 4;
        quizSetup = new boolean[]{true,true,true,true};
        Button okButton = findViewById(R.id.newcivquiz_ok);
        Button cancelButton = findViewById(R.id.newcivquiz_cancel);
        LinearLayout bonusesLayout = findViewById(R.id.bonuses_layout);
        LinearLayout uniqueUnitsLayout = findViewById(R.id.unique_units_layout);
        LinearLayout themesLayout = findViewById(R.id.themes_layout);
        LinearLayout emblemsLayout = findViewById(R.id.emblems_layout);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numQuestions = getNumQuestions();
                Intent i = new Intent(ctx, CivilizationQuizActivity.class);
                i.putExtra("questions", numQuestions);
                i.putExtra("bonuses", quizSetup[0]);
                i.putExtra("units", quizSetup[1]);
                i.putExtra("themes", quizSetup[2]);
                i.putExtra("emblems", quizSetup[3]);
                ctx.startActivity(i);
                dismiss();
            }
        });

        //at least one check box has to be enabled
        bonusesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cBonuses.isChecked() && numChecked > 1){
                    cBonuses.setChecked(false);
                    --numChecked;
                    quizSetup[0] = false;
                }
                else if (!cBonuses.isChecked()){
                    cBonuses.setChecked(true);
                    ++numChecked;
                    quizSetup[0] = true;
                }

            }
        });
        uniqueUnitsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cUniqueUnits.isChecked() && numChecked > 1){
                    cUniqueUnits.setChecked(false);
                    --numChecked;
                    quizSetup[1] = false;
                }
                else if (!cUniqueUnits.isChecked()){
                    cUniqueUnits.setChecked(true);
                    ++numChecked;
                    quizSetup[1] = true;
                }
            }
        });
        themesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cThemes.isChecked() && numChecked > 1){
                    cThemes.setChecked(false);
                    --numChecked;
                    quizSetup[2] = false;
                }
                else if (!cThemes.isChecked()){
                    cThemes.setChecked(true);
                    ++numChecked;
                    quizSetup[2] = true;
                }
            }
        });
        emblemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (cEmblems.isChecked() && numChecked > 1){
                    cEmblems.setChecked(false);
                    --numChecked;
                    quizSetup[3] = false;
                }
                else if (!cEmblems.isChecked()){
                    cEmblems.setChecked(true);
                    ++numChecked;
                    quizSetup[3] = true;
                }
            }
        });
    }

    private int getNumQuestions(){
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.checkbox10) return 10;
        else if (checkedRadioButtonId == R.id.checkbox20) return 20;
        else if (checkedRadioButtonId == R.id.checkbox30) return 30;
        return 0;
    }


}
