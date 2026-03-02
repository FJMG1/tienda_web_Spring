package com.tienda.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tienda.dto.RegisterDTO;
import com.tienda.model.User;
import com.tienda.model.User.Rol;
import com.tienda.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================
    //  BÚSQUEDAS
    // ============================

    public User findByName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ============================
    //  LOGIN (Spring Security)
    // ============================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario: " + username));
    }

    // ============================
    //  REGISTRO
    // ============================

    public void register(RegisterDTO dto) {

        User user = new User();
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRol(User.Rol.COMPRADOR);

        userRepository.save(user);
    }

    // ============================
    //  GUARDAR
    // ============================

    public void save(User user) {
        userRepository.save(user);
    }

    // ============================
    //  PAGINACIÓN
    // ============================

    public Page<User> getAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    // ============================
    //  ROLES
    // ============================

    public void updateRole(int id, Rol rol) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRol(rol);
        userRepository.save(user);
    }

    // ============================
    //  USUARIO ACTUAL
    // ============================

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
}