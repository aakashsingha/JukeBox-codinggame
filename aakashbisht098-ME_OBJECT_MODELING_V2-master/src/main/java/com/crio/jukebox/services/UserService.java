package com.crio.jukebox.services;

import com.crio.jukebox.entities.User;
import com.crio.jukebox.repositories.IUserRepository;

public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository)
    {
        this.userRepository=userRepository;
    }
    
    @Override
    public User create(String name)
    {
        User u = new User(Integer.toString(userRepository.getLength() + 1), name);
        userRepository.save(u);
        return u;
    }
    
}
