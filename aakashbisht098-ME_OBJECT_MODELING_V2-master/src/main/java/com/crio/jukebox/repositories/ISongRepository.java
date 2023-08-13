package com.crio.jukebox.repositories;

import java.util.Optional;
import com.crio.jukebox.entities.Song;

public interface ISongRepository {
    public <T> Optional <T> findSongById(String id);
    public int getLength();
    public Song save(Song entity);
}
