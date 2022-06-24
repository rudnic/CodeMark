package com.example.beginningsoap.services;

import com.example.beginningsoap.ModifyUserResponse;
import com.example.beginningsoap.models.User;
import com.example.beginningsoap.repository.DatabaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final DatabaseUserRepository userRepository;

    public UserServiceImpl(DatabaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isCorrectPassword(String login, String password) {
        return password.equals(userRepository.getUserByLogin(login).getPassword());
    }

    @Override
    public boolean isExistUser(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public boolean isEmptyUser(String login) {
        return userRepository.findByLogin(login).isEmpty();
    }

    @Override
    public ModifyUserResponse checkUserCredentials(String login, String password) {
        ModifyUserResponse response = new ModifyUserResponse();

        /* Проверка, есть ли пользователь с таким логином */
        if (isEmptyUser(login)) {
            response.setSuccess(false);
            response.setError("User with this username not found");

            return response;
        }

        /* Проверка на пароль пользователя */
        if (!isCorrectPassword(login, password)) {
            response.setSuccess(false);
            response.setError("Incorrect password");

            return response;
        }

        response.setSuccess(true);
        return response;
    }
}
