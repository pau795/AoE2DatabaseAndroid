package com.aoedb.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.data.Score;
import com.aoedb.data.ScoreList;
import com.aoedb.database.Database;


public class ScoreActivity extends DrawerActivity{

    Context ctx;

    LinearLayout techTreeLayout, techTreeContent;
    LinearLayout civLayout, civContent;
    LinearLayout unitStatsLayout, unitStatsContent;
    LinearLayout unitRecognitionLayout, unitRecognitionContent;
    LinearLayout techStatsLayout, techStatsContent;
    LinearLayout uniqueTechsLayout, uniqueTechsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_score);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_score, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.orange_background));
        initialize_layouts();
        loadScores();
    }

    private void loadScores(){
        ScoreList sl = Database.readScore(this);

        Score s = sl.getScore(1);
        TextView techTreeStarted = findViewById(R.id.score_tech_tree_started);
        TextView techTreeFinished = findViewById(R.id.score_tech_tree_finished);
        TextView techTreeAnswered = findViewById(R.id.score_tech_tree_answered);
        TextView techTreeCorrect =  findViewById(R.id.score_tech_tree_total_correct);
        TextView techTreeWrong = findViewById(R.id.score_tech_tree_total_wrong);
        TextView techTreeScore10 = findViewById(R.id.score_tech_tree_score10);
        TextView techTreeScore20 = findViewById(R.id.score_tech_tree_score20);
        TextView techTreeScore30 = findViewById(R.id.score_tech_tree_score30);
        TextView techTreeAnswers10 = findViewById(R.id.score_tech_tree_answers10);
        TextView techTreeAnswers20 = findViewById(R.id.score_tech_tree_answers20);
        TextView techTreeAnswers30 = findViewById(R.id.score_tech_tree_answers30);
        TextView techTreeStreak10 = findViewById(R.id.score_tech_tree_streak10);
        TextView techTreeStreak20 = findViewById(R.id.score_tech_tree_streak20);
        TextView techTreeStreak30 = findViewById(R.id.score_tech_tree_streak30);
        TextView techTreeMultiplier10 = findViewById(R.id.score_tech_tree_multiplier10);
        TextView techTreeMultiplier20 = findViewById(R.id.score_tech_tree_multiplier20);
        TextView techTreeMultiplier30 = findViewById(R.id.score_tech_tree_multiplier30);
        techTreeStarted.setText(String.valueOf(s.getNumStarted()));
        techTreeFinished.setText(String.valueOf(s.getNumFinished()));
        techTreeAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        techTreeCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        techTreeWrong.setText(String.valueOf(s.getWrongAnswers()));
        techTreeScore10.setText(String.valueOf(s.getScore(10)));
        techTreeScore20.setText(String.valueOf(s.getScore(20)));
        techTreeScore30.setText(String.valueOf(s.getScore(30)));
        techTreeAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        techTreeAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        techTreeAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        techTreeStreak10.setText(String.valueOf(s.getStreak(10)));
        techTreeStreak20.setText(String.valueOf(s.getStreak(20)));
        techTreeStreak30.setText(String.valueOf(s.getStreak(30)));
        techTreeMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        techTreeMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        techTreeMultiplier30.setText(String.valueOf(s.getMultiplier(30)));


        s = sl.getScore(2);
        TextView civilizationStarted = findViewById(R.id.score_civilization_started);
        TextView civilizationFinished = findViewById(R.id.score_civilization_finished);
        TextView civilizationAnswered = findViewById(R.id.score_civilization_answered);
        TextView civilizationCorrect =  findViewById(R.id.score_civilization_total_correct);
        TextView civilizationWrong = findViewById(R.id.score_civilization_total_wrong);
        TextView civilizationScore10 = findViewById(R.id.score_civilization_score10);
        TextView civilizationScore20 = findViewById(R.id.score_civilization_score20);
        TextView civilizationScore30 = findViewById(R.id.score_civilization_score30);
        TextView civilizationAnswers10 = findViewById(R.id.score_civilization_answers10);
        TextView civilizationAnswers20 = findViewById(R.id.score_civilization_answers20);
        TextView civilizationAnswers30 = findViewById(R.id.score_civilization_answers30);
        TextView civilizationStreak10 = findViewById(R.id.score_civilization_streak10);
        TextView civilizationStreak20 = findViewById(R.id.score_civilization_streak20);
        TextView civilizationStreak30 = findViewById(R.id.score_civilization_streak30);
        TextView civilizationMultiplier10 = findViewById(R.id.score_civilization_multiplier10);
        TextView civilizationMultiplier20 = findViewById(R.id.score_civilization_multiplier20);
        TextView civilizationMultiplier30 = findViewById(R.id.score_civilization_multiplier30);
        civilizationStarted.setText(String.valueOf(s.getNumStarted()));
        civilizationFinished.setText(String.valueOf(s.getNumFinished()));
        civilizationAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        civilizationCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        civilizationWrong.setText(String.valueOf(s.getWrongAnswers()));
        civilizationScore10.setText(String.valueOf(s.getScore(10)));
        civilizationScore20.setText(String.valueOf(s.getScore(20)));
        civilizationScore30.setText(String.valueOf(s.getScore(30)));
        civilizationAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        civilizationAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        civilizationAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        civilizationStreak10.setText(String.valueOf(s.getStreak(10)));
        civilizationStreak20.setText(String.valueOf(s.getStreak(20)));
        civilizationStreak30.setText(String.valueOf(s.getStreak(30)));
        civilizationMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        civilizationMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        civilizationMultiplier30.setText(String.valueOf(s.getMultiplier(30)));

        s = sl.getScore(3);
        TextView unitStatsStarted = findViewById(R.id.score_unit_stats_started);
        TextView unitStatsFinished = findViewById(R.id.score_unit_stats_finished);
        TextView unitStatsAnswered = findViewById(R.id.score_unit_stats_answered);
        TextView unitStatsCorrect =  findViewById(R.id.score_unit_stats_total_correct);
        TextView unitStatsWrong = findViewById(R.id.score_unit_stats_total_wrong);
        TextView unitStatsScore10 = findViewById(R.id.score_unit_stats_score10);
        TextView unitStatsScore20 = findViewById(R.id.score_unit_stats_score20);
        TextView unitStatsScore30 = findViewById(R.id.score_unit_stats_score30);
        TextView unitStatsAnswers10 = findViewById(R.id.score_unit_stats_answers10);
        TextView unitStatsAnswers20 = findViewById(R.id.score_unit_stats_answers20);
        TextView unitStatsAnswers30 = findViewById(R.id.score_unit_stats_answers30);
        TextView unitStatsStreak10 = findViewById(R.id.score_unit_stats_streak10);
        TextView unitStatsStreak20 = findViewById(R.id.score_unit_stats_streak20);
        TextView unitStatsStreak30 = findViewById(R.id.score_unit_stats_streak30);
        TextView unitStatsMultiplier10 = findViewById(R.id.score_unit_stats_multiplier10);
        TextView unitStatsMultiplier20 = findViewById(R.id.score_unit_stats_multiplier20);
        TextView unitStatsMultiplier30 = findViewById(R.id.score_unit_stats_multiplier30);
        unitStatsStarted.setText(String.valueOf(s.getNumStarted()));
        unitStatsFinished.setText(String.valueOf(s.getNumFinished()));
        unitStatsAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        unitStatsCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        unitStatsWrong.setText(String.valueOf(s.getWrongAnswers()));
        unitStatsScore10.setText(String.valueOf(s.getScore(10)));
        unitStatsScore20.setText(String.valueOf(s.getScore(20)));
        unitStatsScore30.setText(String.valueOf(s.getScore(30)));
        unitStatsAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        unitStatsAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        unitStatsAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        unitStatsStreak10.setText(String.valueOf(s.getStreak(10)));
        unitStatsStreak20.setText(String.valueOf(s.getStreak(20)));
        unitStatsStreak30.setText(String.valueOf(s.getStreak(30)));
        unitStatsMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        unitStatsMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        unitStatsMultiplier30.setText(String.valueOf(s.getMultiplier(30)));


        s = sl.getScore(4);
        TextView unitRecognitionStarted = findViewById(R.id.score_unit_recognition_started);
        TextView unitRecognitionFinished = findViewById(R.id.score_unit_recognition_finished);
        TextView unitRecognitionAnswered = findViewById(R.id.score_unit_recognition_answered);
        TextView unitRecognitionCorrect =  findViewById(R.id.score_unit_recognition_total_correct);
        TextView unitRecognitionWrong = findViewById(R.id.score_unit_recognition_total_wrong);
        TextView unitRecognitionScore10 = findViewById(R.id.score_unit_recognition_score10);
        TextView unitRecognitionScore20 = findViewById(R.id.score_unit_recognition_score20);
        TextView unitRecognitionScore30 = findViewById(R.id.score_unit_recognition_score30);
        TextView unitRecognitionAnswers10 = findViewById(R.id.score_unit_recognition_answers10);
        TextView unitRecognitionAnswers20 = findViewById(R.id.score_unit_recognition_answers20);
        TextView unitRecognitionAnswers30 = findViewById(R.id.score_unit_recognition_answers30);
        TextView unitRecognitionStreak10 = findViewById(R.id.score_unit_recognition_streak10);
        TextView unitRecognitionStreak20 = findViewById(R.id.score_unit_recognition_streak20);
        TextView unitRecognitionStreak30 = findViewById(R.id.score_unit_recognition_streak30);
        TextView unitRecognitionMultiplier10 = findViewById(R.id.score_unit_recognition_multiplier10);
        TextView unitRecognitionMultiplier20 = findViewById(R.id.score_unit_recognition_multiplier20);
        TextView unitRecognitionMultiplier30 = findViewById(R.id.score_unit_recognition_multiplier30);
        unitRecognitionStarted.setText(String.valueOf(s.getNumStarted()));
        unitRecognitionFinished.setText(String.valueOf(s.getNumFinished()));
        unitRecognitionAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        unitRecognitionCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        unitRecognitionWrong.setText(String.valueOf(s.getWrongAnswers()));
        unitRecognitionScore10.setText(String.valueOf(s.getScore(10)));
        unitRecognitionScore20.setText(String.valueOf(s.getScore(20)));
        unitRecognitionScore30.setText(String.valueOf(s.getScore(30)));
        unitRecognitionAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        unitRecognitionAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        unitRecognitionAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        unitRecognitionStreak10.setText(String.valueOf(s.getStreak(10)));
        unitRecognitionStreak20.setText(String.valueOf(s.getStreak(20)));
        unitRecognitionStreak30.setText(String.valueOf(s.getStreak(30)));
        unitRecognitionMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        unitRecognitionMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        unitRecognitionMultiplier30.setText(String.valueOf(s.getMultiplier(30)));

        s = sl.getScore(5);
        TextView technologyStatsStarted = findViewById(R.id.score_technology_stats_started);
        TextView technologyStatsFinished = findViewById(R.id.score_technology_stats_finished);
        TextView technologyStatsAnswered = findViewById(R.id.score_technology_stats_answered);
        TextView technologyStatsCorrect =  findViewById(R.id.score_technology_stats_total_correct);
        TextView technologyStatsWrong = findViewById(R.id.score_technology_stats_total_wrong);
        TextView technologyStatsScore10 = findViewById(R.id.score_technology_stats_score10);
        TextView technologyStatsScore20 = findViewById(R.id.score_technology_stats_score20);
        TextView technologyStatsScore30 = findViewById(R.id.score_technology_stats_score30);
        TextView technologyStatsAnswers10 = findViewById(R.id.score_technology_stats_answers10);
        TextView technologyStatsAnswers20 = findViewById(R.id.score_technology_stats_answers20);
        TextView technologyStatsAnswers30 = findViewById(R.id.score_technology_stats_answers30);
        TextView technologyStatsStreak10 = findViewById(R.id.score_technology_stats_streak10);
        TextView technologyStatsStreak20 = findViewById(R.id.score_technology_stats_streak20);
        TextView technologyStatsStreak30 = findViewById(R.id.score_technology_stats_streak30);
        TextView technologyStatsMultiplier10 = findViewById(R.id.score_technology_stats_multiplier10);
        TextView technologyStatsMultiplier20 = findViewById(R.id.score_technology_stats_multiplier20);
        TextView technologyStatsMultiplier30 = findViewById(R.id.score_technology_stats_multiplier30);
        technologyStatsStarted.setText(String.valueOf(s.getNumStarted()));
        technologyStatsFinished.setText(String.valueOf(s.getNumFinished()));
        technologyStatsAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        technologyStatsCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        technologyStatsWrong.setText(String.valueOf(s.getWrongAnswers()));
        technologyStatsScore10.setText(String.valueOf(s.getScore(10)));
        technologyStatsScore20.setText(String.valueOf(s.getScore(20)));
        technologyStatsScore30.setText(String.valueOf(s.getScore(30)));
        technologyStatsAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        technologyStatsAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        technologyStatsAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        technologyStatsStreak10.setText(String.valueOf(s.getStreak(10)));
        technologyStatsStreak20.setText(String.valueOf(s.getStreak(20)));
        technologyStatsStreak30.setText(String.valueOf(s.getStreak(30)));
        technologyStatsMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        technologyStatsMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        technologyStatsMultiplier30.setText(String.valueOf(s.getMultiplier(30)));

        s = sl.getScore(6);
        TextView uniqueTechStarted = findViewById(R.id.score_unique_tech_started);
        TextView uniqueTechFinished = findViewById(R.id.score_unique_tech_finished);
        TextView uniqueTechAnswered = findViewById(R.id.score_unique_tech_answered);
        TextView uniqueTechCorrect =  findViewById(R.id.score_unique_tech_total_correct);
        TextView uniqueTechWrong = findViewById(R.id.score_unique_tech_total_wrong);
        TextView uniqueTechScore10 = findViewById(R.id.score_unique_tech_score10);
        TextView uniqueTechScore20 = findViewById(R.id.score_unique_tech_score20);
        TextView uniqueTechScore30 = findViewById(R.id.score_unique_tech_score30);
        TextView uniqueTechAnswers10 = findViewById(R.id.score_unique_tech_answers10);
        TextView uniqueTechAnswers20 = findViewById(R.id.score_unique_tech_answers20);
        TextView uniqueTechAnswers30 = findViewById(R.id.score_unique_tech_answers30);
        TextView uniqueTechStreak10 = findViewById(R.id.score_unique_tech_streak10);
        TextView uniqueTechStreak20 = findViewById(R.id.score_unique_tech_streak20);
        TextView uniqueTechStreak30 = findViewById(R.id.score_unique_tech_streak30);
        TextView uniqueTechMultiplier10 = findViewById(R.id.score_unique_tech_multiplier10);
        TextView uniqueTechMultiplier20 = findViewById(R.id.score_unique_tech_multiplier20);
        TextView uniqueTechMultiplier30 = findViewById(R.id.score_unique_tech_multiplier30);
        uniqueTechStarted.setText(String.valueOf(s.getNumStarted()));
        uniqueTechFinished.setText(String.valueOf(s.getNumFinished()));
        uniqueTechAnswered.setText(String.valueOf(s.getAnsweredQuestions()));
        uniqueTechCorrect.setText(String.valueOf(s.getCorrectAnswers()));
        uniqueTechWrong.setText(String.valueOf(s.getWrongAnswers()));
        uniqueTechScore10.setText(String.valueOf(s.getScore(10)));
        uniqueTechScore20.setText(String.valueOf(s.getScore(20)));
        uniqueTechScore30.setText(String.valueOf(s.getScore(30)));
        uniqueTechAnswers10.setText(String.valueOf(s.writeCorrectAnswers(10)));
        uniqueTechAnswers20.setText(String.valueOf(s.writeCorrectAnswers(20)));
        uniqueTechAnswers30.setText(String.valueOf(s.writeCorrectAnswers(30)));
        uniqueTechStreak10.setText(String.valueOf(s.getStreak(10)));
        uniqueTechStreak20.setText(String.valueOf(s.getStreak(20)));
        uniqueTechStreak30.setText(String.valueOf(s.getStreak(30)));
        uniqueTechMultiplier10.setText(String.valueOf(s.getMultiplier(10)));
        uniqueTechMultiplier20.setText(String.valueOf(s.getMultiplier(20)));
        uniqueTechMultiplier30.setText(String.valueOf(s.getMultiplier(30)));
    }

    private void initialize_layouts(){
        ctx = this;

        techTreeLayout = findViewById(R.id.score_tech_tree_layout);
        techTreeContent = findViewById(R.id.score_tech_tree_content);
        civLayout = findViewById(R.id.score_civilization_layout);
        civContent = findViewById(R.id.score_civilization_content);
        unitStatsLayout = findViewById(R.id.score_unit_stats_layout);
        unitStatsContent = findViewById(R.id.score_unit_stats_content);
        unitRecognitionContent = findViewById(R.id.score_unit_recognition_content);
        unitRecognitionLayout = findViewById(R.id.score_unit_recognition_layout);
        techStatsLayout = findViewById(R.id.score_technology_stats_layout);
        techStatsContent = findViewById(R.id.score_technology_stats_content);
        uniqueTechsLayout = findViewById(R.id.score_unique_tech_layout);
        uniqueTechsContent = findViewById(R.id.score_unique_tech_content);

        techTreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (techTreeContent.getVisibility() == View.GONE) techTreeContent.setVisibility(View.VISIBLE);
                else techTreeContent.setVisibility(View.GONE);
            }
        });
        civLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (civContent.getVisibility() == View.GONE) civContent.setVisibility(View.VISIBLE);
                else civContent.setVisibility(View.GONE);
            }
        });
        unitStatsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unitStatsContent.getVisibility() == View.GONE) unitStatsContent.setVisibility(View.VISIBLE);
                else unitStatsContent.setVisibility(View.GONE);
            }
        });
        unitRecognitionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unitRecognitionContent.getVisibility() == View.GONE) unitRecognitionContent.setVisibility(View.VISIBLE);
                else unitRecognitionContent.setVisibility(View.GONE);
            }
        });
        techStatsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (techStatsContent.getVisibility() == View.GONE) techStatsContent.setVisibility(View.VISIBLE);
                else techStatsContent.setVisibility(View.GONE);
            }
        });
        uniqueTechsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uniqueTechsContent.getVisibility() == View.GONE) uniqueTechsContent.setVisibility(View.VISIBLE);
                else uniqueTechsContent.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset){
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.quiz_restart_scores))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.quiz_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ScoreList s =  new ScoreList();
                            Database.writeScore(s, ctx);
                            loadScores();
                        }
                    })
                    .setNegativeButton(getString(R.string.quiz_no), null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}