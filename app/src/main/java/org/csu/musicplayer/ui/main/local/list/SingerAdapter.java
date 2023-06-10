package org.csu.musicplayer.ui.main.local.list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Artist;

import java.util.List;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {
    private List<Artist> artists;
    private SingerAdapter.OnItemClickListener mOnItemClickListener;

    public SingerAdapter(List<Artist> artists) {
        this.artists = artists;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(SingerAdapter.OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_singer_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.artistImage.setImageResource(R.drawable.ic_baseline_person_24);
        holder.artistName.setText(artist.ArtistName);
        holder.songCount.setText(artist.songs.size() + "首歌");
        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(view -> mOnItemClickListener.onItemClicked(view, position));
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView artistImage;
        TextView artistName;
        TextView songCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.constraint_layout);
            this.artistImage = itemView.findViewById(R.id.imageView);
            this.artistName = itemView.findViewById(R.id.artistName);
            this.songCount = itemView.findViewById(R.id.song_number);
        }
    }
}
