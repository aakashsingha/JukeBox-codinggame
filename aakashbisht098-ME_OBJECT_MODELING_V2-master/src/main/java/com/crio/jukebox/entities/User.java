package com.crio.jukebox.entities;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity{

    String username;
    List<PlayList> playlist;
    PlayList currentPlayList;

    public User(String id,String name)
    {
        setId(id);
        this.username=name;
        this.playlist = new ArrayList<>();
    }

    public String getName()
    {
       return this.username;
    }

    public List<PlayList> getPlayList()
    {
        return this.playlist;
    }
    public void setPlayList(List<PlayList> playlists) {
        this.playlist = playlists;
    }

    public PlayList getCurrentPlayList()
    {
        return this.currentPlayList;
    }

    public void setCurrentPlayList(PlayList currentPlayList)
    {
        this.currentPlayList= currentPlayList;
    }
}
