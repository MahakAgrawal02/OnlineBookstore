package com.project.service;

import javax.servlet.http.HttpSession;

import com.project.model.StoreException;
import com.project.model.User;
import com.project.model.UserRole;

public interface UserService {

    public User login(UserRole role, String email, String password, HttpSession session) throws StoreException;

    public String register(UserRole role, User user) throws StoreException;

    public boolean isLoggedIn(UserRole role, HttpSession session);

    public boolean logout(HttpSession session);

}
