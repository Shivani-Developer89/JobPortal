package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;



    @Override
    public List<RegisterOutputDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<RegisterOutputDTO> registerOutputDTOList = new ArrayList<>();
        for(User user : userList){
            RegisterOutputDTO registerOutputDTO = new RegisterOutputDTO();

            registerOutputDTO.setId(user.getId());
            registerOutputDTO.setName(user.getName());
            registerOutputDTO.setEmail(user.getEmail());
            registerOutputDTO.setPassword(user.getPassword());
            registerOutputDTO.setRole(user.getRole());

            registerOutputDTOList.add(registerOutputDTO);
        }

        return registerOutputDTOList;
    }

    @Override
    public RegisterOutputDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        RegisterOutputDTO registerOutputDTO = new RegisterOutputDTO();
        registerOutputDTO.setId(user.getId());
        registerOutputDTO.setName(user.getName());
        registerOutputDTO.setEmail(user.getEmail());
        registerOutputDTO.setPassword(user.getPassword());
        registerOutputDTO.setRole(user.getRole());

        return registerOutputDTO;
    }

    @Override
    public RegisterOutputDTO createUser(RegisterInputDTO registerInputDTO) {
        User user = new User();


        user.setName(registerInputDTO.getName());
        user.setEmail(registerInputDTO.getEmail());
        user.setPassword(
                passwordEncoder.encode(registerInputDTO.getPassword())
        );
        user.setRole(registerInputDTO.getRole());

        user = userRepository.save(user);

        RegisterOutputDTO registerOutputDTO = new RegisterOutputDTO();
        registerOutputDTO.setId(user.getId());
        registerOutputDTO.setName(user.getName());
        registerOutputDTO.setEmail(user.getEmail());
        registerOutputDTO.setPassword(user.getPassword());
        registerOutputDTO.setRole(user.getRole());
        return  registerOutputDTO;
    }

    @Override
    public RegisterOutputDTO updateUser(Long id, RegisterInputDTO registerInputDTO) {
        User user = new User();

        user.setId(id);
        user.setName(registerInputDTO.getName());
        user.setEmail(registerInputDTO.getEmail());
        user.setPassword(registerInputDTO.getPassword());
        user.setRole(registerInputDTO.getRole());

        user = userRepository.save(user);

        RegisterOutputDTO registerOutputDTO = new RegisterOutputDTO();
        registerOutputDTO.setId(user.getId());
        registerOutputDTO.setName(user.getName());
        registerOutputDTO.setEmail(user.getEmail());
        registerOutputDTO.setPassword(user.getPassword());
        registerOutputDTO.setRole(user.getRole());
        return  registerOutputDTO;

    }

    @Override
    public String removeUser(Long id) {
        String name =userRepository.findById(id).orElse(null).getName();
        userRepository.deleteById(id);
        return  "User name : " + name + " and Id : " + id + "has been removed successfully! ";

    }
}
