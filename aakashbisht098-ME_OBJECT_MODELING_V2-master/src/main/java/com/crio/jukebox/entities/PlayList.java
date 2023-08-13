package com.crio.jukebox.entities;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PlayList extends BaseEntity{
    private List<Song> songs;
    String playListName;
    public String getPlayListName() {
        return playListName;
    }

    public PlayList()
    {
        
    }

    User user;
    int currentSongIndex;
    public PlayList(String id,User user,String name, List<Song> songs)
    {
       setId(id);
       this.user=user;
       this.playListName=name;
       this.songs=songs;
    }
    public List<Song> getSongs() {
        return this.songs;
    }
    public void addSongs(List<Song> songsToAdd) {
        this.songs.addAll(songsToAdd);
    }
    
    public void setSongs(List<Song> songs)
    {
        this.songs=songs;
    }

    public Song getNextSong()
    {
        this.currentSongIndex=(this.currentSongIndex+1)%songs.size();
       return songs.get(currentSongIndex);
    }

    public Song getPreviousSong()
    {
        this.currentSongIndex=(songs.size() + this.currentSongIndex-1)%songs.size();
       return songs.get(currentSongIndex);
    }

    public Optional<Song> getSongById(String songId)
    {
        for(int i = 0; i < songs.size(); i++){
            if(songs.get(i).getId().equals(songId)){
                this.currentSongIndex = i;
                return Optional.ofNullable(songs.get(i));
            }
        }
        return Optional.empty();
    }
}
