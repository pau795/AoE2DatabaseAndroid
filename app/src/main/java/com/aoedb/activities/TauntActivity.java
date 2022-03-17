package com.aoedb.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aoedb.R;
import com.aoedb.adapters.TauntAdapter;
import com.aoedb.data.TauntElement;
import com.aoedb.database.Database;

import java.util.List;

public class TauntActivity extends DrawerActivity {

    Context ctx;
    List<TauntElement> dataModels;
    ListView list;
    TauntAdapter adapter;
    MediaPlayer mp;
    int lastPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_taunts);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = this;
        View contentView = inflater.inflate(R.layout.activity_listview, null, false);
        setActivityView(contentView);
        list = findViewById(R.id.element_list);
        dataModels = Database.getTauntList();
        adapter = new TauntAdapter(this, dataModels);
        list.setAdapter(adapter);
        lastPos = -1;
        mp = MediaPlayer.create(ctx,R.raw.t_1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View v, int position, long id) {
                if (lastPos == position && mp.isPlaying()){
                    mp.stop();
                }
                else {
                    TauntElement l3 = adapter.getItem(position);
                    mp.stop();
                    mp.reset();
                    mp = MediaPlayer.create(ctx, l3.getFileID());
                    mp.start();
                    lastPos = position;
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        mp.pause();
        mp.seekTo(0);
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mp.pause();
        mp.seekTo(0);
        return super.onNavigationItemSelected(item);
    }
}
