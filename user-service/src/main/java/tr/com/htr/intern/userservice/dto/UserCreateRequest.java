package tr.com.htr.intern.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserCreateRequest {

    private String username;

    private String firstName;

    private String lastName;

    private String password;
    private String phoneNumber;

    private List<Long> permissionId;
}
