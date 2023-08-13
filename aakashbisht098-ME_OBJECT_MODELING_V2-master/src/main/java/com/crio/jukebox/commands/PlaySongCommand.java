package com.crio.jukebox.commands;

import java.util.List;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.services.IPlayListService;

public class PlaySongCommand implements ICommand{
    private final IPlayListService playListService;

    public PlaySongCommand(IPlayListService playListService) {
        this.playListService = playListService;
    }

    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String commandOrSongId = tokens.get(2);
        Song song = null;
        try{
            if (commandOrSongId.equals("BACK")) {
                song = playListService.PlayPreviousSong(userId);
            } else if (commandOrSongId.equals("NEXT")) {
                song = playListService.PlayNextSong(userId);
            } else {
                song = playListService.PlaySong(userId, commandOrSongId);
            }
            System.out.println("Current Song Playing");
            System.out.println("Song - " + song.getSongname());
            System.out.println("Album - " + song.getAlbum());
            System.out.println("Artists - " + String.join(",", song.getArtists()));
        } catch(SongNotFoundException e){
            System.out.println(e.getMessage());
        }   
    }
}
