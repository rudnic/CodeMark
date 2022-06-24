package com.example.beginningsoap.endpoint;

import com.example.beginningsoap.*;
import com.example.beginningsoap.models.User;

import com.example.beginningsoap.repository.DatabaseRoleRepository;
import com.example.beginningsoap.repository.DatabaseUserRepository;
import com.example.beginningsoap.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://beginningsoap.example.com";

    private final UserService userService;

    private final DatabaseUserRepository userRepository;

    private final DatabaseRoleRepository roleRepository;

    public UserEndpoint(DatabaseUserRepository userRepository, DatabaseRoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserByLoginRequest request) {
        GetUserResponse response = new GetUserResponse();
        com.example.beginningsoap.User soapUser = new com.example.beginningsoap.User();
        BeanUtils.copyProperties(userService.getUserByLogin(request.getLogin()), soapUser);
        response.setUser(soapUser);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByIdRequest")
    @ResponsePayload
    public GetUserResponse getUserById(@RequestPayload GetUserByIdRequest request) {
        GetUserResponse response = new GetUserResponse();

        /* Создание объекта из сгенерированного класса */
        com.example.beginningsoap.User soapUser = new com.example.beginningsoap.User();

        /* Берём пользователя из БД */
        User dbUser = userService.getUserById(request.getTargetId());

        /* Копирование в созданный класс свойств из объекта БД */
        BeanUtils.copyProperties(dbUser, soapUser);
        response.setUser(soapUser);

        /* Также клонируем список ролей */
        dbUser.getRoles().forEach((el) -> {
            Role soapRole = new Role();
            BeanUtils.copyProperties(el, soapRole);
            soapUser.getRoles().add(soapRole);
        });

        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers(@RequestPayload GetAllUsersRequest request) {
        GetAllUsersResponse response = new GetAllUsersResponse();

        /* Берём всех пользователей из БД */
        List<User> dbUsers = userService.getAllUsers();

        /* Создаем список из обектов сгенерированного класса */
        List<com.example.beginningsoap.User> soapUsersList = new LinkedList<>();

        dbUsers.forEach((el) -> {
            /* Копируем свойства */
            com.example.beginningsoap.User soapUser = new com.example.beginningsoap.User();
            BeanUtils.copyProperties(el, soapUser);

            /* Также клонируем список ролей */
            el.getRoles().forEach((_el) -> {
                Role soapRole = new Role();
                BeanUtils.copyProperties(_el, soapRole);
                soapUser.getRoles().add(soapRole);
            });
            soapUsersList.add(soapUser);

        });
        response.getUser().addAll(soapUsersList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public ModifyUserResponse addUser(@RequestPayload AddUserRequest request) {

        ModifyUserResponse response;


/*        if (userService.isEmptyUser(request.getLogin())) {
            response.setSuccess(false);
            response.setError("User with this username not found");

            return response;
        }

        *//* Проверка на пароль пользователя *//*
        if (!userService.isCorrectPassword(request.getLogin(), request.getPassword())) {
            response.setSuccess(false);
            response.setError("Incorrect password");

            return response;
        }*/

        /* Проверка, есть ли пользователь с таким логином и правильный ли пароль*/
        response = userService.checkUserCredentials(request.getLogin(), request.getPassword());
        if (!response.isSuccess()) {
            return response;
        }


        /* Проверка на существование пользователя с заданным логином */
        if (userService.isExistUser(request.getUser().getLogin())) {
            response.setSuccess(false);
            response.setError("User with this username already exist");

            return response;
        }


        /* Валидация пароля с помощью регулярки */
        if (!request.getUser().getPassword().matches("^.*(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$")) {
            response.setSuccess(false);
            response.setError("The password does not meet the requirements");

            return response;
        }

        /* BeanUtils не копирует атрибуты из soap пользователя в сущность, поэтому работаем ручками =) */
        com.example.beginningsoap.User soapNewUser = request.getUser();
        User newUser = new User();
        // BeanUtils.copyProperties(soapNewUser, user);
        newUser.setLogin(soapNewUser.getLogin());
        newUser.setName(soapNewUser.getName());
        newUser.setPassword(soapNewUser.getPassword());
        newUser.setRoles(new HashSet<>());

        /* Также не получится просто так назначить роли
            Так, как классы разные
        */
        soapNewUser.getRoles().forEach((el) -> {
            com.example.beginningsoap.models.Role role = roleRepository.getRoleById(el.getId());
            newUser.getRoles().add(role);
        });

        userService.saveUser(newUser);
        response.setSuccess(true);
        response.setError(null);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "modifyUserRequest")
    @ResponsePayload
    public ModifyUserResponse modifyUser(@RequestPayload ModifyUserRequest request) {

        ModifyUserResponse response;

        /* Проверка, есть ли пользователь с таким логином */
/*        if (userService.isEmptyUser(request.getLogin())) {
            response.setSuccess(false);
            response.setError("User with this username not found");

            return response;
        }

        *//* Проверка на пароль пользователя *//*
        if (!userService.isCorrectPassword(request.getLogin(), request.getPassword())) {
            response.setSuccess(false);
            response.setError("Incorrect password");

            return response;
        }*/

        // TODO: модификация
        /* Проверка, есть ли пользователь с таким логином и правильный ли пароль*/
        response = userService.checkUserCredentials(request.getLogin(), request.getPassword());
        if (!response.isSuccess()) {
            return response;
        }

        response = userService.checkUserCredentials(request.getLogin(), request.getPassword());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserByIdRequest")
    @ResponsePayload
    public ModifyUserResponse deleteUserById(@RequestPayload DeleteUserByIdRequest request) {
        ModifyUserResponse response;

        response = userService.checkUserCredentials(request.getLogin(), request.getPassword());
        if (!response.isSuccess()) {
            return response;
        }

        // TODO: удаление
        return null;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserByLoginRequest")
    @ResponsePayload
    public ModifyUserResponse deleteUserByLogin(@RequestPayload DeleteUserByLoginRequest request) {

        return null;
    }
}
