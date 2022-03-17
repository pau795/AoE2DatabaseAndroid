package com.aoedb.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.aoedb.R;
import com.aoedb.database.Database;

public class LoadingAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);
        new LoadingAppActivity.AppLoader(this).execute();
    }


    @Override
    public void onBackPressed() {
    }

    private class AppLoader extends AsyncTask<String, Void, String> {

        Context c;
        Intent i;
        public AppLoader (Context c){
            this.c=c;
        }

        @Override
        protected String doInBackground(String[] params) {

            Database.loadDatabase(c);
            i = new Intent(c, MainActivity.class);
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