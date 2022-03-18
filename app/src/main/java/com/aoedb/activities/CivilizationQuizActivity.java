package com.aoedb.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.aoedb.R;
import com.aoedb.data.Bonus;
import com.aoedb.data.Civilization;
import com.aoedb.adapters.TextFilterAdapter;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CivilizationQuizActivity extends QuizActivity {
    int bonusCount, unitCount, civCount, civ;

    List<String> civNames;
    HashMap<String, Integer> civId;
    HashMap<Integer, List<String>> civBonuses;
    HashMap<Integer, Integer> civThemes;
    HashMap<Integer, Integer> civIcons;
    HashMap<Integer, List<Integer>> civUnits;

    ArrayList<Integer> questionSample;

    AppCompatAutoCompleteTextView civSelector;
    ImageView playIcon;
    TextView playIconText;
    LinearLayout playIconLayout;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_civilization_quiz);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_civilization_quiz, null, false);
        setActivityView(contentView);
        setAppBarColor(getColor(R.color.red_background));
        initializeValues();
        newRound();
    }

    @Override
    public int getQuiz(){
        return 2;
    }


    private void getQuestionSample(){
        boolean bonuses = getIntent().getBooleanExtra("bonuses", true);
        boolean units = getIntent().getBooleanExtra("units", true);
        boolean themes = getIntent().getBooleanExtra("themes", true);
        boolean emblems = getIntent().getBooleanExtra("emblems", true);
        if (bonuses) questionSample.add(0);
        if (units) questionSample.add(1);
        if (themes) questionSample.add(2);
        if (emblems) questionSample.add(3);
    }
    @Override
    protected void initializeValues(){
        super.initializeValues();
        bonusCount = 0;
        civCount = 0;
        unitCount = 0;
        civ = 0;

        civId = new HashMap<>();
        civBonuses = new HashMap<>();
        civThemes =  new HashMap<>();
        civUnits = new HashMap<>();
        civIcons = new HashMap<>();
        civNames = new ArrayList<>();
        questionSample = new ArrayList<>();
        getQuestionSample();
        getCivInfo();
        civSelector = findViewById(R.id.quiz_spinner);
        playIcon = findViewById(R.id.quiz_play_icon);
        playIconText = findViewById(R.id.quiz_play_text);
        playIconLayout =  findViewById(R.id.quiz_play_layout);
        TextFilterAdapter adapter = new TextFilterAdapter(this, R.layout.autocomplete_text_layout, new ArrayList<>(civNames));
        civSelector.setThreshold(1);
        civSelector.setAdapter(adapter);
        civSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    String s = civSelector.getText().toString();
                    if (civId.containsKey(s)) {
                        if (civId.get(s) == civ) correctAnswer();
                        else wrongAnswer();
                    }
                    else comment.setText(getString(R.string.quiz_please_select_civ));
                }
                else{
                    ++currentQuestion;
                    if (currentQuestion <= numQuestions){
                        civSelector.setText("");
                        newRound();
                    }
                    else showResults();
                }
            }
        });
    }

    private void getCivInfo(){
        HashMap<Integer,String> civRelation = Database.getCivNameMap();
        for (int i: civRelation.keySet()){
            civNames.add(civRelation.get(i));
            civId.put(civRelation.get(i), i);
            Civilization c = Database.getCivilization(i);
            int theme = c.getCivThemeID();
            int icon1 = c.getNameElement().getImage();
            civThemes.put(i, theme);
            civIcons.put(i,icon1);
            ++civCount;
            if (i == 34 || i == 39) civUnits.put(i, new ArrayList<>(Collections.singletonList(c.getUniqueUnit()))); //discard winged hussar
            else civUnits.put(i, c.getUniqueUnitList());
            unitCount += civUnits.get(i).size();
            ArrayList<String> a = new ArrayList<>();
            for(int b: c.getBonusList()){
                Bonus bonus = Database.getBonus(b);
                a.add(bonus.getTechTreeDescription());
                ++bonusCount;
            }
            Bonus bonus = Database.getBonus(c.getTeamBonusId());
            a.add(bonus.getTechTreeDescription());
            ++bonusCount;
            civBonuses.put(i, a);
        }
    }

    public void newRound(){
        Random r = new Random();
        civ = r.nextInt(civCount)+1;
        int pos = r.nextInt(questionSample.size());
        int n = questionSample.get(pos);
        comment.setText(getString(R.string.quiz_select_civ));
        correctionComment = String.format(getString(R.string.quiz_correction_civ), civNames.get(civ-1));
        okButton.setText(getString(R.string.ok));
        switch (n){
            case 0:
                List<String> bonuses = civBonuses.get(civ);
                int i = r.nextInt(bonuses.size());
                String bonus = bonuses.get(i);
                String q = String.format(getString(R.string.quiz_civ_bonus_question), currentQuestion, numQuestions, bonus);
                icon.setImageResource(R.drawable.medal1);
                icon.setBackground(null);
                iconName.setText(getString(R.string.quiz_civ_bonus));
                question.setText(q);
                symbol.setImageResource(R.drawable.question);
                playIconLayout.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
                gif.setImageResource(R.drawable.quiz);
                break;
            case 1:
                List<Integer> units = civUnits.get(civ);
                int i1 =  r.nextInt(units.size());
                int unitID = units.get(i1);
                Unit u = Database.getUnit(unitID);
                playIconLayout.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
                icon.setImageResource(u.getNameElement().getImage());
                icon.setBackgroundColor(getColor(R.color.red_border));
                iconName.setText(u.getName());
                gif.setImageResource(u.getNameElement().getMedia());
                String q1 = String.format(getString(R.string.quiz_civ_unique_unit_question), currentQuestion, numQuestions, u.getDescriptor().getNominative());
                question.setText(q1);
                symbol.setImageResource(R.drawable.question);
                break;
            case 2:
                String q2 = String.format(getString(R.string.quiz_civ_theme_question), currentQuestion, numQuestions);
                question.setText(q2);
                symbol.setImageResource(R.drawable.question);
                gif.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.trumpet);
                icon.setBackground(null);
                iconName.setText(getString(R.string.quiz_civ_theme));
                playIcon.setImageResource(R.drawable.playicon_red);
                playIconLayout.setVisibility(View.VISIBLE);
                playIconText.setText(getString(R.string.quiz_click_play));
                mp = MediaPlayer.create(this, civThemes.get(civ));
                playIcon.setOnClickListener(new View.OnClickListener(){

                    public void onClick(View v) {
                        ImageView i = (ImageView) v;
                        if (!mp.isPlaying())  {
                            mp.start();
                            i.setImageResource(R.drawable.stopicon_red);
                            playIconText.setText(getString(R.string.quiz_click_stop));
                        }
                        else {
                            mp.pause();
                            mp.seekTo(0);
                            i.setImageResource(R.drawable.playicon_red);
                            playIconText.setText(getString(R.string.quiz_click_play));
                        }
                    }
                });

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        ImageView play = findViewById(R.id.quiz_play_icon);
                        playIconText.setText(getString(R.string.quiz_click_play));
                        play.setImageResource(R.drawable.playicon_red);
                    }
                });
                break;
            case 3:
                String q3 = String.format(getString(R.string.quiz_civ_emblem_question), currentQuestion, numQuestions);
                icon.setImageResource(R.drawable.shield1);
                icon.setBackground(null);
                iconName.setText(getString(R.string.quiz_civ_emblem));
                question.setText(q3);
                symbol.setImageResource(R.drawable.question);
                playIconLayout.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
                int icon1 = civIcons.get(civ);
                gif.setImageResource(icon1);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed(){
        if (mp != null) {
            mp.pause();
            mp.seekTo(0);
        }
        playIcon.setImageResource(R.drawable.playicon_red);
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mp.pause();
        mp.seekTo(0);
        playIcon.setImageResource(R.drawable.playicon_red);
        return super.onNavigationItemSelected(item);
    }
}
