package tr.com.htr.intern.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.com.htr.intern.userservice.dto.UserCreateRequest;
import tr.com.htr.intern.userservice.repository.PermissionRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,

    })
    @JoinTable(name = "user_permissions",
            joinColumns = { @JoinColumn(name = "user_id",nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "permission_id",nullable = false) })
    private List<Permission> permissions = new ArrayList<>();

    public User(UserCreateRequest request) {
        setUsername(request.getUsername());
        setPassword(request.getPassword());
        setFirstName(request.getFirstName());
        setLastName(request.getLastName());
        setPhoneNumber(request.getPhoneNumber());
    }

}
