package com.crio.jukebox.commands;

import java.util.ArrayList;
import java.util.List;
import com.crio.jukebox.entities.PlayList;
import com.crio.jukebox.services.IPlayListService;

public class CreatePlayListCommand  implements ICommand{
    private final IPlayListService playListService;
    
    public CreatePlayListCommand(IPlayListService playListService) {
        this.playListService = playListService;
    }
    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String playlistName = tokens.get(2);
        List<String> songIds = new ArrayList<>();
        for(int i = 3; i < tokens.size(); i++)
            songIds.add(tokens.get(i));
        PlayList playlist = playListService.create(userId, playlistName, songIds);
        System.out.println("Playlist ID - " + playlist.getId());
    }
    
}
