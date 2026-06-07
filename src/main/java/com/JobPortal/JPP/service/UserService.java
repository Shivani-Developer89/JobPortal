package com.JobPortal.JPP.service;


import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<RegisterOutputDTO> getAllUsers();
    RegisterOutputDTO getUserById(Long id);
    RegisterOutputDTO createUser(RegisterInputDTO registerInputDTO);
    RegisterOutputDTO updateUser(Long id , RegisterInputDTO registerInputDTO);
    String removeUser(Long id);
    String uploadResume(MultipartFile file);
    Resource downloadResume();

}
