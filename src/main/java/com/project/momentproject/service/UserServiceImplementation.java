package com.project.momentproject.service;

import com.project.momentproject.repository.UserRepository;
import com.project.momentproject.usermodel.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Импортируйте это
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository; // Сделайте его final для неизменяемости

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username); // Получите Optional

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с email: " + username)); // Обработайте случай, когда пользователь не существует

        System.out.println("Загруженный пользователь: " + user.getEmail() + ", Роль: " + user.getRole());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole())); // Предполагая, что user.getRole() возвращает роль в виде String
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}