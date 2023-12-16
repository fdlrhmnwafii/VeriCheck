package com.capstone.vericheck.service;

import com.capstone.vericheck.model.UsersModel;
import com.capstone.vericheck.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }

    public UsersModel registerUser(String username, String password, String email){
        if(username == null || password == null){
            return null;
        }else {
            if(usersRepository.findFirstByUsername(username).isPresent()){
                System.out.println("Duplicate login");
                return null;
            }
            UsersModel usersModel = new UsersModel();
            usersModel.setUsername(username);
            usersModel.setPassword(passwordEncoder.encode(password));
            usersModel.setEmail(email);

            String tokenLogin = generateLoginToken(usersModel.getUsername());
            System.out.println(generateLoginToken(usersModel.getUsername()));

            usersModel.setLoginToken(tokenLogin);

            return usersRepository.save(usersModel);
        }
    }

    public boolean authenticate(String username, String password){
        Optional<UsersModel> userOptional = usersRepository.findFirstByUsername(username);
        if(userOptional.isPresent()){
            UsersModel usersModel = userOptional.get();
            return passwordEncoder.matches(password, usersModel.getPassword());
        }

        return false;
    }

    public String generateLoginToken(String username) {
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000)) // 10 days
                .signWith(key)
                .compact();
    }
}
