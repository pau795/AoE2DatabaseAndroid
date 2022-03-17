package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.data.EntityElement;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UnitRecognitionQuizActivity extends QuizActivity {

    int unitID;

    List<String> unitNames;
    List<Integer> unitList;
    HashMap<String, Integer> unitRelation;

    TextView unitAttack, unitHP, unitPArmor, unitMArmor, unitRange;
    AutoCompleteTextView unitSelector;

    Unit u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_unit_recognition_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_unit_recognition_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        initializeValues();
        newRound();
    }

    private boolean notForbidden (int id){
        if (id == 147) {
            return false;
        }
        return true;
    }

    @Override
    public int getQuiz(){
        return 4;
    }

    @Override
    protected void initializeValues(){
        super.initializeValues();
        unitNames = new ArrayList<>();
        unitList =  new ArrayList<>();
        unitRelation =  new HashMap<>();
        getUnitsInfo();
        unitAttack = findViewById(R.id.quiz_attack);
        unitHP = findViewById(R.id.quiz_hp);
        unitRange = findViewById(R.id.quiz_range);
        unitPArmor = findViewById(R.id.quiz_pierce_armor);
        unitMArmor = findViewById(R.id.quiz_melee_armor);
        unitSelector = findViewById(R.id.quiz_spinner);
        TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(unitNames));
        unitSelector.setThreshold(0);
        unitSelector.setAdapter(adapter);
        unitSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }

        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okButton.getText().equals(getString(R.string.ok))) {
                    String s = unitSelector.getText().toString();
                    if (unitNames.contains(s)) {
                        icon.setImageResource(u.getNameElement().getImage());
                        icon.setBackgroundColor(getColor(R.color.blue_border));
                        iconName.setText(u.getName());
                        gif.setImageResource(u.getNameElement().getMedia());
                        if (unitRelation.get(s) == unitID) correctAnswer();
                        else wrongAnswer();
                    }
                    else comment.setText(getString(R.string.quiz_please_select_unit));
                }
                else{
                    ++currentQuestion;
                    if (currentQuestion <= numQuestions){
                        unitSelector.setText("");
                        newRound();
                    }
                    else showResults();
                }
            }
        });
    }

    private void getUnitsInfo() {
        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        for (EntityElement e : u){
            if (notForbidden(e.getId())) {
                unitList.add(e.getId());
                unitNames.add(e.getName());
                unitRelation.put(e.getName(), e.getId());
            }
        }
    }

    @Override
    public void newRound() {
        Random r = new Random();
        int n = r.nextInt(unitList.size());
        unitID = unitList.get(n);
        u = Database.getUnit(unitID);
        String q = String.format(getString(R.string.quiz_unit_recognition_question), currentQuestion, numQuestions);
        comment.setText(getString(R.string.quiz_select_unit));
        question.setText(q);
        unitHP.setText(Utils.getDecimalString(u.getBaseStat(Database.HP), 2));
        unitAttack.setText(Utils.getDecimalString(u.getBaseStat(Database.ATTACK), 2));
        unitRange.setText(Utils.getDecimalString(u.getBaseStat(Database.RANGE), 2));
        unitMArmor.setText(Utils.getDecimalString(u.getBaseStat(Database.MELEE_ARMOR), 2));
        unitPArmor.setText(Utils.getDecimalString(u.getBaseStat(Database.PIERCE_ARMOR), 2));
        symbol.setImageResource(R.drawable.question);
        gif.setImageResource(R.drawable.quiz);
        icon.setImageResource(R.drawable.unknown1);
        icon.setBackground(null);
        iconName.setText(getString(R.string.quiz_unit_recognition));
        correctionComment = String.format(getString(R.string.quiz_correction_unit), u.getName());
        okButton.setText(getString(R.string.ok));
    }
}
