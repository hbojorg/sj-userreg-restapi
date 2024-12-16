package com.smartjob.userreg.application;

import com.smartjob.userreg.domain.UserSJ;
import com.smartjob.userreg.infrastructure.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserSJ> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserSJ> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserSJ create(UserSJ user) {
        return userRepository.save(user);
    }

    public UserSJ update(UserSJ user) {
        return userRepository.save(user);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

}
