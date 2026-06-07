package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

            registerOutputDTO.setRole(user.getRole());

            registerOutputDTOList.add(registerOutputDTO);
        }

        return registerOutputDTOList;
    }

    @Override
    public RegisterOutputDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        RegisterOutputDTO dto = new RegisterOutputDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        dto.setRole(user.getRole());

        return dto;
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

        registerOutputDTO.setRole(user.getRole());
        return  registerOutputDTO;

    }

    @Override
    public String removeUser(Long id) {
        String name =userRepository.findById(id).orElse(null).getName();
        userRepository.deleteById(id);
        return  "User name : " + name + " and Id : " + id + "has been removed successfully! ";

    }
    @Override
    public  String uploadResume(MultipartFile file){
    try {


        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserDoesNotExist("user not found"));
        String uploadResume = "uploads/";

        Files.createDirectories(Paths.get(uploadResume));

        String fileName = System.currentTimeMillis()
                         + "_ "
                         +file.getOriginalFilename();
        Path filePath =Paths.get(uploadResume , fileName);

        Files.copy(
                   file.getInputStream(),
                    filePath,
                StandardCopyOption.REPLACE_EXISTING);

        user.setResumePath(filePath.toString());
        System.out.println("UPLOAD USER = " + email);
        System.out.println("SAVED PATH = " + filePath);

        userRepository.save(user);


        return "Resume  uploaded successfully";
    }
   catch (IOException e) {
        throw new RuntimeException("Failed to upload resume");
    }

    }
    @Override
    public Resource downloadResume() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        String resumePath = user.getResumePath();
        System.out.println("DOWNLOAD USER = " + email);
        System.out.println("DB PATH = " + user.getResumePath());

        if (resumePath == null || resumePath.isEmpty()) {
            throw new RuntimeException("No resume uploaded");
        }

        try {

            Path path = Paths.get(resumePath);

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Resume file not found");
            }

            return resource;

        } catch (Exception e) {
            e.printStackTrace(); // important for debugging
            throw new RuntimeException("Resume not found");
        }
    }
}
