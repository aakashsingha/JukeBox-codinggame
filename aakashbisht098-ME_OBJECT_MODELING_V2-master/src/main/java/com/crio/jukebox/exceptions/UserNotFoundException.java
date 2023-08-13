package com.crio.jukebox.exceptions;

import com.crio.jukebox.entities.User;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException()
    {
        super();
    }

    public UserNotFoundException(String msg)
    {
        super(msg);
    }
    
}
