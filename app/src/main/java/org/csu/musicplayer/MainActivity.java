package org.csu.musicplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.csu.musicplayer.databinding.ActivityMainBinding;
import org.csu.musicplayer.ui.main.SectionsPagerAdapter;
import org.csu.musicplayer.utils.PermissionUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private MediaBrowserCompat mBrowser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PermissionUtils.initCheckSelfPermission(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(new SectionsPagerAdapter(this));



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
                        tab.setText("发现");
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();

        LinearLayout layout = findViewById(R.id.mini_player);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

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

    private void showDialog(){
        Dialog dialog= new AlertDialog.Builder(this)
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




}