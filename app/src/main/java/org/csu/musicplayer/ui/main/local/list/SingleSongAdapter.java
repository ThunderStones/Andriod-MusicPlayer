package org.csu.musicplayer.ui.main.local.list;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.entity.Playlist;
import org.csu.musicplayer.utils.LoadSongUtils;
import org.csu.musicplayer.utils.MusicProviderUtils;

import java.text.DecimalFormat;
import java.util.List;

public class SingleSongAdapter extends RecyclerView.Adapter<SingleSongAdapter.ViewHolder> {
    private static final String TAG = "SingleSongAdapter";
    private List<Song> localSongList;
    private String favorite;
    //    private OnItemClickListener   mOnItemClickListener;
//    public interface OnItemClickListener {
//        void onItemClicked(View view, int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener){
//        this.mOnItemClickListener = mOnItemClickLitener;
//    }
    public SingleSongAdapter(List<Song> localSongList) {
        this.localSongList = localSongList;
    }

    public SingleSongAdapter(List<Song> localSongList, String favorite) {
        this.localSongList = localSongList;
        this.favorite = favorite;
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

    }

    @Override
    public int getItemCount() {
        return localSongList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout layout;
        ImageView imageView;
        TextView songName;
        TextView artistName;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.constraint_layout);
            imageView = itemView.findViewById(R.id.imageView);
            songName = itemView.findViewById(R.id.t_song);
            artistName = itemView.findViewById(R.id.t_singer);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            initDialog(position);

        }

        private void initDialog(int position) {
            Song song = localSongList.get(position);
            View view = View.inflate(context, R.layout.local_dialog, null);
            TextView dialog_song=view.findViewById(R.id.dialog_song);//歌名
            dialog_song.setText(song.song);
            TextView dialog_album=view.findViewById(R.id.dialog_album);//专辑
            dialog_album.setText(song.album);
            TextView dialog_singer=view.findViewById(R.id.dialog_singer);//歌手
            dialog_singer.setText(song.singer);
            TextView dialog_singer2=view.findViewById(R.id.dialog_singer2);//歌手
            dialog_singer2.setText(song.singer);
            TextView dialog_time=view.findViewById(R.id.dialog_time);//时长
            dialog_time.setText(LoadSongUtils.formatTime(song.duration));
            TextView dialog_size=view.findViewById(R.id.dialog_size);//歌曲大小
            dialog_size.setText(((new DecimalFormat("#.00")).format(Long.valueOf(song.size).floatValue()/1000000)));
            ImageView localDialogIcon=view.findViewById(R.id.localDialogIcon);//歌手
            localDialogIcon.setImageBitmap(song.albumBitmap);

            // 收藏
            TextView favoriteText = view.findViewById(R.id.local_favorite);
            ImageView favoriteIcon = view.findViewById(R.id.favorite_icon);
            Log.d(TAG, "initDialog: " + LoadSongUtils.isFavorite(song.id));
            if (LoadSongUtils.isFavorite(song.id)) {
                favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
                favoriteText.setText("取消收藏");
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                favoriteText.setText("收藏");
            }

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
            TextView addToPlaylist = view.findViewById(R.id.local_add_to_playlist);
            addToPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song s = localSongList.get(position);
                    MusicProviderUtils.addToPlaylist(s);
                    Toast.makeText(context, "Add To Playlist:" + s.song, Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.cancel();
                }
            });
            favoriteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favoriteText.getText().toString().equals("收藏")) {
                        LoadSongUtils.addToPlaylist(LoadSongUtils.favoriteListId, localSongList.get(position).id);
                    } else {
                        LoadSongUtils.deleteFromPlaylist(LoadSongUtils.favoriteListId, localSongList.get(position).id);
                        if ("True".equals(favorite)) {
                            localSongList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    bottomSheetDialog.cancel();
                }
            });
            //添加到歌单
            TextView addToList = view.findViewById(R.id.tv_add_to_list);
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Playlist> playlists = LoadSongUtils.getAllPlaylists();
                    final CharSequence[] items = new CharSequence[playlists.size()];
                    for (int i = 0; i < playlists.size(); i++) {
                        items[i] = playlists.get(i).getName();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("选择一个歌单");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // do something with the selection
                            Toast.makeText(context, playlists.get(item).getName(), Toast.LENGTH_SHORT).show();
                            LoadSongUtils.addToPlaylist(playlists.get(item).getId(), song.id);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }
}
