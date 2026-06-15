package test.bankapplication.dto.response;

public class AuthResponseDTO {
    private UserDTO userDTO;


    public AuthResponseDTO() {
    }

    public AuthResponseDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
