package com.crio.jukebox.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.util.ElementScanner6;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.exceptions.PlayListAndUserDoesNotMatchException;
import com.crio.jukebox.exceptions.PlayListNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.repositories.IPlayListRepository;
import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.IUserRepository;
import com.crio.jukebox.repositories.UserRepository;

public class PlayListService implements IPlayListService {

   private final IUserRepository userRepository;
   private final ISongService songService;
   private final IPlayListRepository playlistRepository;
   private final ISongRepository songRepository;

   public PlayListService(IUserRepository userRepository, ISongService songService,
         IPlayListRepository playlistRepository, ISongRepository songRepository) {
      this.userRepository = userRepository;
      this.songService = songService;
      this.playlistRepository = playlistRepository;
      this.songRepository = songRepository;
   }

   @Override
   public PlayList create(String userId, String playlistName, List<String> songIds) {
      Optional<User> optionalUser = userRepository.findUserById(userId);
      PlayList p = null;
      List<Song> songs = new ArrayList<>();
      for (String itr : songIds) {
         Optional<Song> s = songRepository.findSongById(itr);
         if (!s.isPresent()) {
            throw new SongNotFoundException(
                  "Some Requested Songs Are Not Available. Please try again.");
         } else {
            songs.add(s.get());
         }
      }
      if (optionalUser.isPresent()) {
         User user = optionalUser.get();
         p = new PlayList(Integer.toString(playlistRepository.getLength() + 1), user, playlistName,
               songs);
         List<PlayList> userPlayLists = user.getPlayList();
         userPlayLists.add(p);
         user.setPlayList(userPlayLists);
         userRepository.save(user);
         playlistRepository.save(p);
      } else {
         throw new UserNotFoundException("User Not found");
      }
      return p;
   }

   @Override
   public PlayList addSongsToPlayList(String userId, String playListId, List<String> songIds) {

      PlayList playListToUpdate = null;
      Optional<PlayList> a = playlistRepository.findById(playListId);
      Optional<User> u = userRepository.findUserById(userId);
      List<Song> ls = new ArrayList<>();
      for (String itr : songIds) {
         Optional<Song> s = songRepository.findSongById(itr);
         if (!s.isPresent()) {
            throw new SongNotFoundException(
                  "Some Requested Songs Not Available. Please try again.");
         } else {
            ls.add(s.get());
         }
      }
      if (a.isPresent() && u.isPresent()) {
         playListToUpdate = a.get();
         User userToUpdate = u.get();
         final PlayList finalPlayListToUpdate = playListToUpdate;
         List<PlayList> lp = userToUpdate.getPlayList();
         Boolean isMatching = lp.stream().anyMatch(p -> p.getId().equals(playListId));
         if (isMatching) {
            // filter songs to add as a song in toAddList can already be present over there
            List<Song> songsToAdd =
                  ls.stream()
                        .filter(s -> !finalPlayListToUpdate.getSongs().stream()
                              .anyMatch(ps -> ps.getId().equals(s.getId())))
                        .collect(Collectors.toList());
            PlayList updatedPlayList =
                  playlistRepository.addToPlayList(playListToUpdate, songsToAdd);
            List<PlayList> userPlayList = userToUpdate.getPlayList();
            List<PlayList> updatedUserPlayList = userPlayList.stream().map(p -> {
               if (p.getId().equals(playListId)) {
                  return updatedPlayList;
               }
               return p;
            }).collect(Collectors.toList());
            userToUpdate.setPlayList(updatedUserPlayList);
            userRepository.save(userToUpdate);
            return updatedPlayList;
         } else {
            throw new PlayListAndUserDoesNotMatchException();
         }
      } else if (!a.isPresent()) {
         throw new PlayListNotFoundException("Play List Not Found");
      } else if (!u.isPresent()) {
         throw new UserNotFoundException("User Not found");
      }
      return playListToUpdate;
   }

