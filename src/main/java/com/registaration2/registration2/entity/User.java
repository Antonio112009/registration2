package com.registaration2.registration2.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "seq_user", allocationSize = 1, initialValue = 1)
    private Long ID_USER;

    @Column(name="External_ID")
    private Long External_ID;

    @Column(name="FirstName")
    private String FirstName;

    @Column(name="SecondName")
    private String SecondName;

    @Column(name="Email")
    private String Email;

    @Column(name="Login")
    private String Login;

    @Column(name="Password")
    private String Password;

    @Column(name="Phone")
    private String Phone;

    @Column(name="Photo")
    private String Photo;

    @Column(name="ID_Role")
    private Long ID_Role;

    public User() {
    }

}
