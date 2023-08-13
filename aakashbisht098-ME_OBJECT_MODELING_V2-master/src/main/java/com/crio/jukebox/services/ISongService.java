package com.crio.jukebox.services;

import java.util.List;
import com.crio.jukebox.entities.Song;

public interface ISongService {
    public Song create(String album, String owner, List<String> artists, String songname);
}
