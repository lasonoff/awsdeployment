package ru.yauroff.awsdeployment.service;

import ru.yauroff.awsdeployment.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User getByLogin(String login);

    long getCount();

    User create(User user);

    User update(User user);

    void deleteById(Long id);
}
