package org.csu.musicplayer.ui.main.local.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.util.List;


public class SingleSongFragment extends Fragment {
    private RecyclerView recyclerView;

    public SingleSongFragment() {
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
        recyclerView = view.findViewById(R.id.rv_song);
        List<Song> songs = LoadSongUtils.getLocalMusic(getContext());
        SingleSongAdapter adapter = new SingleSongAdapter(songs);
        adapter.setOnItemClickListener(new SingleSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        return view;
    }
}