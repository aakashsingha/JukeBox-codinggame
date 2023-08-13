package com.crio.jukebox.commands;

import java.util.List;
import com.crio.jukebox.services.IPlayListService;

public class DeletePlayListCommand implements ICommand{
    private final IPlayListService playListService;
    
    public DeletePlayListCommand(IPlayListService playListService) {
        this.playListService = playListService;
    }
    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String playlistId = tokens.get(2);
        playListService.DeletePlayList(userId, playlistId);
        System.out.println("Delete Successful");
    }    
}
