package org.csu.musicplayer.utils;

import android.annotation.SuppressLint;
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
import org.csu.musicplayer.bean.Album;
import org.csu.musicplayer.bean.Artist;
import org.csu.musicplayer.bean.Folder;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.dao.HistoryDao;
import org.csu.musicplayer.dao.PlaylistDao;
import org.csu.musicplayer.dao.PlaylistItemDao;
import org.csu.musicplayer.database.HistoryDatabase;
import org.csu.musicplayer.database.PlaylistDatabase;
import org.csu.musicplayer.database.PlaylistItemDatabase;
import org.csu.musicplayer.entity.History;
import org.csu.musicplayer.entity.Playlist;
import org.csu.musicplayer.entity.PlaylistItem;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by liuliu on 2018/1/14.
 */


//工具类
public class LoadSongUtils {
    //定义一个集合，存放从本地读取到的内容
    private static final String TAG = "LoadSongUtils";
    public static List<Song> list;
    public static Map<Integer, Song> songMap;
    public static List<Album> albumList;
    public static List<Folder> folders;
    public static List<Artist> artists;
    public static boolean isAlbumUpdated = false;
    public static Song song;
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static long version = 0;
    public static int favoriteListId;
    private static PlaylistDao playlistDao;
    private static PlaylistItemDao playlistItemDao;
    private static Map<Integer, Song> favoriteMap;

    private static boolean checkUpdate(Context context) {
        Set<String> volumeNames = MediaStore.getExternalVolumeNames(context);
        Log.d(TAG, volumeNames.toString());
        long generation = (long) MediaStore.getGeneration(context, volumeNames.iterator().next());
        if (version != 0 && version == generation) {
            Log.d(TAG, "No Update");
            return false;
        }
        version = generation;
        return true;
    }

