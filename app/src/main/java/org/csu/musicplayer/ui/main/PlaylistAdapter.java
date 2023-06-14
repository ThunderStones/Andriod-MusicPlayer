package org.csu.musicplayer.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.entity.Playlist;
import org.csu.musicplayer.utils.LoadSongUtils;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    private List<Playlist> playlistList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item,parent, false);
        return new ViewHolder(view);
    }

    public PlaylistAdapter(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        int count = LoadSongUtils.getPlaylistCount(playlist.getId());
        holder.playlistCount.setText(count + "首歌");
        holder.playlistName.setText(playlist.getName());
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView playlistName;
        TextView playlistCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.playlist_name);
            playlistCount = itemView.findViewById(R.id.playlist_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO
            Intent intent = new Intent();
            intent.setClass(v.getContext(), PlaylistDetailActivity.class);
            intent.putExtra("playlistId", playlistList.get(getAdapterPosition()).getId());
            v.getContext().startActivity(intent);
        }
    }
}
