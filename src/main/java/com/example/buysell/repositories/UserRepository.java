package com.example.buysell.repositories;

import com.example.buysell.models.UserPackage.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByName(String name);
    User findById(long idUser);
}
