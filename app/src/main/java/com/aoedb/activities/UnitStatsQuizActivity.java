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
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class UnitStatsQuizActivity extends QuizActivity {

    int unitID;
    double askedValue, askedValue2;
    int costQuestion;

    List<Integer> unitList, buildingList;

    View doubleAnswerDivider;
    LinearLayout singleAnswerLayout, doubleAnswerLayout, doubleAnswerLayout2;
    ImageView res1, res2;
    AppCompatEditText singleAnswer, doubleAnswer1, doubleAnswer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_unit_building_stats_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_unit_stats_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.blue_background));
        initializeValues();
        newRound();
    }

    @Override
    public int getQuiz(){
        return 3;
    }

    private void getUnitsInfo(){
        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        for (EntityElement e:u) if (notForbidden(e.getId(), Database.UNIT)) unitList.add(e.getId());
        List<EntityElement> b = Database.getList(Database.BUILDING_LIST);
        for (EntityElement e:b)  if (notForbidden(e.getId(), Database.BUILDING)) buildingList.add(e.getId());
    }

    private boolean notForbidden (int id, String entity){
        switch (entity) {
            case Database.UNIT:
                switch (id) {
                    case 2:
                    case 3:
                    case 5:
                    case 9:
                    case 22:
                    case 23:
                    case 39:
                    case 102:
                        return false;
                    default:
                        return true;
                }
            case Database.BUILDING:
                switch (id) {
                    case 7:
                    case 19:
                        return false;
                    default:
                        return true;
                }
                default: return true;
        }
    }

    @Override
    public void initializeValues(){
        super.initializeValues();
        unitList = new ArrayList<>();
        buildingList =  new ArrayList<>();
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
                            if (Double.compare(Double.parseDouble(s),askedValue) == 0) correctAnswer();
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
        int n = r.nextInt(buildingList.size()+unitList.size());
        okButton.setText(getString(R.string.ok));
        comment.setText(getString(R.string.quiz_write_value));
        costQuestion = 0;
        if (n < buildingList.size()){   //building
            unitID = buildingList.get(n);
            Building b = Database.getBuilding(unitID);
            Double attack = b.getBaseStat(Database.ATTACK);
            gif.setImageResource(b.getNameElement().getMedia());
            icon.setImageResource(b.getNameElement().getImage());
            iconName.setText(b.getName());
            symbol.setImageResource(R.drawable.question);
            if (!Double.isNaN(attack)){
                int n1 = r.nextInt(2);
                if (n1==0){ //Building with attack, asking random attack value
                    LinkedHashMap<String, LinkedHashMap<Integer, Double>> l = b.getBaseAttackValues();
                    LinkedHashMap<Integer, Double> l1 = l.entrySet().iterator().next().getValue();
                    List<Integer> lel = new ArrayList<>(l1.keySet());
                    int type = lel.get(r.nextInt(lel.size()));
                    askedValue = l1.get(type);
                    String askedName = Database.getElement(Database.TYPE_LIST, type).getName();
                    String q = String.format(getString(R.string.quiz_unit_attack_question), currentQuestion, numQuestions, askedName, b.getName());
                    question.setText(q);
                    correctionComment = String.format(getString(R.string.quiz_unit_attack_correction), b.getName(), (int)askedValue, askedName);
                    doubleAnswerLayout.setVisibility(View.GONE);
                    singleAnswerLayout.setVisibility(View.VISIBLE);
                }
                else{ //Building with attack, asking random base stat
                    int n2 = r.nextInt(6);
                    prepareLayouts(b, n2);
                }
            }
            else{   //building with no attack
                if (unitID == 28){ //if the wonder is chosen, avoid asking the cost
                    int n2 = r.nextInt(2);
                    prepareLayouts(b, n2);
                }
                else{
                    int n2 = r.nextInt(3);
                    prepareLayouts(b, n2);
                }
            }
            symbol.setImageResource(R.drawable.question);
            gif.setImageResource(b.getNameElement().getMedia());
            icon.setImageResource(b.getNameElement().getImage());
            iconName.setText(b.getName());

        }
        else { //unit
            unitID = unitList.get(r.nextInt(unitList.size()));
            Unit u = Database.getUnit(unitID);
            int n1 = r.nextInt(4);
            gif.setImageResource(u.getNameElement().getMedia());
            icon.setImageResource(u.getNameElement().getImage());
            iconName.setText(u.getName());
            symbol.setImageResource(R.drawable.question);
            if (n1 == 0){   //asking a random attack value
                LinkedHashMap<String, LinkedHashMap<Integer, Double>> l = u.getBaseAttackValues();
                LinkedHashMap<Integer, Double> l1 = l.entrySet().iterator().next().getValue();
                List<Integer> lel = new ArrayList<>(l1.keySet());
                int type = lel.get(r.nextInt(lel.size()));
                askedValue = l1.get(type);
                String askedName = Database.getElement(Database.TYPE_LIST, type).getName();
                String q = String.format(getString(R.string.quiz_unit_attack_question), currentQuestion, numQuestions, askedName, u.getName());
                question.setText(q);
                correctionComment = String.format(getString(R.string.quiz_unit_attack_correction), u.getName(), (int)askedValue, askedName);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
            }
            else if (n1 == 1){ //asking a random armor value
                LinkedHashMap<String, LinkedHashMap<Integer, Double>> l = u.getBaseArmorValues();
                LinkedHashMap<Integer, Double> l1 = l.entrySet().iterator().next().getValue();
                List<Integer> lel = new ArrayList<>(l1.keySet());
                int type = lel.get(r.nextInt(lel.size()));
                askedValue = l1.get(type);
                String askedName = Database.getElement(Database.TYPE_LIST, type).getName();
                String q = String.format(getString(R.string.quiz_unit_armor_question), currentQuestion, numQuestions, askedName, u.getName());
                question.setText(q);
                correctionComment = String.format(getString(R.string.quiz_unit_armor_correction), u.getName(), (int)askedValue, askedName);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
            }
            else { //asking a random stat
                double range = u.getBaseStat(Database.RANGE);
                if (!Double.isNaN(range)) { // ranged unit
                    int n2 = r.nextInt(8);
                    prepareLayouts(u, n2);
                } else {  // melee unit;
                    int n2 = r.nextInt(7);
                    prepareLayouts(u, n2);
                }
            }
        }
    }

    private String getStatText(double d){
        if (Double.isNaN(d))return "-";
        else {
            BigDecimal b= new BigDecimal(d);
            b = b.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
            if (b.compareTo(BigDecimal.ZERO) == 0) return "0";
            else return b.toPlainString();
        }
    }

    private void prepareLayouts(Unit u, int n){
        switch (n){
            case 0:
                askedValue = u.getBaseStat(Database.HP);
                String q = String.format(getString(R.string.quiz_unit_hp_question), currentQuestion, numQuestions, u.getName());
                question.setText(q);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_hp_correction), u.getName(), getStatText(askedValue));
                break;
            case 1:
                askedValue = u.getBaseStat(Database.LOS);
                String q1 = String.format(getString(R.string.quiz_unit_los_question), currentQuestion, numQuestions, u.getName());
                question.setText(q1);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment  = String.format(getString(R.string.quiz_unit_los_correction), u.getName(), getStatText(askedValue));
                break;
            case 2:
                askedValue = u.getBaseStat(Database.TRAINING_TIME);;
                String q2 = String.format(getString(R.string.quiz_unit_training_question), currentQuestion, numQuestions, u.getName());
                question.setText(q2);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_training_correction), getStatText(askedValue), u.getName());
                break;
            case 3:
                String q3 = String.format(getString(R.string.quiz_unit_cost_question), currentQuestion, numQuestions, u.getName());
                question.setText(q3);
                doubleAnswerLayout.setVisibility(View.VISIBLE);
                singleAnswerLayout.setVisibility(View.GONE);
                HashMap<String, Integer> cost = u.getBaseCost();
                if (cost.size() == 1){
                    for(String res: cost.keySet()) {
                        doubleAnswerLayout2.setVisibility(View.GONE);
                        doubleAnswerDivider.setVisibility(View.GONE);
                        doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        askedValue = Double.valueOf(cost.get(res));
                        costQuestion = 1;
                        res1.setImageResource(Utils.getResourceIcon(res));
                        correctionComment = String.format(getString(R.string.quiz_unit_cost_correction), u.getName(), cost.get(res), getString(Utils.getResourceString(res)))+".";
                    }
                }
                else{
                    int r = 1;
                    doubleAnswerLayout2.setVisibility(View.VISIBLE);
                    doubleAnswerDivider.setVisibility(View.VISIBLE);
                    doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    comment.setText(getString(R.string.quiz_write_value));
                    costQuestion = 2;
                    for(String res: cost.keySet()) {
                        if(r == 1){
                            askedValue = Double.valueOf(cost.get(res));
                            res1.setImageResource(Utils.getResourceIcon(res));
                            correctionComment =  String.format(getString(R.string.quiz_unit_cost_correction), u.getName(), cost.get(res), getString(Utils.getResourceString(res)));
                        }
                        else{
                            askedValue2 = Double.valueOf(cost.get(res));
                            correctionComment +=" " +  String.format(getString(R.string.quiz_cost_correction2), cost.get(res), getString(Utils.getResourceString(res)));
                            res2.setImageResource(Utils.getResourceIcon(res));

                        }
                        ++r;
                    }
                }
                break;
            case 4:
                askedValue = u.getBaseStat(Database.ATTACK);
                String q4 = String.format(getString(R.string.quiz_unit_base_attack_question), currentQuestion, numQuestions, u.getName());
                question.setText(q4);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment  = String.format(getString(R.string.quiz_unit_base_attack_correction), u.getName(), getStatText(askedValue));
                break;
            case 5:
                askedValue = u.getBaseStat(Database.SPEED);
                String q5 = String.format(getString(R.string.quiz_unit_speed_question), currentQuestion, numQuestions, u.getName());
                question.setText(q5);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment  = String.format(getString(R.string.quiz_unit_speed_correction), u.getName(), getStatText(askedValue));
                break;
            case 6:
                askedValue = u.getBaseStat(Database.RELOAD_TIME);
                String q6 = String.format(getString(R.string.quiz_unit_reload_question), currentQuestion, numQuestions, u.getName());
                question.setText(q6);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment  = String.format(getString(R.string.quiz_unit_reload_correction), u.getName(), getStatText(askedValue));
                break;
            case 7:
                askedValue = u.getBaseStat(Database.RANGE);
                String q7 = String.format(getString(R.string.quiz_unit_range_question), currentQuestion, numQuestions, u.getName());
                question.setText(q7);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment  = String.format(getString(R.string.quiz_unit_range_correction), u.getName(), getStatText(askedValue));
                break;
        }
    }

    private void prepareLayouts(Building b, int n){
        switch (n){
            case 0:
                askedValue = b.getBaseStat(Database.HP);
                String q = String.format(getString(R.string.quiz_unit_hp_question), currentQuestion, numQuestions, b.getName());
                question.setText(q);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_hp_correction), b.getName(), getStatText(askedValue));
                break;
            case 1:
                askedValue = b.getBaseStat(Database.LOS);
                String q1 = String.format(getString(R.string.quiz_unit_los_question), currentQuestion, numQuestions, b.getName());
                question.setText(q1);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_los_correction), b.getName(), getStatText(askedValue));
                break;
            case 2:
                askedValue = b.getBaseStat(Database.TRAINING_TIME);
                String q2 = String.format(getString(R.string.quiz_building_time_question), currentQuestion, numQuestions, b.getName());
                question.setText(q2);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_building_time_correction), getStatText(askedValue), b.getName());
                break;
            case 3:
                String q3 = String.format(getString(R.string.quiz_unit_cost_question), currentQuestion, numQuestions, b.getName());
                question.setText(q3);
                doubleAnswerLayout.setVisibility(View.VISIBLE);
                singleAnswerLayout.setVisibility(View.GONE);
                HashMap<String, Integer> cost = b.getBaseCost();
                if (cost.size() == 1){
                    for(String res: cost.keySet()) {
                        doubleAnswerLayout2.setVisibility(View.GONE);
                        doubleAnswerDivider.setVisibility(View.GONE);
                        doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        askedValue = Double.valueOf(cost.get(res));
                        costQuestion = 1;
                        res1.setImageResource(Utils.getResourceIcon(res));
                        correctionComment = String.format(getString(R.string.quiz_unit_cost_correction), b.getName(), cost.get(res), getString(Utils.getResourceString(res)))+".";
                    }
                }
                else{
                    int r = 1;
                    doubleAnswerLayout2.setVisibility(View.VISIBLE);
                    doubleAnswerDivider.setVisibility(View.VISIBLE);
                    doubleAnswer1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    comment.setText(getString(R.string.quiz_write_value));
                    costQuestion = 2;
                    for(String res: cost.keySet()) {
                        if(r == 1){
                            askedValue = Double.valueOf(cost.get(res));
                            res1.setImageResource(Utils.getResourceIcon(res));
                            correctionComment = String.format(getString(R.string.quiz_unit_cost_correction), b.getName(), cost.get(res), getString(Utils.getResourceString(res)));
                        }
                        else{
                            askedValue2 = Double.valueOf(cost.get(res));
                            correctionComment +=" " +  String.format(getString(R.string.quiz_cost_correction2), cost.get(res), getString(Utils.getResourceString(res)));
                            res2.setImageResource(Utils.getResourceIcon(res));

                        }
                        ++r;
                    }
                }
                break;
            case 4:
                askedValue = b.getBaseStat(Database.ATTACK);
                String q4 = String.format(getString(R.string.quiz_unit_base_attack_question), currentQuestion, numQuestions, b.getName());
                question.setText(q4);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_base_attack_correction), b.getName(), getStatText(askedValue));
                break;
            case 5:
                askedValue = b.getBaseStat(Database.RANGE);
                String q5 = String.format(getString(R.string.quiz_unit_range_question), currentQuestion, numQuestions, b.getName());
                question.setText(q5);
                doubleAnswerLayout.setVisibility(View.GONE);
                singleAnswerLayout.setVisibility(View.VISIBLE);
                correctionComment = String.format(getString(R.string.quiz_unit_range_correction), b.getName(), getStatText(askedValue));
                break;
        }
    }

}
