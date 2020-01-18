package com.registaration2.registration2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.registaration2.registration2.entity.User;
import com.registaration2.registration2.service.RegistrationService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/registration")
    public ModelAndView getRegistration()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/createUser")
    public ModelAndView postRegistration(@ModelAttribute("user") User user)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/registration");

        //Я бы тут добавил бы проверку, что юзера с таким, к примеру, именем и фамилией нету в БД

        registrationService.createUser(user);
        return modelAndView;
    }


}