   @Override
   public PlayList DeleteSongsFromPlayList(String userId, String playListId,
         final List<String> songIds) {
      PlayList playListToUpdate = null;
      Optional<PlayList> a = playlistRepository.findById(playListId);
      Optional<User> u = userRepository.findUserById(userId);
      List<Song> ls = new ArrayList<>();
      for (String itr : songIds) {
         Optional<Song> s = songRepository.findSongById(itr);
         if (!s.isPresent()) {
            throw new SongNotFoundException(
                  "Some Requested Songs Not Available. Please try again.");
         } else {
            ls.add(s.get());
         }
      }
      if (a.isPresent() && u.isPresent()) {
         playListToUpdate = a.get();
         User userToUpdate = u.get();
         final PlayList finalPlayListToUpdate = playListToUpdate;
         List<PlayList> lp = userToUpdate.getPlayList();
         Boolean isMatching = lp.stream().anyMatch(p -> p.getId().equals(playListId));
         if (isMatching) {
            // filter songs to add as a song in toAddList can already be present over there
            Long songsLen = ls.stream().filter(s -> finalPlayListToUpdate.getSongs().stream()
                  .anyMatch(ps -> ps.getId().equals(s.getId()))).count();

            if (songsLen == ls.size()) {
               PlayList updatedPlayList =
                     playlistRepository.deleteFromPlayList(playListToUpdate, ls);
               List<PlayList> userPlayList = userToUpdate.getPlayList();
               List<PlayList> updatedUserPlayList = userPlayList.stream().map(p -> {
                  if (p.getId().equals(updatedPlayList.getId())) {
                     return updatedPlayList;
                  }
                  return p;
               }).collect(Collectors.toList());
               userToUpdate.setPlayList(updatedUserPlayList);
               userRepository.save(userToUpdate);
               return updatedPlayList;
            } else {
               throw new SongNotFoundException(
                     "Some Requested Songs Not Available. Please try again.");
            }
         } else {
            throw new PlayListAndUserDoesNotMatchException();
         }
      } else if (!a.isPresent()) {
         throw new PlayListNotFoundException("Play List Not Found");
      } else if (!u.isPresent()) {
         throw new UserNotFoundException("User Not found");
      }
      return playListToUpdate;
   }

   @Override
   public void DeletePlayList(String userId, String playListId) {
      Optional<PlayList> optionalPlayList = playlistRepository.findById(playListId);
      Optional<User> optionalUser = userRepository.findUserById(userId);

      if (optionalPlayList.isPresent() && optionalUser.isPresent()) {

         playlistRepository.deleteById(playListId);
         User userToUpdate = optionalUser.get();
         List<PlayList> userPlayList = userToUpdate.getPlayList();
         List<PlayList> updatedUserPlayLists = userPlayList.stream()
               .filter(up -> !up.getId().equals(playListId)).collect(Collectors.toList());
         userToUpdate.setPlayList(updatedUserPlayLists);
         userRepository.save(userToUpdate);

      } else if (!optionalPlayList.isPresent()) {
         throw new PlayListNotFoundException("Play List Not Found");
      } else if (!optionalUser.isPresent()) {
         throw new UserNotFoundException("User Not found");
      }
   }

   @Override
   public Song PlayPlayList(String userId, String playListId) {
      Optional<PlayList> a = playlistRepository.findById(playListId);
      Optional<User> u = userRepository.findUserById(userId);

      if (a.isPresent() && u.isPresent()) {
         PlayList playListToPlay = a.get();
         User userToPlay = u.get();
         List<PlayList> lp = userToPlay.getPlayList();
         Boolean b = lp.stream().anyMatch(p -> p.getId().equals(playListId));
         if (b) {
            userToPlay.setCurrentPlayList(playListToPlay);
            List<Song> songs = playListToPlay.getSongs();
            return songs.get(0);
         } else {
            throw new PlayListAndUserDoesNotMatchException();
         }
      } else if (!a.isPresent()) {
         throw new PlayListNotFoundException();
      } else {
         throw new UserNotFoundException();
      }
   }

   @Override
   public Song PlayNextSong(String userId) {
      Optional<User> u = userRepository.findUserById(userId);
      if (u.isPresent()) {
         User userToPlay = u.get();
         PlayList playListToPlay = userToPlay.getCurrentPlayList();
         Song songToPlay = playListToPlay.getNextSong();
         return songToPlay;
      } else {
         throw new UserNotFoundException();
      }
   }

   @Override
   public Song PlayPreviousSong(String userId) {
      Optional<User> u = userRepository.findUserById(userId);
      if (u.isPresent()) {
         User userToPlay = u.get();
         PlayList playListToPlay = userToPlay.getCurrentPlayList();
         Song songToPlay = playListToPlay.getPreviousSong();
         return songToPlay;
      } else {
         throw new UserNotFoundException();
      }
   }

   @Override
   public Song PlaySong(String userId, String songId) {
      Optional<User> u = userRepository.findUserById(userId);
      Optional<Song> song = songRepository.findSongById(songId);
      if (u.isPresent() && song.isPresent()) {
         User userToPlay = u.get();
         PlayList playListToPlay = userToPlay.getCurrentPlayList();
         Optional<Song> optionalSong = playListToPlay.getSongById(songId);
         if (optionalSong.isPresent()) {
            return optionalSong.get();
         } else {
            throw new SongNotFoundException("Given song id is not a part of the active playlist");
         }
      } else if (!u.isPresent()) {
         throw new UserNotFoundException();
      } else {
         throw new SongNotFoundException();
      }
   }

}
