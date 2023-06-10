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
import org.csu.musicplayer.bean.Folder;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<Folder> folders;
    private FolderAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(FolderAdapter.OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    public FolderAdapter(List<Folder> folders) {
        this.folders = folders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_folder_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.folderImage.setImageResource(R.drawable.ic_baseline_folder_24);
        holder.path.setText(folder.path);
        holder.songCount.setText(folder.songs.size() + "首歌");
        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(view -> mOnItemClickListener.onItemClicked(view, position));
        }
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView folderImage;
        TextView path;
        TextView songCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraint_layout);
            folderImage = itemView.findViewById(R.id.folderView);
            path = itemView.findViewById(R.id.folderPath);
            songCount = itemView.findViewById(R.id.song_number);

        }
    }
}
