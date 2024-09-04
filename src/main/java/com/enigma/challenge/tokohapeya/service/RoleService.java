package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.entity.Role;
import com.enigma.challenge.tokohapeya.helper.UserRole;

public interface RoleService {
    Role getOrSave(UserRole role);
}
