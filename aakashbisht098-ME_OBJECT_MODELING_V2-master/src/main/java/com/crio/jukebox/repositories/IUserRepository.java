package com.crio.jukebox.repositories;

import java.util.Optional;
import com.crio.jukebox.entities.User;

public interface IUserRepository {
    public User save(User u);
    public <T> Optional<T> findUserById(String id);
    public int getLength();
}
