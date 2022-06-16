package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;

public interface UserService {

    User[] findAll(AuthenticatedUser authenticatedUser);

    User findByUsername(AuthenticatedUser authenticatedUser, String username);

    User findIdByUsername(AuthenticatedUser authenticatedUser, String username);

    User[] getAllUsers(AuthenticatedUser currentUser);
}
