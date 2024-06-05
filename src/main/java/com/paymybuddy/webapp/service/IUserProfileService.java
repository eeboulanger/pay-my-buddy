package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;

public interface IUserProfileService {

    void updateUser(UserDTO dto);
    void deleteUser(int id);
}
