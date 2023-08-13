package com.crio.jukebox.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.services.IPlayListService;

public class ModifyPlayListCommand implements ICommand {
    private final IPlayListService playListService;

    public ModifyPlayListCommand(IPlayListService playListService) {
        this.playListService = playListService;
    }

    @Override
    public void execute(List<String> tokens) {
        String command = tokens.get(1);
        String userId = tokens.get(2);
        String playlistId = tokens.get(3);
        List<String> songIds = new ArrayList<>();
        for (int i = 4; i < tokens.size(); i++)
            songIds.add(tokens.get(i));
        PlayList playlist = null;
        if (command.equals("ADD-SONG")) {
            playlist = playListService.addSongsToPlayList(userId, playlistId, songIds);
            
        } else if (command.equals("DELETE-SONG")) {
            playlist = playListService.DeleteSongsFromPlayList(userId, playlistId, songIds);
        }

        List<String> playListSongIds = playlist.getSongs().stream().map(Song::getId).collect(Collectors.toList());
        System.out.println("Playlist ID - " + playlist.getId());
        System.out.println("Playlist Name - " + playlist.getPlayListName());
        System.out.println("Song IDs - " + String.join(" ", playListSongIds));
    }
}
