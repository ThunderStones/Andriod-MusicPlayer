package org.csu.musicplayer.ui.main.local;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Artist;
import org.csu.musicplayer.bean.Folder;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.ui.main.local.list.SingleSongAdapter;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private List<Song> songs;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_detail);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_detail);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("最近播放");
        //TO-DO : get song list
        songs = LoadSongUtils.getHistories(getApplicationContext());
        setSupportActionBar(toolbar);
        SingleSongAdapter adapter = null;
        adapter = new SingleSongAdapter(songs);

//        adapter.setOnItemClickListener((view1, position) -> Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
