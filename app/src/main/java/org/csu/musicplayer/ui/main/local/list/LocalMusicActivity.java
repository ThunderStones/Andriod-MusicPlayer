package org.csu.musicplayer.ui.main.local.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.csu.musicplayer.R;

import java.util.Objects;

public class LocalMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Local Music");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);

        ViewPager2 viewPager = findViewById(R.id.local_view_pager);
        viewPager.setAdapter(new LocalPagerAdapter(this));

        TabLayout tabs = findViewById(R.id.local_tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabs, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("单曲");
                        break;
                    case 1:
                        tab.setText("专辑");
                        break;
                    case 2:
                        tab.setText("歌手");
                        break;
                    case 3:
                        tab.setText("文件夹");
                        break;

                }
            }
        }
        );
        tabLayoutMediator.attach();
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