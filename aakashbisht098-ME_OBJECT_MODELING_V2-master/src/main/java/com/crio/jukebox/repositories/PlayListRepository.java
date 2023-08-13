package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;

public class PlayListRepository implements IPlayListRepository {
    
    private Map<String,PlayList> playlistmap;

    public PlayListRepository() {
        this.playlistmap = new HashMap<>();
    }

    @Override
    public PlayList save(PlayList entity)
    {
        playlistmap.put(entity.getId(),entity);
        return entity;
    }
    
    @Override
    public int getLength()
    {
        return playlistmap.size();
    }

    @Override
    public Optional<PlayList> findById(String id)
    {
      return  Optional.ofNullable(playlistmap.get(id));
    }

    @Override 
    public PlayList addToPlayList(PlayList playList, List<Song> songsToAdd) {
        playList.addSongs(songsToAdd);
        return save(playList);
    }

    @Override
    public void deleteById(String playListId) {
        playlistmap.remove(playListId);
    }

    @Override
    public PlayList deleteFromPlayList(PlayList playList,List<Song> songsToDelete)
    {
       List<Song> songs= playList.getSongs().stream().filter(p->!songsToDelete.stream().anyMatch(s->s.getId().equals(p.getId()))).collect(Collectors.toList());
       playList.setSongs(songs);
       return save(playList);
    }
}
