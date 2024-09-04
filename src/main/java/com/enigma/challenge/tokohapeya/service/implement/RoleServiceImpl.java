package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.entity.Role;
import com.enigma.challenge.tokohapeya.helper.UserRole;
import com.enigma.challenge.tokohapeya.repository.RoleRepo;
import com.enigma.challenge.tokohapeya.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        return roleRepo.findByRole(role)
                .orElseGet(() -> roleRepo.saveAndFlush(
                        Role.builder().role(role).build()
                ));
    }
}
