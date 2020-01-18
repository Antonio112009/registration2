package com.registaration2.registration2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.registaration2.registration2.entity.User;
import com.registaration2.registration2.repository.UsersRepository;

@Service
public class RegistrationService {

    @Autowired
    UsersRepository usersRepository;

    public void createUser(User user)
    {
        usersRepository.save(user);

    }


}
