package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public List<RegisterOutputDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public RegisterOutputDTO getUserById(Long id) {
        return null;
    }

    @Override
    public RegisterOutputDTO createUser(RegisterInputDTO registerInputDTO) {
        User user = new User();

        user.setName(registerInputDTO.getName());
        return null;
    }

    @Override
    public RegisterOutputDTO updateUser(Long id, RegisterInputDTO registerInputDTO) {
        return null;
    }

    @Override
    public String removeUser(Long id) {
        return "";
    }
}
