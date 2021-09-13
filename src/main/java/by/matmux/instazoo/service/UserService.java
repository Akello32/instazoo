package by.matmux.instazoo.service;

import by.matmux.instazoo.dao.UserDao;
import by.matmux.instazoo.dto.UserDTO;
import by.matmux.instazoo.entity.User;
import by.matmux.instazoo.entity.enums.Role;
import by.matmux.instazoo.exceptions.UserExistException;
import by.matmux.instazoo.payload.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        try {
            LOG.info("Saving user {}", user.getEmail());
            return userDao.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User update(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userDao.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User getUserById(Long userId) {
        return userDao.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userDao.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

}
