package tr.com.htr.intern.userservice.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tr.com.htr.intern.userservice.dto.UserCreateRequest;
import tr.com.htr.intern.userservice.dto.UserResponse;
import tr.com.htr.intern.userservice.model.Permission;
import tr.com.htr.intern.userservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(user);
    }

    public Page<UserResponse> toResponses(Page<User> userList) {
        return userList.map(this::toResponse);
//        return new PageImpl<>(userList.stream().map(user -> toResponse(user)).collect(Collectors.toList()));
    }

    public Page<UserResponse> listToResponsesPage(List<User> userList, PageRequest pageRequest) {

        return new PageImpl<>(userList.stream().map(user -> toResponse(user)).collect(Collectors.toList()), pageRequest, userList.size());
    }

    public User toEntity(UserCreateRequest userCreateRequest, List<Permission> permissions) {
        User user = new User(userCreateRequest);
        user.setPermissions(permissions);
        return user;
    }

}
