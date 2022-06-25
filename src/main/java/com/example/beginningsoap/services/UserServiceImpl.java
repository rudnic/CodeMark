package com.example.beginningsoap.services;

import com.example.beginningsoap.ModifyUserResponse;
import com.example.beginningsoap.models.Role;
import com.example.beginningsoap.models.User;
import com.example.beginningsoap.repository.DatabaseUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
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

    @Override
    public void modifyUser(com.example.beginningsoap.User soapUser) {
        User user = getUserByLogin(soapUser.getLogin());
        soapUser.setId(user.getId());
        BeanUtils.copyProperties(soapUser, user);

        soapUser.getRoles().forEach((el) -> {
            Role role = new Role();
            BeanUtils.copyProperties(el, role);
            user.getRoles().add(role);
        });

        saveUser(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteUserByLogin(String login) {
        userRepository.deleteUserByLogin(login);
    }

    @Override
    public void addUser(com.example.beginningsoap.User soapUser) {
        User newUser = new User();
        // BeanUtils.copyProperties(soapNewUser, user);
        newUser.setLogin(soapUser.getLogin());
        newUser.setName(soapUser.getName());
        newUser.setPassword(soapUser.getPassword());
        newUser.setRoles(new HashSet<>());

        /* Также не получится просто так назначить роли
            Так, как классы разные
        */
        soapUser.getRoles().forEach((el) -> {
            // com.example.beginningsoap.models.Role role = roleRepository.getRoleById(el.getId());
            com.example.beginningsoap.models.Role role = new com.example.beginningsoap.models.Role();
            BeanUtils.copyProperties(el, role);
            newUser.getRoles().add(role);
        });

        saveUser(newUser);
    }
}
