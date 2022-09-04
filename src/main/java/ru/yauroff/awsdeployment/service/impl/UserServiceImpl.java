package ru.yauroff.awsdeployment.service.impl;

import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.model.User;
import ru.yauroff.awsdeployment.repository.UserRepository;
import ru.yauroff.awsdeployment.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                             .orElse(null);
    }

    @Override
    public User findByLoginOrEmail(String identifier) {
        return userRepository.findByLoginOrEmail(identifier, identifier)
                             .orElse(null);
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);

    }
}
