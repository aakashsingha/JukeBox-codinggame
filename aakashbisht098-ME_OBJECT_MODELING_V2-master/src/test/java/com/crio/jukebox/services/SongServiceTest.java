package com.crio.jukebox.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.repositories.ISongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SongServiceTest")
@ExtendWith(MockitoExtension.class)

public class SongServiceTest {
    @Mock
    private ISongRepository songRepositoryMock;

    @InjectMocks
    private SongService songService;

    @DisplayName("Create should create Song")
    @Test
    public void create_ShouldReturnSong()
    {
        Song expectedSong = new Song("1","Album","owner",Collections.singletonList("artist1"),"songName");
        when(songRepositoryMock.save(any(Song.class))).thenReturn(expectedSong);

        Song actualSong = songService.create("Album","owner",Collections.singletonList("artist1"),"songName");

        Assertions.assertEquals(expectedSong.getId(),actualSong.getId());
        Assertions.assertEquals(expectedSong.getSongname(),actualSong.getSongname());
        Assertions.assertEquals(expectedSong.getOwner(),actualSong.getOwner());
        Assertions.assertEquals(expectedSong.getAlbum(),actualSong.getAlbum());
        Assertions.assertEquals(expectedSong.getArtists(),actualSong.getArtists());
        verify(songRepositoryMock,times(1)).save(any(Song.class));
    }
}
