package com.crio.jukebox.repositories;

import java.util.List;
import java.util.Optional;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;

public interface IPlayListRepository {
    public PlayList save(PlayList p);
    public void deleteById(String playList);
    public PlayList addToPlayList(PlayList playList, List<Song> songs);
    public PlayList deleteFromPlayList(PlayList playlist, List<Song> songs);
    public <T> Optional<T> findById(String id);
    public int getLength();
}
