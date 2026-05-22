package com.JobPortal.JPP.service;


import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.entity.User;

import java.util.List;

public interface UserService {
    List<RegisterOutputDTO> getAllUsers();
    RegisterOutputDTO getUserById(Long id);
    RegisterOutputDTO createUser(RegisterInputDTO registerInputDTO);
    RegisterOutputDTO updateUser(Long id , RegisterInputDTO registerInputDTO);
    String removeUser(Long id);

}
