package org.csu.musicplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.makeramen.roundedimageview.RoundedImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.databinding.ActivityMainBinding;
import org.csu.musicplayer.service.MusicService;
import org.csu.musicplayer.ui.main.SectionsPagerAdapter;
import org.csu.musicplayer.utils.LoadSongUtils;
import org.csu.musicplayer.utils.MusicProviderUtils;
import org.csu.musicplayer.utils.PermissionUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private MediaBrowserCompat mBrowser;
    private LinearLayout layout;
    private MainActivity.MusicReceiver musicReceive;
    private MusicService.MusicController controller;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Service Connected");
            controller = (MusicService.MusicController) service;
            Song song = controller.getCurrentSong();
            Log.d(TAG, "onServiceConnected: " + song);
            if (song != null) {
                updateSongInfo(song);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void initBroadcast() {
        musicReceive = new MainActivity.MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("music");
        registerReceiver(musicReceive, filter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSongUtils.initLocalMusic(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PermissionUtils.initCheckSelfPermission(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(new SectionsPagerAdapter(this));
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

        TabLayout tabs = binding.tabs;
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabs, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("本地");
                        break;
                    case 1:
                        tab.setText("歌单");
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();

        layout = findViewById(R.id.mini_player);
        ImageView albumPic = layout.findViewById(R.id.MainButtomPic);
        albumPic.setImageResource(R.drawable.ic_baseline_access_time_24);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });

        initBroadcast();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unbindService(connection);
        unregisterReceiver(musicReceive);


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "onRequestPermissionsResult: " + requestCode);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showDialog();
            }
        }
    }

    private void showDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("请授权必要权限")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PermissionUtils.initCheckSelfPermission(MainActivity.this);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
        dialog.setCancelable(false);//点击返回键不消失
        dialog.show();
    }

    public class MusicReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals("music")) {
                int index = intent.getIntExtra("index", -1);
                if (index != -1) {
                    Song song = MusicProviderUtils.getPlaylist().get(index);
                    Log.d(TAG, "onReceive: " + song.toString());
                    updateSongInfo(song);
                }
            }
        }
    }

    private void updateSongInfo(Song song) {
        ImageView albumPic = layout.findViewById(R.id.MainButtomPic);
        TextView songName = layout.findViewById(R.id.tv_song_name);
        TextView artistName = layout.findViewById(R.id.tv_singer);
        albumPic.setImageBitmap(song.albumBitmap);
        songName.setText(song.song);
        artistName.setText(song.singer);
    }
}