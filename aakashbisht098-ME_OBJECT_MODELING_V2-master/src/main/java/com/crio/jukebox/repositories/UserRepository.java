package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.crio.jukebox.entities.User;

public class UserRepository implements IUserRepository{
    
    private Map<String,User> usermap;
    private Integer autoIncrement=0;

    public UserRepository()
    {
        usermap=new HashMap<String,User>();
    }

    public UserRepository(Map<String,User> usermap)
    {
        this.usermap=usermap;
        this.autoIncrement=usermap.size();
    } 
    @Override
    public User save(User entity)
    {
        usermap.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public Optional<User> findUserById(String id)
    {
        return Optional.ofNullable(usermap.get(id));
    }

    @Override
    public int getLength(){
        return usermap.size();
    }
}
