package org.csu.musicplayer.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;


import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by liuliu on 2018/1/14.
 */


//工具类
public class LoadSongUtils {
    //定义一个集合，存放从本地读取到的内容
    private static final String TAG = "LoadSongUtils";
    public static List<Song> list;
    public static Song song;
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static long version = 0;
    @SuppressLint({"Range", "UseCompatLoadingForDrawables"})
    public static List<Song> getLocalMusic(Context context) {
        Set<String> volumeNames = MediaStore.getExternalVolumeNames(context);
        Log.d(TAG, volumeNames.toString());
        long generation = (long) MediaStore.getGeneration(context, volumeNames.iterator().next());
        if (version != 0 && version == generation) {
            Log.d(TAG, "getmusic: Return List");
            return list;
        }

        Log.d(TAG, "getmusic: Get Music");
        Log.d(TAG, "getmusic: " + generation);
        list = new ArrayList<>();
        String remove=".mp3";

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

                , null, null, null, null);
        Log.d("LoadSongUtils", "getmusic: " + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        if (cursor != null) {

            while (cursor.moveToNext()) {

                song = new Song();
                song.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String removemp3 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).trim();
                song.song=removemp3.replace(remove,"");
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)).trim();
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)).trim();
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                song.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                song.albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Uri uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        song.albumId
                );
                try {
                    song.albumBitmap = context.getContentResolver().loadThumbnail(uri, new Size(500, 500), null);
                } catch (IOException e) {
                    song.albumBitmap = ((BitmapDrawable)context.getDrawable(R.mipmap.play_head)).getBitmap();
                    e.printStackTrace();
                }
                Log.d("LoadSongUtils", "getmusic: " + song);

//                把歌曲名字和歌手切割开
                if (song.size > 1000 * 800) {
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
                        song.singer = str[0].trim();
                        song.song = str[1].trim();
                    }
                    list.add(song);
                }

            }
        cursor.close();
        }

        Log.d("LoadSongUtils", "getMusic: " + list.toString());
        return list;

    }


    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }




    public static Bitmap getMusicBitmap(Context context, long songid, long albumid) {
        Bitmap bm = null;
        // 专辑id和歌曲id小于0说明没有专辑、歌曲，并抛出异常
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);


                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException ex) {
        }
       //如果获取的bitmap为空，则返回一个默认的bitmap
      if (bm == null) {
            Resources resources = context.getResources();
            Drawable drawable = resources.getDrawable(R.mipmap.play_head);
            //Drawable 转 Bitmap
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bm = bitmapDrawable.getBitmap();
        }

        return Bitmap.createScaledBitmap(bm, 150, 150, true);
    }

}