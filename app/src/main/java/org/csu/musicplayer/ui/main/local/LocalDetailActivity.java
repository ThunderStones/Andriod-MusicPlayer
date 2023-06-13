package org.csu.musicplayer.ui.main.local;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Artist;
import org.csu.musicplayer.bean.Folder;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.ui.main.local.list.SingleSongAdapter;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalDetailActivity extends AppCompatActivity {
    private static final String TAG = "LocalDetailActivity";
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
        int albumIndex = intent.getIntExtra("albumIndex", -1);
        int folderIndex = intent.getIntExtra("folderIndex", -1);
        int artistIndex = intent.getIntExtra("artistIndex", -1);
        if (albumIndex != -1) {
            Album album = LoadSongUtils.albumList.get(albumIndex);
            songs = album.songs;
            toolbar.setTitle(album.albumName);
        } else if (folderIndex != -1) {
            Folder folder = LoadSongUtils.folders.get(folderIndex);
            songs = folder.songs;
            toolbar.setTitle(folder.path);
        } else if (artistIndex != -1) {
            Artist artist = LoadSongUtils.artists.get(artistIndex);
            songs = artist.songs;
            toolbar.setTitle(artist.ArtistName);
        }
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