package tr.com.htr.intern.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import tr.com.htr.intern.userservice.model.Permission;

@Getter
@Setter
public class PermissionResponse {
    private long id;
    private String name;

    public PermissionResponse(Permission permission) {
        setId(permission.getId());
        setName(permission.getName());
    }
}
