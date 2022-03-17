package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.aoedb.R;
import com.aoedb.data.Civilization;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UniqueTechnologiesQuizActivity extends QuizActivity {

    int civID, civCount;
    String askedValue;
    boolean isCivQuestion;

    HashMap<String, List<String>> civToTech;
    HashMap<String, String> techToCiv;
    HashMap<String, String> techDescriptions;
    HashMap<Integer, String> civToNames;
    List<String> techNames, civNames;

    AppCompatAutoCompleteTextView selector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_unique_techs_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_unique_technologies_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.green_background));
        initializeValues();
        newRound();
    }

    @Override
    public int getQuiz(){
        return 6;
    }

    @Override
    protected void initializeValues(){
        super.initializeValues();

        civToTech = new HashMap<>();
        techToCiv = new HashMap<>();
        techDescriptions = new HashMap<>();
        civToNames = new HashMap<>();

        techNames = new ArrayList<>();
        civNames = new ArrayList<>();
        getTechsInfo();

        selector = findViewById(R.id.quiz_spinner);
        selector.setThreshold(1);
    }

    private void getTechsInfo(){
        HashMap<Integer,String> civRelation = Database.getCivNameMap();
        civCount = civRelation.size();
        for(int i: civRelation.keySet()){
            Civilization c = Database.getCivilization(i);
            String civName = c.getName();
            civToNames.put(i, civName);
            civNames.add(civName);
            ArrayList<String> a1 = new ArrayList<>();
            for(int t: c.getUniqueTechList()){
                techNames.add(Database.getTechnology(t).getName());
                techToCiv.put(Database.getTechnology(t).getName(), civName);
                techDescriptions.put(Database.getTechnology(t).getName(), Database.getTechnology(t).getDescriptor().getQuickDescription());
                a1.add(Database.getTechnology(t).getName());
            }
            civToTech.put(civName, a1);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okButton.getText().equals(getString(R.string.ok))) {
                    String s = selector.getText().toString();
                    if (isCivQuestion) {
                        if (civNames.contains(s)) {
                            if (s.equals(askedValue)) correctAnswer();
                            else wrongAnswer();
                        } else comment.setText(getString(R.string.quiz_please_select_civ));
                    }
                    else{
                        if (techNames.contains(s)) {
                            if (s.equals(askedValue)) correctAnswer();
                            else wrongAnswer();
                        } else comment.setText(getString(R.string.quiz_please_select_tech));
                    }
                }
                else{
                    ++currentQuestion;
                    if (currentQuestion <= numQuestions){
                        selector.setText("");
                        newRound();
                    }
                    else showResults();
                }
            }
        });
    }

    @Override
    public void newRound() {
        Random r = new Random();
        civID = r.nextInt(civCount)+1;
        String civName = civToNames.get(civID);
        List<String> s = civToTech.get(civName);
        int n1 = r.nextInt(s.size()); // choosing castle age or imperial age ut
        String techName = s.get(n1);
        String techDescription = techDescriptions.get(techName);

        gif.setImageResource(R.drawable.quiz);
        symbol.setImageResource(R.drawable.question);
        icon.setImageResource(R.drawable.crown2);
        iconName.setText(getString(R.string.civilization_unique_technologies));
        okButton.setText(getString(R.string.ok));
        String q;
        int n2 =r.nextInt(2);   //choosing between asking civ
        // name or asking tech name
        if (n2 ==0){    //asking civ name
            int n3 = r.nextInt(2);
            if (n3 == 0){  //showing tech name
                q = String.format(getString(R.string.quiz_unique_techs_civ_question_tech_name), currentQuestion, numQuestions, techName);
                correctionComment = String.format(getString(R.string.quiz_correction_civ), civName);
                comment.setText(getString(R.string.quiz_select_civ));
                askedValue = civName;
                isCivQuestion = true;
                TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(civNames));
                selector.setAdapter(adapter);
                selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                    }
                });
            }
            else{  // showing tech description
                q = String.format(getString(R.string.quiz_unique_techs_civ_question_tech_desc), currentQuestion, numQuestions, techDescription);
                correctionComment = String.format(getString(R.string.quiz_correction_civ), civName);
                askedValue = civName;
                isCivQuestion = true;
                comment.setText(getString(R.string.quiz_select_civ));
                TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(civNames));
                selector.setAdapter(adapter);
                selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                    }
                });
            }
        }
        else {      //asking tech name
            int n3 = r.nextInt(2);
            if (n3 == 0){  //showing civ name and tech age
                String ageName;
                if (n1 ==0) ageName = getString(R.string.castle_age);
                else ageName= getString(R.string.imperial_age);
                q = String.format(getString(R.string.quiz_unique_techs_question_civ_age), currentQuestion, numQuestions, civName, ageName);
                correctionComment = String.format(getString(R.string.quiz_correction_tech),techName);
                comment.setText(getString(R.string.quiz_select_tech));
                askedValue = techName;
                isCivQuestion = false;
                TextFilterAdapter adapter = new TextFilterAdapter(this,R.layout.autocomplete_text_layout, new ArrayList<>(techNames));
                selector.setAdapter(adapter);
                selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                    }
                });
            }
            else{  // showing tech description
                q = String.format(getString(R.string.quiz_unique_techs_question_desc),currentQuestion, numQuestions, techDescription);
                correctionComment = String.format(getString(R.string.quiz_correction_tech),techName);
                askedValue = techName;
                comment.setText(getString(R.string.quiz_select_tech));
                isCivQuestion = false;
                TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(techNames));
                selector.setAdapter(adapter);
                selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                    }
                });
            }
        }
        question.setText(q);
    }
}
