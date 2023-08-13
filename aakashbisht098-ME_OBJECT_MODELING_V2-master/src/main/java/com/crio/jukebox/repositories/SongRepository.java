package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.crio.jukebox.entities.Song;

public class SongRepository implements ISongRepository {
    private final Map<String,Song> songmap;

    public SongRepository()
    {
        songmap= new HashMap<String,Song>();
    }

    public SongRepository(Map<String,Song> songmap)
    {
        this.songmap=songmap;
    }

    @Override
    public Song save(Song entity)
    {
        songmap.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public int getLength()
    {
        return songmap.size();
    }

    @Override
    public Optional<Song> findSongById(String id)
    {
       return Optional.ofNullable(songmap.get(id));
    }
}
