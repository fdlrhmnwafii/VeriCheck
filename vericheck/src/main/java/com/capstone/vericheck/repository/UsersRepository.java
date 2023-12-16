package com.capstone.vericheck.repository;

import com.capstone.vericheck.model.UsersModel;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {
    Optional<UsersModel> findByUsernameAndPassword(String username, String password);
    Optional<UsersModel> findFirstByUsername(String username);

}
