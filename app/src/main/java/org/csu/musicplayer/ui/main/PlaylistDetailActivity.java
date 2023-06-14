package org.csu.musicplayer.ui.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Artist;
import org.csu.musicplayer.bean.Folder;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.entity.Playlist;
import org.csu.musicplayer.service.MusicService;
import org.csu.musicplayer.ui.main.local.list.SingleSongAdapter;
import org.csu.musicplayer.utils.LoadSongUtils;
import org.csu.musicplayer.utils.MusicProviderUtils;

import java.util.List;

public class PlaylistDetailActivity extends AppCompatActivity {
    private static final String TAG = "PlaylistDetailActivity";
    private List<Song> songs;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Playlist playlist;
    private MusicService.MusicController controller;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Service Connected");
            controller = (MusicService.MusicController) service;
            Song song = controller.getCurrentSong();
            Log.d(TAG, "onServiceConnected: " + song);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_detail);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_detail);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        int playlistId = intent.getIntExtra("playlistId", -1);
        playlist = LoadSongUtils.getPlaylistById(playlistId);
        songs = LoadSongUtils.getSongListByPlayListId(playlist.getId());
        toolbar.setTitle(playlist.getName());

        setSupportActionBar(toolbar);
        SingleSongAdapter adapter = null;
        adapter = new SingleSongAdapter(songs);
//        adapter.setOnItemClickListener((view1, position) -> Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        Intent intent2 = new Intent(PlaylistDetailActivity.this, MusicService.class);
        bindService(intent2, connection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.playlist, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_play:
                Log.d(TAG, "onOptionsItemSelected: ");
                List<Song> songs = LoadSongUtils.getSongListByPlayListId(playlist.getId());
                Log.d(TAG, "onOptionsItemSelected: " + songs.toString());
                MusicProviderUtils.setPlaylist(songs);
                controller.playIndex(0);
        }   
        return true;
    }

}
