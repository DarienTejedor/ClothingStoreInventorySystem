package com.darientejedor.demo.security;

import com.darientejedor.demo.domain.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        return userRepository.findByLoginUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado " + user)) ;
    }
}



//Implementa la interfaz UserDetailsService, que Spring Security usa para obtener los datos de un usuario como el login
//userRepository.findByLoginUser(user) busca al usuario en la base de datos usando el repositorio y su nombre de usuario (user).