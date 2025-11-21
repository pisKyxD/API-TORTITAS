package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {

            if (rolRepository.count() == 0) {

                Rol usuario = new Rol();
                usuario.setNombre("USUARIO");

                Rol admin = new Rol();
                admin.setNombre("ADMIN");

                rolRepository.save(usuario);
                rolRepository.save(admin);

                System.out.println("Roles iniciales creados correctamente.");
            } else {
                System.out.println("Roles ya existen, no se crearán datos iniciales.");
            }
        };
    }
}
