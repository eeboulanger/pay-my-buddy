package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.ProfileException;

public interface IUserProfileService {

    void updateUser(UserDTO dto) throws ProfileException;

    UserDTO getCurrentUserAsDTO();
}
