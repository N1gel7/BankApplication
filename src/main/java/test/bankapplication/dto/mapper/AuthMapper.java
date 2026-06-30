package test.bankapplication.dto.mapper;

import test.bankapplication.dto.response.AuthResponseDTO;
import test.bankapplication.dto.response.RegisterResponseDTO;
import test.bankapplication.entity.User;

public class AuthMapper {

    private AuthMapper() {
    }

    public static RegisterResponseDTO toRegisterResponse(User user) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDOB(user.getDOB());
        dto.setKycStatus(user.getKycStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRole(user.getRole());
        return dto;
    }

    public static AuthResponseDTO toAuthResponse(User user) {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setUserDTO(UserMapper.toUserDTO(user));
        return dto;
    }
}
