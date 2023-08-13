package com.crio.jukebox.services;

import java.util.List;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;

public interface IPlayListService {
    public PlayList create(String id, String name, List<String> songs);
    public PlayList addSongsToPlayList(String userId, String playListId, List<String> songIds);
    public void DeletePlayList(String userId, String playListId);
    public PlayList DeleteSongsFromPlayList(String userId, String playListId, List<String> songIds);
    public Song PlayPlayList(String userId, String playListId);
    public Song PlayNextSong(String userId);
    public Song PlayPreviousSong(String userId);
    public Song PlaySong(String userId, String songId);
}
