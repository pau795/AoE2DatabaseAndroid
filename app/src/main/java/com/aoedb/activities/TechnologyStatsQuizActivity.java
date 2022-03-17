package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.aoedb.R;
import com.aoedb.data.Building;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Technology;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TechnologyStatsQuizActivity extends QuizActivity {

    int techID;
    Double askedValue, askedValue2;
    int costQuestion;

    List<Integer> techList;

    View doubleAnswerDivider;
    LinearLayout singleAnswerLayout, doubleAnswerLayout, doubleAnswerLayout2;
    ImageView res1, res2;
    AppCompatEditText singleAnswer, doubleAnswer1, doubleAnswer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_technology_stats_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_technology_stats_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.green_background));
        initializeValues();
        newRound();
    }

    @Override
    public int getQuiz(){
        return 5;
    }

    private void getUnitsInfo(){
        List<EntityElement> u = Database.getList(Database.TECH_LIST);
        for (EntityElement e:u) if (notForbidden(e.getId())) techList.add(e.getId());
    }

    private boolean notForbidden (int id){
        switch (id) {
            case 9:
            case 142:
                return false;
            default: return true;
        }
    }

    @Override
    public void initializeValues(){
        super.initializeValues();
        techList = new ArrayList<>();
        costQuestion = 0;
        getUnitsInfo();
        singleAnswerLayout = findViewById(R.id.quiz_single_answer_layout);
        doubleAnswerLayout = findViewById(R.id.quiz_double_answer_layout);
        doubleAnswerLayout2 = findViewById(R.id.quiz_double_answer_layout2);
        doubleAnswerDivider = findViewById(R.id.quiz_double_answer_divider);
        res1 = findViewById(R.id.quiz_res1);
        res2 = findViewById(R.id.quiz_res2);
        singleAnswer = findViewById(R.id.quiz_single_answer);
        doubleAnswer1 = findViewById(R.id.quiz_double_answer1);
        doubleAnswer2 = findViewById(R.id.quiz_double_answer2);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (costQuestion == 0) {
                    if (okButton.getText().equals(getString(R.string.ok))) {
                        String s = singleAnswer.getText().toString();
                        if (!s.isEmpty()) {
                            if (Double.compare(Double.parseDouble(s), askedValue) == 0) correctAnswer();
                            else wrongAnswer();
                        } else comment.setText(getString(R.string.quiz_write_value_please));
                    } else {
                        ++currentQuestion;
                        if (currentQuestion <= numQuestions) {
                            singleAnswer.setText("");
                            newRound();
                        } else showResults();
                    }
                }
                else {
                    if (okButton.getText().equals(getString(R.string.ok))) {
                        if (costQuestion ==1) {
                            String s = doubleAnswer1.getText().toString();
                            if (!s.isEmpty()) {
                                if (Double.compare(Double.parseDouble(s), askedValue) == 0) correctAnswer();
                                else wrongAnswer();
                            } else comment.setText(getString(R.string.quiz_write_value_please));
                        }
                        else {
                            String s1 = doubleAnswer1.getText().toString();
                            String s2 = doubleAnswer2.getText().toString();
                            if (!s1.isEmpty() && !s2.isEmpty()) {
                                if (Double.compare(Double.parseDouble(s1),askedValue) == 0 && Double.compare(Double.parseDouble(s2),askedValue2) == 0) correctAnswer();
                                else wrongAnswer();
                            } else comment.setText(getString(R.string.quiz_write_value_please));
                        }
                    } else {
                        ++currentQuestion;
                        if (currentQuestion <= numQuestions) {
                            doubleAnswer1.setText("");
                            doubleAnswer2.setText("");
                            newRound();
                        } else showResults();
                    }
                }
            }
        });
    }


    @Override
    public void newRound() {
        Random r = new Random();
        int n = r.nextInt(techList.size());
        okButton.setText(getString(R.string.ok));
        comment.setText(getString(R.string.quiz_write_value));
        costQuestion = 0;
        techID = techList.get(n);
        Technology t = Database.getTechnology(techID);
        int buildingID = t.getCreatorElement().getId();
        Building b = Database.getBuilding(buildingID);
        gif.setImageResource(b.getNameElement().getMedia());
        icon.setImageResource(t.getNameElement().getImage());
        iconName.setText(t.getName());
        symbol.setImageResource(R.drawable.question);
        int n1 = r.nextInt(2);
        prepareLayouts(t, n1);
    }

    private void prepareLayouts(Technology t, int n) {
        switch (n) {
            case 0:
                askedValue = t.getBaseStat(Database.TRAINING_TIME);
                String q2 = String.format(getString(R.string.quiz_tech_stats_research_time_question), currentQuestion, numQuestions, t.getName());
                question.setText(q2);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_tech_stats_research_time_correction),(int)t.getBaseStat(Database.TRAINING_TIME), t.getName());
                break;
            case 1:
                String q3 = String.format(getString(R.string.quiz_tech_stats_cost_question), currentQuestion, numQuestions, t.getName());
                question.setText(q3);
                doubleAnswerLayout.setVisibility(View.VISIBLE);
                singleAnswerLayout.setVisibility(View.GONE);
                HashMap<String, Integer> cost = t.getBaseCost();
                if (cost.size() == 1) {
                    for(String res: cost.keySet()){
                        doubleAnswerLayout2.setVisibility(View.GONE);
                        doubleAnswerDivider.setVisibility(View.GONE);
                        doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        askedValue = Double.valueOf(cost.get(res));
                        costQuestion = 1;
                        res1.setImageResource(Utils.getResourceIcon(res));
                        correctionComment = String.format(getString(R.string.quiz_tech_stats_cost_correction), t.getName(), cost.get(res), getString(Utils.getResourceString(res)))+".";
                    }

                } else {
                    int r = 1;
                    doubleAnswerLayout2.setVisibility(View.VISIBLE);
                    doubleAnswerDivider.setVisibility(View.VISIBLE);
                    doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    comment.setText(R.string.quiz_write_value);
                    costQuestion = 2;
                    for(String res: cost.keySet()) {
                        if(r == 1){
                            askedValue = Double.valueOf(cost.get(res));
                            res1.setImageResource(Utils.getResourceIcon(res));
                            correctionComment = String.format(getString(R.string.quiz_tech_stats_cost_correction), t.getName(), cost.get(res), getString(Utils.getResourceString(res)));
                        }
                        else{
                            askedValue2 = Double.valueOf(cost.get(res));
                            correctionComment +=" " + String.format(getString(R.string.quiz_cost_correction2), cost.get(res), getString(Utils.getResourceString(res)));
                            res2.setImageResource(Utils.getResourceIcon(res));

                        }
                        ++r;
                    }
                }
                break;
        }
    }
}
