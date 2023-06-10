package org.csu.musicplayer.ui.main.local.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.ui.main.local.LocalDetailActivity;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.io.Serializable;
import java.util.List;

public class AlbumFragment extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "AlbumFragment";
    private List<Album> albums;
    public AlbumFragment() {
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
        albums = LoadSongUtils.getAlbums(getContext());
        AlbumAdapter adapter = null;
        adapter = new AlbumAdapter(albums);


        adapter.setOnItemClickListener((view1, position) -> {
            Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("albumIndex", position);
            intent.setClass(getActivity(), LocalDetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
    }

}
