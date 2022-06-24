package com.example.beginningsoap.services;

import com.example.beginningsoap.ModifyUserResponse;
import com.example.beginningsoap.models.User;

import java.util.List;

public interface UserService {

    public boolean isCorrectPassword(String login, String password);
    public boolean isExistUser(String login);
    public boolean isEmptyUser(String login);
    public ModifyUserResponse checkUserCredentials(String login, String password);
    public User getUserById(Long id);
    public List<User> getAllUsers();
    public User getUserByLogin(String login);
    public void saveUser(User user);
}
