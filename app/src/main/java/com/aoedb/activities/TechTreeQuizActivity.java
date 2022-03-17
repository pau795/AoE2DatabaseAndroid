package com.aoedb.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.aoedb.R;
import com.aoedb.data.Building;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class TechTreeQuizActivity extends QuizActivity {

    int numCivs;

    LinearLayout yes, no;
    AppCompatCheckBox yCheckBox, nCheckBox;

    boolean correctAnswer;

    List<EntityElement> civList;
    List<Integer> unitQuestions, techQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_tech_tree_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tech_tree_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.red_background));
        initializeValues();
        newRound();
    }

    @Override
    public int getQuiz(){
        return 1;
    }

    @Override
    protected void initializeValues(){
        super.initializeValues();
        civList = Database.getList(Database.CIVILIZATION_LIST);
        numCivs=civList.size();
        yCheckBox = findViewById(R.id.quiz_yes_checkbox);
        nCheckBox = findViewById(R.id.quiz_no_checkbox);
        yes = findViewById(R.id.quiz_yes);
        no = findViewById(R.id.quiz_no);
        correctAnswer = false;

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yCheckBox.isChecked()) yCheckBox.setChecked(true);
                if (nCheckBox.isChecked()) nCheckBox.setChecked(false);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nCheckBox.isChecked()) nCheckBox.setChecked(true);
                if (yCheckBox.isChecked()) yCheckBox.setChecked(false);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okButton.getText().equals(getString(R.string.ok))) {
                    if (!yCheckBox.isChecked() && !nCheckBox.isChecked())
                        comment.setText(getString(R.string.quiz_select_answer));
                    else {
                        if (yCheckBox.isChecked() == correctAnswer) correctAnswer();
                        else wrongAnswer();
                        yes.setClickable(false);
                        no.setClickable(false);

                    }
                }
                else{
                    ++currentQuestion;
                    if (currentQuestion <= numQuestions){
                        yes.setClickable(true);
                        no.setClickable(true);
                        newRound();
                    }
                    else showResults();
                }
            }
        });
        LinkedHashMap<String, List<Integer>> a = Database.getTechTreeQuizQuestions();
        unitQuestions = a.get("Units");
        techQuestions = a.get("Techs");
    }

    public void newRound(){
        Random r = new Random();
        int c =r.nextInt(numCivs);
        EntityElement cle = civList.get(c);
        String civName = cle.getName();
        int i = r.nextInt(unitQuestions.size()+ techQuestions.size());
        if (i < unitQuestions.size()){
            Unit u = Database.getUnit(unitQuestions.get(i));
            String q = String.format(getString(R.string.quiz_tech_tree_unit_question),currentQuestion,numQuestions, civName, u.getName());
            question.setText(q);
            icon.setImageResource(u.getNameElement().getImage());
            iconName.setText(u.getName());
            gif.setImageResource(u.getNameElement().getMedia());
            symbol.setImageResource(R.drawable.question);
            yes.setClickable(true);
            no.setClickable(true);
            yCheckBox.setChecked(false);
            nCheckBox.setChecked(false);
            comment.setText("");
            okButton.setText(getString(R.string.ok));
            correctAnswer = Database.getUnit(unitQuestions.get(i)).isAvailableTo(c + 1);
        }
        else {
            i -= unitQuestions.size();
            Technology t = Database.getTechnology(techQuestions.get(i));
            String q = String.format(getString(R.string.quiz_tech_tree_tech_question),currentQuestion,numQuestions, civName, t.getName());
            question.setText(q);
            icon.setImageResource(t.getNameElement().getImage());
            iconName.setText(t.getName());
            Building b = Database.getBuilding(t.getCreatorElement().getId());
            gif.setImageResource(b.getNameElement().getMedia());
            symbol.setImageResource(R.drawable.question);
            yCheckBox.setChecked(false);
            nCheckBox.setChecked(false);
            comment.setText("");
            okButton.setText(getString(R.string.ok));
            correctAnswer = Database.getTechnology(techQuestions.get(i)).isAvailableTo(c + 1);
        }
    }
}
