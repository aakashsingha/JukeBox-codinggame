package com.crio.jukebox.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.repositories.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@DisplayName("UserServiceTest")
@ExtendWith(MockitoExtension.class)

public class UserServiceTest {
    @Mock
    private IUserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    @DisplayName("Create should create User")
    @Test
    public void create_ShouldReturnUser()
    {
        User expectedUser= new User("1","Amit");
        when(userRepositoryMock.save(any(User.class))).thenReturn(expectedUser);

        User actualUser = userService.create("Amit");

        Assertions.assertEquals(expectedUser.getId(),actualUser.getId());
        Assertions.assertEquals(expectedUser.getName(),actualUser.getName());
        verify(userRepositoryMock,times(1)).save(any(User.class));
    }
}
