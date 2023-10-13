package tr.com.htr.intern.userservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.htr.intern.userservice.dto.*;
import tr.com.htr.intern.userservice.exception.*;
import tr.com.htr.intern.userservice.mapper.UserMapper;
import tr.com.htr.intern.userservice.model.Permission;
import tr.com.htr.intern.userservice.model.User;
import tr.com.htr.intern.userservice.rabbitmq.MessageProducer;
import tr.com.htr.intern.userservice.rabbitmq.Notification;
import tr.com.htr.intern.userservice.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final UserMapper userMapper;
    private final MessageProducer messageProducer;

    public UserService(UserRepository userRepository, PermissionService permissionService, MessageProducer messageProducer) {
        this.userRepository = userRepository;
        this.messageProducer = messageProducer;
        this.userMapper = new UserMapper();
        this.permissionService = permissionService;
    }

    //create default user
    @PostConstruct
    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();

            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setUsername("admin");
            admin.setPhoneNumber("12345");
            admin.setPassword("12345");
            userRepository.save(admin);
            System.out.println("User created, name is admin");
        }
    }

    //create user
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (checkUsername(userCreateRequest.getUsername())) {
            User user = userMapper.toEntity(userCreateRequest, permissionService.getPermissionListByIds(userCreateRequest.getPermissionId()));
            User createdUser = userRepository.save(user);
            log.info("New user created");

            String str = "USER CREATED! username:" + user.getUsername();
            messageProducer.sendToQueue(new Notification("CREATE", new Date(), str));

            return userMapper.toResponse(userRepository.findById(createdUser.getId()).orElseThrow(() -> new UserCouldNotBeCreatedException("User Couldn't Be Created")));
        } else {
            throw new ExistUsernameException("Exist Username");
        }
    }

    //delete user
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            String str = "USER DELETED! username:" + userRepository.findById(id).get().getUsername();
            messageProducer.sendToQueue(new Notification("DELETE", new Date(), str));
            userRepository.deleteById(id);

        } else {
            throw new ResourceNotFoundException("user not found");
        }
    }

    //update user
    public UserResponse updateUser(UserUpdateRequest user) {
        User currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if (user.getFirstName() != null) {
            currentUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            currentUser.setLastName(user.getLastName());
        }
        if (user.getPhoneNumber() != null) {
            currentUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getUsername() != null) {
            currentUser.setUsername(user.getUsername());
        }
        if (user.getPermissionId() != null) {
            List<Permission> permissions = user.getPermissionId().stream().map(permissionService::getPermissionById).collect(Collectors.toList());
            currentUser.setPermissions(permissions);
        }
        String str = "USER UPDATED! username:" + currentUser.getUsername() ;
        messageProducer.sendToQueue(new Notification("UPDATE", new Date(),  str));
        return userMapper.toResponse(userRepository.save(currentUser));

    }

    //list user by filter parameters
    public Page<UserResponse> getAllUsers(FilterRequest filterRequest, PageRequest pageRequest) {
        if (!filterRequest.getOperator().isEmpty() && !filterRequest.getFilter().isEmpty()) {
            Specification<User> spec = createSpecification(filterRequest);
            if (filterRequest.getSort().toUpperCase().equals("DESC") && !filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(spec, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(filterRequest.getSortBy()).descending())));
            } else if (filterRequest.getSort().toUpperCase().equals("ASC") && !filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(spec, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(filterRequest.getSortBy()).ascending())));
            } else if (filterRequest.getSort().isEmpty() || filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(spec, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize())));
            } else {
                throw new InvalidValueException("invalid sort value!");
            }
        } else {
            if (filterRequest.getSort().toUpperCase().equals("DESC") && !filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(filterRequest.getSortBy()).descending())));
            } else if (filterRequest.getSort().toUpperCase().equals("ASC") && !filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(filterRequest.getSortBy()).ascending())));
            } else if (filterRequest.getSort().isEmpty() || filterRequest.getSortBy().isEmpty()) {
                return userMapper.toResponses(userRepository.findAll(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize())));
            } else {
                throw new InvalidValueException("invalid sort value!");
            }
        }
    }

    //find user by id
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(
                userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"))
        );
    }

    //find permission by user id
    public List<PermissionResponse> findPermissionsByUsersId(Long id) {
        return permissionService.findPermissionsByUserId(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found")));
    }

    //find userS by permission id
    public Page<UserResponse> findUsersByPermissionsId(Long id, PageRequest pageRequest) {
        return userMapper.listToResponsesPage(permissionService.getPermissionById(id).getUserList(), pageRequest);
    }

    //find user by permission id
    public UserResponse findUserByPermissionsId(Long id, Long userId) {
        return userMapper.toResponse(permissionService.getPermissionById(id).getUserList().stream().filter(u -> u.getId().equals(userId)).findAny().orElseThrow(
                () -> new ResourceNotFoundException("user not found")));
    }

    //create specification for filter
    private Specification<User> createSpecification(FilterRequest filterRequest) {
        switch (filterRequest.getOperator().toUpperCase()) {
            case "EQUALS":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filterRequest.getFilter()), filterRequest.getValue());
            case "LIKE":
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(filterRequest.getFilter()), "%" + filterRequest.getValue() + "%");
            case "PERMISSION":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.join("permissions").get(filterRequest.getFilter()), filterRequest.getValue()
                );
            default:
                throw new InvalidValueException("Operation not supported yet");
        }
    }

    //username-check
    private Boolean checkUsername(String username) {
        if (username.contains("admin") || userRepository.existsByUsername(username)) {
            return false;
        } else {
            return true;
        }
    }


    //login func
    public UserResponse login(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            if (password.equals(user.getPassword())) {
                return userMapper.toResponse(user);
            } else {
                throw new AuthenticateException("invalid username or password");
            }
        } else {
            throw new AuthenticateException("invalid username or password");
        }
    }


    //rabbitmq send message test
//    @PostConstruct
//    private void test() {
//        messageProducer.sendToQueue(new Notification("test", new Date(), "TEST GUL"));
//    }
}
