package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean save(Role role) {
        if (role == null || role.getName() == null) {
            System.out.println("Save role failed: incorrect data");
            return false;
        }
        if (roleRepository.findByName(role.getName()).isEmpty()) {
            roleRepository.save(role);
            System.out.println(role.getName() + " saved");
            return true;
        } else {
            System.out.println(role.getName() + " exists");
        }
        return false;
    }

    @Override
    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean deleteById(long id) {
        if (roleRepository.findById(id).isPresent()) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
