package tr.com.htr.intern.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.htr.intern.userservice.dto.*;
import tr.com.htr.intern.userservice.exception.*;
import tr.com.htr.intern.userservice.mapper.PermissionMapper;
import tr.com.htr.intern.userservice.model.*;
import tr.com.htr.intern.userservice.repository.PermissionRepository;
import javax.annotation.PostConstruct;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class PermissionService {
    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = new PermissionMapper();
    }

    @PostConstruct
    private void createPermissions() {
        if (!permissionRepository.existsByname("CREATE")) {
            Permission permission = new Permission();
            permission.setName("CREATE");
            permissionRepository.save(permission);
        }
        if (!permissionRepository.existsByname("DELETE")) {
            Permission permission1 = new Permission();
            permission1.setName("DELETE");
            permissionRepository.save(permission1);
        }
        if (!permissionRepository.existsByname("UPDATE")) {
            Permission permission2 = new Permission();
            permission2.setName("UPDATE");
            permissionRepository.save(permission2);
        }
        if (!permissionRepository.existsByname("LIST")) {
            Permission permission3 = new Permission();
            permission3.setName("LIST");
            permissionRepository.save(permission3);
        }
        if (!permissionRepository.existsByname("NOTIFICATION")) {
            Permission permission4 = new Permission();
            permission4.setName("NOTIFICATION");
            permissionRepository.save(permission4);
        }
    }

    public List<PermissionUsersDto> getAllPermissions(FilterRequest filterRequest) {
        if(!filterRequest.getOperator().isEmpty() && !filterRequest.getFilter().isEmpty()){
            Specification<Permission> spec = createSpecification(filterRequest);
            if(filterRequest.getSort().toUpperCase().equals("DESC") && !filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll(spec, Sort.by(filterRequest.getSortBy()).descending()));
            }
            else if(filterRequest.getSort().toUpperCase().equals("ASC") && !filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll(spec, Sort.by(filterRequest.getSortBy()).ascending()));
            }
            else if(filterRequest.getSort().isEmpty() || filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll(spec));
            }
            else{
                throw new InvalidValueException("invalid sort value!");
            }
        }
        else{
            if(filterRequest.getSort().toUpperCase().equals("DESC") && !filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll(Sort.by(filterRequest.getSortBy()).descending()));
            }
            else if(filterRequest.getSort().toUpperCase().equals("ASC") && !filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll(Sort.by(filterRequest.getSortBy()).ascending()));
            }
            else if(filterRequest.getSort().isEmpty() || filterRequest.getSortBy().isEmpty()){
                return permissionMapper.toPermissionUsersResponses(permissionRepository.findAll());
            }
            else{
                throw new InvalidValueException("invalid sort value!");
            }

        }
    }

    public Permission getPermissionById(Long id){
        return permissionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("permission not found"));
    }

    public PermissionUsersDto getPermissionDtoById(Long id) {
        return permissionMapper.toPermissionUsersResponse(permissionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("permission not found")));
    }

    public List<Permission> getPermissionListByIds(List<Long> permissionId) {
        return permissionId.stream().map(id->permissionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("permission not found"))).collect(Collectors.toList());
    }


    public List<PermissionResponse> findPermissionsByUserId(User user) {
        return user.getPermissions().stream().map(
                permissionMapper::toResponse).collect(Collectors.toList()
        );
    }
    public Long findPermissionsByName(String permissionName){
        return permissionRepository.findByName(permissionName).getId();
    }

    private Specification<Permission> createSpecification(FilterRequest filterRequest) {
        switch (filterRequest.getOperator().toUpperCase()){
            case "EQUALS":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filterRequest.getFilter()), filterRequest.getValue());
            case "LIKE":
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(filterRequest.getFilter()), "%"+filterRequest.getValue()+"%");
            case "USER":
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.join("userList").get(filterRequest.getFilter()), filterRequest.getValue()
                );
            default:
                throw new InvalidValueException("Operation not supported yet");
        }
    }
}