    @SuppressLint({"Range", "UseCompatLoadingForDrawables"})
    public static List<Song> getLocalMusic(Context context) {
        if (!checkUpdate(context)) {
            return list;
        }
        Log.d(TAG, "getmusic: Get Music");
//        list = new ArrayList<>();
        songMap = new HashMap<>();
        String remove = ".mp3";

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

                , null, null, null, null);
        Log.d("LoadSongUtils", "getmusic: " + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        if (cursor != null) {

            while (cursor.moveToNext()) {

                song = new Song();
                song.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String removemp3 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).trim();
                song.song = removemp3.replace(remove, "");
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
                    song.albumBitmap = ((BitmapDrawable) context.getDrawable(R.mipmap.play_head)).getBitmap();
                    e.printStackTrace();
                }
                Log.d("LoadSongUtils", "getmusic: " + song);

//                把歌曲名字和歌手切割开
                if (song.size > 1000 * 800) {
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
//                        song.singer = str[0].trim();
                        song.song = song.song.replace(str[0] + '-', "");
                    }
//                    list.add(song);
                    songMap.put(song.id, song);
                }

            }
            cursor.close();
        }
        list = new ArrayList<>(songMap.values());
        Log.d("LoadSongUtils", "getMusic: " + list);
        return list;

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static List<Album> getAlbums(Context context) {
        if (!checkUpdate(context) && isAlbumUpdated) {
            return albumList;
        }
        Map<String, Album> albumMap = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
                , null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Album album = new Album();
                album.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID));
                album.albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                album.artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
                Uri uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        album.albumId
                );
                try {
                    album.albumBitmap = context.getContentResolver().loadThumbnail(uri, new Size(500, 500), null);
                } catch (IOException e) {
                    album.albumBitmap = ((BitmapDrawable) context.getDrawable(R.mipmap.play_head)).getBitmap();
                    e.printStackTrace();
                }
                albumMap.put(String.valueOf(album.albumId), album);

            }
            cursor.close();
        }
        for (Song s : list) {
            Objects.requireNonNull(albumMap.get(String.valueOf(s.albumId))).songs.add(s);
        }
        albumList = new ArrayList<>(albumMap.values());
        Log.d("LoadSongUtils", "getAlbums: " + albumList);
        return albumList;
    }

    public static List<Artist> getArtistList(Context context) {
        getLocalMusic(context);
        Map<String, Artist> artistMap = new HashMap<>();
        for (Song song : list){
            String artistName = song.singer;
            if (artistMap.containsKey(artistName)) {
                artistMap.get(artistName).songs.add(song);
            } else {
                Artist artist = new Artist();
                artist.ArtistName = artistName;
                artist.songs.add(song);
                artistMap.put(artistName, artist);
            }
        }
        artists = new ArrayList<>(artistMap.values());
        return artists;
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

    public static List<Folder> getFolder(Context context) {
        getLocalMusic(context);
        Map<String, Folder> folderMap = new HashMap<>();
        for (Song song : list){
            List<String> paths = new ArrayList<>(Arrays.asList(song.path.replace("/storage/emulated/0/", "").split("/")));
            paths.remove(paths.size() - 1);
            StringBuilder clearPath = new StringBuilder();
            for (String path : paths) {
                clearPath.append("/");
                clearPath.append(path);
            }

            if (folderMap.containsKey(clearPath.toString())) {
                folderMap.get(clearPath.toString()).songs.add(song);
            } else {
                Folder folder = new Folder();
                folder.path = clearPath.toString();
                folder.songs.add(song);
                folderMap.put(folder.path, folder);
            }
        }
        folders = new ArrayList<>(folderMap.values());
        return folders;
    }
    public static List<Song> getHistories(Context context) {
        getLocalMusic(context);
        HistoryDao historyDao = HistoryDatabase.getInstance(context).getHistoryDao();
        List<History> histories = historyDao.getHistories();
        List<Song> songList = new ArrayList<>();
        for (History history:
             histories) {
            if (songMap.containsKey(history.getMediaId())) {
                Song song = songMap.get(history.getMediaId());
                if (!songList.contains(song))
                    songList.add(song);

            }
        }
        return songList;
    }


    public static void initLocalMusic(Context context) {
        getLocalMusic(context);
        initDataBase(context);
        getFavoriteListId(context);
        initFavoriteList();
    }

    private static void initFavoriteList() {
        List<PlaylistItem> favoriteItems = playlistItemDao.getPlaylistItemsByPlaylistId(favoriteListId);
        favoriteMap = new HashMap<>();
        for (PlaylistItem favoriteItem : favoriteItems) {
            int mediaId = favoriteItem.getMediaId();
            Song song = songMap.get(mediaId);
            if (song != null) {
                favoriteMap.put(mediaId, song);
            }
        }
    }

    private static void initDataBase(Context context) {
        playlistDao = PlaylistDatabase.getInstance(context).getPlaylistDao();
        playlistItemDao = PlaylistItemDatabase.getInstance(context).getPlaylistItemDao();
    }

    private static void getFavoriteListId(Context context) {
        List<Playlist> playlists = playlistDao.getPlaylist();
        for (Playlist pl :
                playlists) {
            if ("favorite".equals(pl.getType())) {
                favoriteListId = pl.getId();
                return;
            }
        }
        // favorite list not exist, add one
        Playlist playList = new Playlist();
        playList.setName("我的收藏");
        playList.setType("favorite");
        playlistDao.insertPlaylist(playList);
        getFavoriteListId(context);
    }
    public static List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = playlistDao.getPlaylist();
        playlists.removeIf(pl -> pl.getId() == favoriteListId);
        return playlists;
    }
    public static List<Song> getSongListByPlayListId(int playlistId) {
        List<Song> songList = new ArrayList<>();
        List<PlaylistItem> playlistItems = playlistItemDao.getPlaylistItemsByPlaylistId(playlistId);
        for (PlaylistItem playlistItem : playlistItems) {
            if (songMap.containsKey(playlistItem.getMediaId())) {
                songList.add(songMap.get(playlistItem.getMediaId()));
            }
        }
        return songList;
    }
    public static Map<Integer, Song> getSongMapByPlayListId(int playlistId) {
        Map<Integer, Song> playlistMap = new HashMap<>();
        List<PlaylistItem> playlistItems = playlistItemDao.getPlaylistItemsByPlaylistId(playlistId);
        for (PlaylistItem playlistItem : playlistItems) {
            if (songMap.containsKey(playlistItem.getMediaId())) {
                playlistMap.put(playlistItem.getMediaId(), songMap.get(playlistItem.getMediaId()));
            }
        }
        return playlistMap;
    }
    public static boolean isFavorite(int mediaId) {
        return favoriteMap.containsKey(mediaId);
    }

    public static void addToPlaylist(int playlistId, int mediaId) {
        Map<Integer, Song> playlistMap = getSongMapByPlayListId(playlistId);
        if (!playlistMap.containsKey(mediaId)) {
            PlaylistItem playlistItem = new PlaylistItem();
            playlistItem.setPlaylistId(playlistId);
            playlistItem.setMediaId(mediaId);
            playlistItemDao.insertPlaylistItem(playlistItem);
        }
        if (playlistId == favoriteListId) {
            initFavoriteList();
        }
    }
    public static void deleteFromPlaylist(int playlistId, int mediaId) {
        playlistItemDao.deleteByPlaylistIdAndMediaId(playlistId, mediaId);
        if (playlistId == favoriteListId) {
            initFavoriteList();
        }
    }
    public static void createPlaylist(String name) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setType("normal");
        playlistDao.insertPlaylist(playlist);

    }
    public static int getPlaylistCount(int playlistId) {
        return playlistItemDao.getPlaylistItemsByPlaylistId(playlistId).size();
    }
    public static Playlist getPlaylistById(int playlistId) {
        return playlistDao.getPlaylistById(playlistId);
    }
}