package com.crio.codingame.commands;

import java.util.List;
import javax.lang.model.util.ElementScanner6;
import com.crio.codingame.entities.Contest;
import com.crio.codingame.entities.Level;
import com.crio.codingame.services.IContestService;

public class ListContestCommand implements ICommand{

    private final IContestService contestService;
    
    public ListContestCommand(IContestService contestService) {
        this.contestService = contestService;
    }

    // TODO: CRIO_TASK_MODULE_CONTROLLER
    // Execute getAllContestLevelWise method of IContestService and print the result.
    // Look for the unit tests to see the expected output.
    // Sample Input Token List:- ["LIST_CONTEST","HIGH"]
    // or
    // ["LIST_CONTEST"]

    @Override
    public void execute(List<String> tokens) {
        
        String level = tokens.size()==2?tokens.get(1):"";
        List<Contest> l;
        if(level.equals("HIGH"))
        {
         l=contestService.getAllContestLevelWise(Level.HIGH);
        }
        else
        if(level.equals("LOW"))
        {
            l=contestService.getAllContestLevelWise(
                Level.LOW
            );
        }
        else
        if(level.equals("MEDIUM"))
        {
            l= contestService.getAllContestLevelWise(Level.MEDIUM);
        }
        else{
         l=contestService.getAllContestLevelWise(null);   
        }
       System.out.println(l);
    }
}
