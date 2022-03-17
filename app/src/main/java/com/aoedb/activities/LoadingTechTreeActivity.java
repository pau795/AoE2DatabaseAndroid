package com.aoedb.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.aoedb.R;
import com.aoedb.database.Database;

public class LoadingTechTreeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_tech_tree);
        int civID = getIntent().getIntExtra(Database.CIV,0);
        new TechTreeLoader(this, civID).execute(String.valueOf(civID));
    }

    @Override
    public void onBackPressed() {
    }

    private class TechTreeLoader extends AsyncTask<String, Void, String> {

        Context c;
        Intent i;
        int civID;
        public TechTreeLoader (Context c, int id){
            this.c=c;
            this.civID=id;
        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i =  new Intent(c, TechTreeActivity.class);
            i.putExtra(Database.CIV, civID);

            ((Activity) c).finish();
            return "Loaded";
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity(i);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
