package tr.com.htr.intern.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.htr.intern.userservice.dto.FilterRequest;
import tr.com.htr.intern.userservice.dto.PermissionUsersDto;
import tr.com.htr.intern.userservice.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/permission")
    public ResponseEntity<List<PermissionUsersDto>> getAllPermissions(@ModelAttribute FilterRequest filterRequest) {
        return new ResponseEntity<>(permissionService.getAllPermissions(filterRequest), HttpStatus.OK);
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<PermissionUsersDto> getPermissionById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(permissionService.getPermissionDtoById(id),HttpStatus.OK);
    }


}
