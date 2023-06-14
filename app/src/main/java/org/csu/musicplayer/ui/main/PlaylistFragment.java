package org.csu.musicplayer.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.entity.Playlist;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaylistFragment extends Fragment {
    private static final String TAG = "PlaylistFragment";
    private List<Playlist> playlists;
    private RecyclerView recyclerView;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = view.findViewById(R.id.rv_playlist);
        textView = view.findViewById(R.id.tv_playlist);

        playlists = LoadSongUtils.getAllPlaylists();
        PlaylistAdapter adapter = new PlaylistAdapter(playlists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请输入歌单名称");

                // Set up the input
                final EditText input = new EditText(getContext());
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        // do something with the name
                        // create playlist with name
                        LoadSongUtils.createPlaylist(name);
                        playlists = LoadSongUtils.getAllPlaylists();
                        adapter.setPlaylistList(playlists);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        return view;
    }
}
