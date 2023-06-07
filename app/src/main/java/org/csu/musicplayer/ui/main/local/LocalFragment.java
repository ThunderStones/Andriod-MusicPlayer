package org.csu.musicplayer.ui.main.local;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.ui.main.local.list.LocalMusicActivity;


public class LocalFragment extends Fragment {

    private final String TAG = "local_fragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_local, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = requireView().findViewById(R.id.lv_local);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LocalMusicActivity.class);

                Log.d(TAG, "onItemClick: position="+ position + ";text=" + parent.getItemAtPosition(position).toString() );
                startActivity(intent);
            }

        });
    }
}