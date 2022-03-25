package com.aoedb.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aoedb.R;
import com.aoedb.data.Score;
import com.aoedb.data.ScoreList;
import com.aoedb.database.Database;

import pl.droidsonroids.gif.GifImageView;

public abstract class QuizActivity extends DrawerActivity {

    int currentQuestion, numQuestions, correctAnswers, score, streak, maxStreak;
    float multiplier, maxMultiplier;
    String correctionComment;
    ScoreList sl;
    Score s;
    Context ctx;

    GifImageView gif;
    ImageView symbol, icon;
    TextView question, comment, multiplierT, scoreT, correctAnswersT, okButton, iconName;
    TextView fScore, fCorrect, fStreak, fMultiplier;
    LinearLayout ok, quiz, quizFinish, finishButton;

    public abstract void newRound();

    public abstract int getQuiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initializeValues(){
        ctx=this;
        currentQuestion = 1;
        correctionComment = "";
        numQuestions = getIntent().getIntExtra("questions", 10);
        correctAnswers = 0;
        multiplier = 1;
        score = 0;
        streak = 0;
        maxStreak = 0;
        maxMultiplier = 1;
        sl = Database.readScore(this);
        s = sl.getScore(getQuiz());
        s.setNumStarted(s.getNumStarted()+1);
        iconName = findViewById(R.id.quiz_icon_name);
        icon = findViewById(R.id.quiz_icon);
        gif = findViewById(R.id.quiz_gif);
        question = findViewById(R.id.quiz_question);
        comment = findViewById(R.id.quiz_comment);
        correctAnswersT = findViewById(R.id.quiz_correct_answers);
        multiplierT =  findViewById(R.id.quiz_multiplier);
        scoreT = findViewById(R.id.quiz_score);
        symbol = findViewById(R.id.quiz_symbol);
        okButton = findViewById(R.id.quiz_ok);
        correctAnswersT.setText("0 / " + numQuestions);
        multiplierT.setText(String.valueOf(multiplier));
        scoreT.setText(String.valueOf(score));
        ok = findViewById(R.id.quiz_accept);
        quiz = findViewById(R.id.quiz);
        quizFinish = findViewById(R.id.quiz_finish);
        finishButton = findViewById(R.id.quiz_finish_button);
        fScore = findViewById(R.id.quiz_finish_score);
        fCorrect = findViewById(R.id.quiz_finish_correct);
        fStreak = findViewById(R.id.quiz_finish_streak);
        fMultiplier = findViewById(R.id.quiz_finish_multiplier);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });

    }


    protected void correctAnswer(){
        comment.setText(String.format(getString(R.string.quiz_correct_answer_points), ((Float)(10*multiplier)).intValue()));
        symbol.setImageResource(R.drawable.correct);
        score += 10*multiplier;
        ++streak;
        if (streak > maxStreak) maxStreak = streak;
        multiplier += 0.5;
        if (multiplier > maxMultiplier) maxMultiplier = multiplier;
        ++correctAnswers;
        correctAnswersT.setText(correctAnswers +" / " +(numQuestions));
        multiplierT.setText(String.valueOf(multiplier));
        scoreT.setText(String.valueOf(score));
        okButton.setText(getString(R.string.quiz_next));
        s.setAnsweredQuestions(s.getAnsweredQuestions()+1);
        s.setCorrectAnswers(s.getCorrectAnswers()+1);
    }

    protected void wrongAnswer(){
        comment.setText(getString(R.string.quiz_wrong_answer_android) +". "+ correctionComment);
        symbol.setImageResource(R.drawable.incorrect);
        streak = 0;
        multiplier = 1;
        correctAnswersT.setText(correctAnswers +" / " +(numQuestions));
        multiplierT.setText(String.valueOf(multiplier));
        scoreT.setText(String.valueOf(score));
        okButton.setText(getString(R.string.quiz_next));
        s.setAnsweredQuestions(s.getAnsweredQuestions()+1);
        s.setWrongAnswers(s.getWrongAnswers()+1);
    }

    private void end(){
        Database.writeScore(sl, ctx);
        Intent i =  new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        i =  new Intent(this, NewQuizActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.quiz_warning_question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.quiz_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Database.writeScore(sl, ctx);
                        back();
                    }
                })
                .setNegativeButton(getString(R.string.quiz_no), null)
                .show();

    }

    private void back (){
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int i = item.getItemId();
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.quiz_warning_question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.quiz_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nav(i);
                    }
                })
                .setNegativeButton(getString(R.string.quiz_no), null)
                .show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showResults(){
        s.setNumFinished(s.getNumFinished()+1);
        if (score > s.getScore(numQuestions)) s.setScore(numQuestions,score);
        if (correctAnswers > s.getCorrectAnswers(numQuestions)) s.setCorrectAnswers(numQuestions, correctAnswers);
        if (maxStreak > s.getStreak(numQuestions)) s.setStreak(numQuestions, maxStreak);
        if (maxMultiplier > s.getMultiplier(numQuestions)) s.setMultiplier(numQuestions, maxMultiplier);

        fScore.setText(String.valueOf(score));
        fCorrect.setText(correctAnswers + " / " + numQuestions);
        fStreak.setText(String.valueOf(maxStreak));
        fMultiplier.setText(String.valueOf(maxMultiplier));
        quiz.setVisibility(View.GONE);
        quizFinish.setVisibility(View.VISIBLE);
    }

    private void nav(int id){
        Database.writeScore(sl, ctx);
        if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_units) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, UnitListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_buildings) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, BuildingListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_techs) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, TechnologyListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_civs) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, CivilizationListActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_techtree) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, LoadingTechTreeActivity.class);
            i.putExtra(Database.CIV, 1);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_game_mechanics) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, GameMechanicsActivity.class);
            startActivity(i);
            this.finish();
        }else if (id == R.id.nav_tools) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, ToolsActivity.class);
            startActivity(i);
            this.finish();
        }else if (id == R.id.nav_misc) {
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                i = new Intent(this, MiscActivity.class);
                startActivity(i);
                this.finish();
        } else if (id == R.id.nav_quiz) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, NewQuizActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_scores) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            i = new Intent(this, ScoreActivity.class);
            startActivity(i);
            this.finish();
        }
        else if (id == R.id.nav_web) {
            Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.web_app_url)));
            startActivity(i);
            this.finish();
        }
    }
}
