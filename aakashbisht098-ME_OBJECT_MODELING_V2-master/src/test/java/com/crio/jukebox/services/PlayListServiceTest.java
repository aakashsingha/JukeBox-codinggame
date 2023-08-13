package com.crio.jukebox.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.crio.jukebox.repositories.IUserRepository;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.exceptions.PlayListAndUserDoesNotMatchException;
import com.crio.jukebox.exceptions.PlayListNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.repositories.IPlayListRepository;
import com.crio.jukebox.repositories.ISongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PlayListServiceTests")
@ExtendWith(MockitoExtension.class)

public class PlayListServiceTest {

        @Mock
        private IUserRepository userRepositoryMock;

        @Mock
        IPlayListRepository playListRepositoryMock;

        @Mock
        ISongRepository songRepositoryMock;

        @InjectMocks
        PlayListService playListService;

        @Test
        @DisplayName("Create should create New PlayList")
        public void create_ShouldReturnPlayListIfAllSongsArePresentAndUserIsPresent() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList expectedplayList = new PlayList("1", user, "PlayListName1",
                                Collections.singletonList(song));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                PlayList actualPlayList = playListService.create("1", "PlayListName1",
                                Collections.singletonList("1"));

                Assertions.assertEquals(expectedplayList.getId(), actualPlayList.getId());
                Assertions.assertEquals(expectedplayList.getPlayListName(),
                                actualPlayList.getPlayListName());
                Assertions.assertEquals(expectedplayList.getSongs().size(),
                                actualPlayList.getSongs().size());
                verify(userRepositoryMock, times(1)).findUserById(any(String.class));
                verify(songRepositoryMock, times(1)).findSongById(any(String.class));
        }

        @Test
        @DisplayName("Create should throw song not found exception")
        public void create_ShouldThrowSongNotFoundExceptionIfAnySongIsNotPresent() {
                User user = new User("1", "Amit");
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.empty());

                Assertions.assertThrows(SongNotFoundException.class, () -> playListService
                                .create("1", "PlayListName1", Collections.singletonList("1")));

                verify(userRepositoryMock, times(1)).findUserById(any(String.class));
                verify(songRepositoryMock, times(1)).findSongById(any(String.class));
        }

        @Test
        @DisplayName("Create should throw user not found exception")
        public void create_ShouldThrowUserNotFoundExceptionIfUserIsNotPresent() {
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                Assertions.assertThrows(SongNotFoundException.class, () -> playListService
                                .create("1", "PlayListName1", Collections.singletonList("1")));

                verify(userRepositoryMock, times(1)).findUserById(any(String.class));
        }

        @Test
        @DisplayName("addSongToPlayList should return PlayList")

        public void addSongsToPlayList_shouldReturnPlayListIfSongsArePresentAndUserIsFoundAndPlayListIsFound() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                user.setPlayList(Collections.singletonList(playList));
                PlayList expectedPlayList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));
                when(playListRepositoryMock.addToPlayList(any(PlayList.class), any()))
                                .thenReturn(expectedPlayList);
                when(userRepositoryMock.save(any(User.class))).thenReturn(user);

                PlayList actualPlayList = playListService.addSongsToPlayList("1", "1",
                                Collections.singletonList("1"));

                Assertions.assertEquals(song.getId(), actualPlayList.getSongs()
                                .get(actualPlayList.getSongs().size() - 1).getId());
        }

        @Test
        @DisplayName("addSongToPlayList should throw Song not found exception")

        public void addSongsToPlayList_shouldThrowSongNotFoundExceptionIfSongsAreNotPresent() {
                User user = new User("1", "Amit");
                PlayList playList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.empty());

                Assertions.assertThrows(SongNotFoundException.class, () -> playListService
                                .addSongsToPlayList("1", "1", Collections.singletonList("1")));
        }

        @Test
        @DisplayName("addSongToPlayList should throw PlayList and User does Not match exception")

        public void addSongsToPlayList_shouldThrowPlayListAndUserDoesNotMatchExceptionIfTheyAreNotMatching() {
                User user = new User("1", "Amit");
                PlayList playList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                Assertions.assertThrows(PlayListAndUserDoesNotMatchException.class,
                                () -> playListService.addSongsToPlayList("1", "2",
                                                Collections.singletonList("1")));
        }

        @Test
        @DisplayName("addSongToPlayList should throw PlayList not found exception")

        public void addSongsToPlayList_shouldThrowPlayListNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                // PlayList playList = new PlayList("1", user, "PlayListName",
                // Collections.emptyList());
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                // user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                Assertions.assertThrows(PlayListNotFoundException.class, () -> playListService
                                .addSongsToPlayList("1", "1", Collections.singletonList("1")));
        }

        @Test
        @DisplayName("addSongToPlayList should throw User not found exception")

        public void addSongsToPlayList_shouldThrowUserNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                PlayList playList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                Assertions.assertThrows(UserNotFoundException.class, () -> playListService
                                .addSongsToPlayList("1", "1", Collections.singletonList("1")));
        }


        @Test
        @DisplayName("DeleteSongsFromPlayList should return PlayList")

        public void DeleteSongsFromPlayList_shouldReturnPlayList() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));

                PlayList expectedPlayList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));
                when(playListRepositoryMock.deleteFromPlayList(any(PlayList.class), any()))
                                .thenReturn(expectedPlayList);
                when(userRepositoryMock.save(any(User.class))).thenReturn(user);
                PlayList actualPlayList = playListService.DeleteSongsFromPlayList("1", "1",
                                Collections.singletonList("1"));
                Assertions.assertEquals(0, actualPlayList.getSongs().size());

        }

        @Test
        @DisplayName("DeleteSongsFromPlayList should throw Song Not Found Exception")

        public void DeleteSongsFromPlayList_shouldThrowSongNotFoundException() {
                User user = new User("1", "Amit");
                // Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                // "songname");
                PlayList playList =
                                new PlayList("1", user, "PlayListName", Collections.emptyList());
                user.setPlayList(Collections.singletonList(playList));

                // PlayList expectedPlayList = new PlayList("1", user, "PlayListName",
                // Collections.emptyList());
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.empty());
                // when(playListRepositoryMock.deleteFromPlayList(any(PlayList.class),
                // any())).thenReturn(expectedPlayList);
                // when(userRepositoryMock.save(any(User.class))).thenReturn(user);
                // PlayList actualPlayList= playListService.DeleteSongsFromPlayList("1", "1",
                // Collections.singletonList("2"));

                Assertions.assertThrows(SongNotFoundException.class, () -> playListService
                                .DeleteSongsFromPlayList("1", "1", Collections.singletonList("1")));

        }

        @Test
        @DisplayName("DeleteSongsFromPlayList should throw User Not Found Exception")

        public void DeleteSongsFromPlayList_shouldThrowUserNotFoundException() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                Assertions.assertThrows(UserNotFoundException.class, () -> playListService
                                .DeleteSongsFromPlayList("1", "1", Collections.singletonList("1")));

        }

        @Test
        @DisplayName("DeleteSongFromPlayList should throw PlayList not found exception")

        public void DeleteSongsFromPlayList_shouldThrowPlayListNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                // PlayList playList = new PlayList("1", user, "PlayListName",
                // Collections.emptyList());
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                // user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                when(songRepositoryMock.findSongById(any(String.class)))
                                .thenReturn(Optional.of(song));

                Assertions.assertThrows(PlayListNotFoundException.class, () -> playListService
                                .DeleteSongsFromPlayList("1", "1", Collections.singletonList("1")));
        }

        @Test
        @DisplayName("DeletePlayList")

        public void DeletePlayList() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                doNothing().when(playListRepositoryMock).deleteById(any(String.class));// .thenReturn(expectedPlayList);
                when(userRepositoryMock.save(any(User.class))).thenReturn(user);
                playListService.DeletePlayList("1", "1");
                Assertions.assertEquals(0, user.getPlayList().size());
        }

        @Test
        @DisplayName("DeletePlayList should throw PlayList not found exception")
        public void DeletePlayList_shouldThrowPlayListNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                assertThrows(PlayListNotFoundException.class,
                                () -> playListService.DeletePlayList("1", "1"));
        }

        @Test
        @DisplayName("DeletePlayList should throw User not found exception")
        public void DeletePlayList_shouldThrowUserNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                assertThrows(UserNotFoundException.class,
                                () -> playListService.DeletePlayList("1", "1"));
        }

        @Test
        @DisplayName("Play playList")
        public void playPlayList() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                Song currentSong = playListService.PlayPlayList("1", "1");
                assertEquals(song.getId(), currentSong.getId());
        }

        @Test
        @DisplayName("PlayPlayList should throw PlayList not found exception")
        public void PlayPlayList_shouldThrowPlayListNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.empty());
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                assertThrows(PlayListNotFoundException.class,
                                () -> playListService.PlayPlayList("1", "1"));
        }

        @Test
        @DisplayName("PlayPlayList should throw User not found exception")
        public void PlayPlayList_shouldThrowUserNotFoundExceptionIfTheyAreNotPresent() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                assertThrows(UserNotFoundException.class,
                                () -> playListService.PlayPlayList("1", "1"));
        }

        @Test
        @DisplayName("PlayPlayList should throw User and Playlist mismatch exception")
        public void PlayPlayList_shouldThrowUserAndPlayListDoNotMatchExceptionIfUserNotHasThePlayList() {
                User user = new User("1", "Amit");
                Song song = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                PlayList playList = new PlayList("1", user, "PlayListName",
                                Collections.singletonList(song));
                user.setPlayList(Collections.singletonList(playList));
                when(playListRepositoryMock.findById(any(String.class)))
                                .thenReturn(Optional.of(playList));
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                assertThrows(PlayListAndUserDoesNotMatchException.class,
                                () -> playListService.PlayPlayList("1", "2"));
        }

        @Test
        @DisplayName("PlayNextSong")
        public void PlayNextSong() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
                songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                Song currentSong = playListService.PlayNextSong("1");
                assertEquals(currentSong.getId(), song2.getId());
        }

        @Test
        @DisplayName("PlayNextSong throws user not found exception")
        public void PlayNextSong_ShouldThrowUserNotFoundExceptionIfUserIsNotPresent() {
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                assertThrows(UserNotFoundException.class, ()->playListService.PlayNextSong("1"));
        }

        @Test
        @DisplayName("PlayPreviousSong")
        public void PlayPreviousSong() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
                songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                Song currentSong = playListService.PlayPreviousSong("1");
                assertEquals(currentSong.getId(), song2.getId());
        }

        @Test
        @DisplayName("PlayPreviousSong throws user not found exception")
        public void PlayPreviousSong_ShouldThrowUserNotFoundExceptionIfUserIsNotPresent() {
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                assertThrows(UserNotFoundException.class, ()->playListService.PlayPreviousSong("1"));
        }

        @Test
        @DisplayName("PlaySong")
        public void PlaySong() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
                songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                                when(songRepositoryMock.findSongById(any(String.class))).thenReturn(Optional.of(song1));
                Song currentSong = playListService.PlaySong("1","1");
                assertEquals(currentSong,song1);
        }

        @Test
        @DisplayName("PlaySong throws Song Not Found Exception")
        public void PlaySong_shouldThrowSongNotFoundException() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
                songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                                when(songRepositoryMock.findSongById(any(String.class))).thenReturn(Optional.empty());
               // Song currentSong = playListService.PlaySong("1","1");
                assertThrows(SongNotFoundException.class, ()->playListService.PlaySong("1","1"));
        }

        @Test
        @DisplayName("PlaySong throws Song Not Found Exception")
        public void PlaySong_shouldThrowSongNotFoundExceptionInPlayList() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
               // songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.of(user));
                                when(songRepositoryMock.findSongById(any(String.class))).thenReturn(Optional.of(song2));
               // Song currentSong = playListService.PlaySong("1","1");
                assertThrows(SongNotFoundException.class, ()->playListService.PlaySong("1","2"));
        }

        @Test
        @DisplayName("PlaySong Throws User Not Found Exception")
        public void PlaySong_shouldThrowUserNotFoundException() {
                User user = new User("1", "Amit");
                Song song1 = new Song("1", "album", "owner", Collections.singletonList("artist1"),
                                "songname");
                Song song2 = new Song("2", "album2", "owner2", Collections.singletonList("artist1"),
                                "songname_2");
                List<Song> songs = new ArrayList<>();
                songs.add(song1);
                songs.add(song2);
                PlayList playList = new PlayList("1", user, "PlayListName", songs);
                user.setPlayList(Collections.singletonList(playList));
                user.setCurrentPlayList(playList);
                when(userRepositoryMock.findUserById(any(String.class)))
                                .thenReturn(Optional.empty());
                                when(songRepositoryMock.findSongById(any(String.class))).thenReturn(Optional.empty());
               // Song currentSong = playListService.PlaySong("1","1");
                assertThrows(UserNotFoundException.class, ()->playListService.PlaySong("1","1"));
        }



}
