package org.csu.musicplayer.ui.main.local.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;

import java.util.List;

public class SingleSongAdapter extends RecyclerView.Adapter<SingleSongAdapter.ViewHolder>{
    private List<Song> localSongList;
    private OnItemClickListener   mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener){
        this.mOnItemClickListener = mOnItemClickLitener;
    }
    public SingleSongAdapter(List<Song> localSongList) {
        this.localSongList = localSongList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_song_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = localSongList.get(position);
        holder.imageView.setImageBitmap(song.albumBitmap);
        holder.songName.setText(song.song);
        holder.artistName.setText(song.singer);
        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClicked(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return localSongList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView imageView;
        TextView songName;
        TextView artistName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraint_layout);
            imageView = itemView.findViewById(R.id.imageView);
            songName = itemView.findViewById(R.id.t_song);
            artistName = itemView.findViewById(R.id.t_singer);


        }
    }
}
