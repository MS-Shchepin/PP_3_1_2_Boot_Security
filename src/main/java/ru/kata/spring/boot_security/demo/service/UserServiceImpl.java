package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int save(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb.isPresent() && user.getId() != userFromDb.get().getId()) {
            return ERROR_SAVE_USERNAME_TAKEN;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getId() == 0) {
            user.getRoles().forEach(roleService::save);
        }
        userRepository.save(user);
        return SAVE_SUCCESS;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User with %s not found".formatted(username)));
    }
}
