package com.crio.jukebox.commands;

import java.util.List;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.services.IPlayListService;

public class PlayPlayListCommand implements ICommand{
    private final IPlayListService playListService;

    public PlayPlayListCommand(IPlayListService playListService) {
        this.playListService = playListService;
    }

    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String playlistId = tokens.get(2);
       
        Song song = playListService.PlayPlayList(userId, playlistId);
        
        // "Song - South of the Border\n"+
        // "Album - No.6 Collaborations Project\n"+
        // "Artists - Ed Sheeran,Cardi.B,Camilla Cabello\n"+
        System.out.println("Current Song Playing");
        System.out.println("Song - " + song.getSongname());
        System.out.println("Album - " + song.getAlbum());
        System.out.println("Artists - " + String.join(",", song.getArtists()));
    }
}
