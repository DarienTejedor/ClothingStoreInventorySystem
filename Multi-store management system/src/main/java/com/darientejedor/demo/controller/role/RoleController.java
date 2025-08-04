package com.darientejedor.demo.controller.role;

import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.services.role.RoleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> rolesList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(roleService.listActiveRoles(pageable).map(RoleResponse::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> roleResponse(@PathVariable Long id){
        return ResponseEntity.ok(roleService.roleResponse(id));
    }


    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody @Valid RoleData roleData){
        roleService.createRole(roleData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @RequestBody @Valid RoleData roleData){
        RoleResponse roleResponse = roleService.updateRole(id, roleData);
        return ResponseEntity.ok(roleResponse);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deactiveRole(id);
        return ResponseEntity.noContent().build();
    }
}






