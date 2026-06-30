package test.bankapplication.dto.mapper;

import test.bankapplication.dto.response.UserDTO;
import test.bankapplication.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setDOB(user.getDOB());
        userDTO.setKycStatus(user.getKycStatus());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
