package com.aoedb.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aoedb.database.Database;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(Database.readLanguage(this));
        Database.setContext(this);

    }

    protected void setLocale(String localeCode){
        Locale locale = new Locale(localeCode);
        Configuration conf = getBaseContext().getResources().getConfiguration();
        conf.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        Configuration systemConf = Resources.getSystem().getConfiguration();
        systemConf.setLocale(locale);
        Resources.getSystem().updateConfiguration(systemConf, Resources.getSystem().getDisplayMetrics());
        Locale.setDefault(locale);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale(Database.readLanguage(this));
    }
}
