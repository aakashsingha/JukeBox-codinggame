package com.crio.jukebox.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.crio.jukebox.services.ISongService;

public class LoadDataCommand implements ICommand{
    private final ISongService songService;
    
    public LoadDataCommand(ISongService songService) {
        this.songService = songService;
    }

    @Override
    public void execute(List<String> tokens) {
        String filename = tokens.get(1);

        // process each row of file
        try
        {
            BufferedReader brdrd = new BufferedReader(new FileReader("songs.csv"));
            String mystring = "";
            while ((mystring = brdrd.readLine()) != null)  
            {
                String[] tkns = mystring.split(",");
                String songName = tkns[0];
                String albumName = tkns[2];
                String owner = tkns[3];
                List<String> artists = Arrays.asList(tkns[4].split("#"));
                songService.create(albumName, owner, artists, songName);  
            }
            System.out.println("Songs Loaded successfully");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
