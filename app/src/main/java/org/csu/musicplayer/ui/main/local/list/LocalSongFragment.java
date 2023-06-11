package org.csu.musicplayer.ui.main.local.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.utils.LoadSongUtils;
import org.csu.musicplayer.utils.MusicProviderUtils;

import java.util.List;


public class LocalSongFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Song> songs;
    private static final String TAG = "SingleSongFragment";
    public LocalSongFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_song, container, false);
        initRecycleView(view);

        return view;
    }

    private void initRecycleView(View view) {

        recyclerView = view.findViewById(R.id.rv_song);
        songs = LoadSongUtils.getLocalMusic(getContext());
        SingleSongAdapter adapter = null;
        adapter = new SingleSongAdapter(songs);


        adapter.setOnItemClickListener((view1, position) -> {
            Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            MusicProviderUtils.addToPlaylist(songs.get(position));
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
    }

}