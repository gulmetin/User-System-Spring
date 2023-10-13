package tr.com.htr.intern.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.com.htr.intern.userservice.model.Permission;
import tr.com.htr.intern.userservice.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;
    private String phoneNumber;
    private String password;

    private List<PermissionResponse> permissionResponses;

    public UserResponse(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setPhoneNumber(user.getPhoneNumber());
        setPassword(user.getPassword());
        setPermissionResponses(user.getPermissions().stream().map(p -> new PermissionResponse(p)).collect(Collectors.toList()));
    }
}
