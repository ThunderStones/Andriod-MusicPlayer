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
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Song;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;
    private AlbumAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }
    public void setOnItemClickListener(AlbumAdapter.OnItemClickListener mOnItemClickLitener){
        this.mOnItemClickListener = mOnItemClickLitener;
    }
    public AlbumAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.albumImage.setImageBitmap(album.albumBitmap);
        holder.singer.setText(album.artistName);
        holder.albumName.setText(album.albumName);
        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(view -> mOnItemClickListener.onItemClicked(view, position));
        }

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView albumImage;
        TextView albumName;
        TextView singer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraint_layout);
            albumImage = itemView.findViewById(R.id.albumImage);
            albumName = itemView.findViewById(R.id.albumName);
            singer = itemView.findViewById(R.id.singer);
        }


    }
}
