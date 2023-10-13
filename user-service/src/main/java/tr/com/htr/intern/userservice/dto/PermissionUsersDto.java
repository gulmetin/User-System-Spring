package tr.com.htr.intern.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import tr.com.htr.intern.userservice.model.Permission;
import tr.com.htr.intern.userservice.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PermissionUsersDto {
    private long id;
    private String name;
    private List<Long> usersId;

    public PermissionUsersDto(Permission permission) {
        setId(permission.getId());
        setName(permission.getName());
        setUsersId(permission.getUserList().stream().map(user -> user.getId()).collect(Collectors.toList()));
    }
}
