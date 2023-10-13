package tr.com.htr.intern.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequest {
    private Long id;
    private String firstName;

    private String lastName;

    private String username;
    private String phoneNumber;

    private List<Long> permissionId;
}
