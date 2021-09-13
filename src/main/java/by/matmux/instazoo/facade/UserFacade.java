package by.matmux.instazoo.facade;

import by.matmux.instazoo.dto.UserDTO;
import by.matmux.instazoo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        userDTO.setId(user.getId());

        return userDTO;
    }
}
