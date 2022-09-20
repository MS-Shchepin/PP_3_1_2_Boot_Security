package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.roles.Roles;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final List<Role> DEFAULT_NEW_USER_PERMITS = List.of(Roles.USER.role());
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public int save(User user) {
        if (!user.getPassword().equals(user.getConfirmPassword())){
            return ERROR_SAVE_PASSWORDS_DONT_MATCH;
        }
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null && user.getId() != userFromDb.getId()) {
            return ERROR_SAVE_USERNAME_TAKEN;
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(DEFAULT_NEW_USER_PERMITS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return SAVE_SUCCESS;
    }

    @Transactional
    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User with %s not found".formatted(username));
        return user;
    }

    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
