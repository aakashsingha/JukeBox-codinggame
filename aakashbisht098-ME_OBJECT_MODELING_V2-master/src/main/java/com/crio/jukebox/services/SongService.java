package com.crio.jukebox.services;

import java.util.List;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.SongRepository;

public class SongService implements ISongService {
    private final ISongRepository songRepository;

    public SongService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public Song create(String album, String owner, List<String> artists, String songname){
        Song songToCreate = new Song(Integer.toString(songRepository.getLength() + 1), album, owner, artists, songname);
        return songRepository.save(songToCreate);
    }
}
