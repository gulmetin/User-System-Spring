package tr.com.htr.intern.userservice.mapper;

import tr.com.htr.intern.userservice.dto.PermissionResponse;
import tr.com.htr.intern.userservice.dto.PermissionUsersDto;
import tr.com.htr.intern.userservice.model.Permission;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionMapper {
    public PermissionMapper() {

    }

    public PermissionResponse toResponse(Permission permission) {
        return new PermissionResponse(permission);
    }

    public PermissionUsersDto toPermissionUsersResponse(Permission permission) {
        return new PermissionUsersDto(permission);
    }

    public List<PermissionUsersDto> toPermissionUsersResponses(List<Permission> permissions) {
        return permissions.stream().map(this::toPermissionUsersResponse).collect(Collectors.toList());

    }

    public Permission toEntity(PermissionUsersDto permissionUsersDto){
        Permission permission = new Permission(permissionUsersDto);
        return permission;
    }

}
