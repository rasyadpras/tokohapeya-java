package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.dto.request.CreateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.request.LoginRequest;
import com.enigma.challenge.tokohapeya.dto.request.RegisterRequest;
import com.enigma.challenge.tokohapeya.dto.response.LoginResponse;
import com.enigma.challenge.tokohapeya.dto.response.RegisterResponse;
import com.enigma.challenge.tokohapeya.entity.Role;
import com.enigma.challenge.tokohapeya.entity.UserAccount;
import com.enigma.challenge.tokohapeya.helper.UserRole;
import com.enigma.challenge.tokohapeya.repository.UserAccountRepo;
import com.enigma.challenge.tokohapeya.service.AuthService;
import com.enigma.challenge.tokohapeya.service.JwtService;
import com.enigma.challenge.tokohapeya.service.ProfileService;
import com.enigma.challenge.tokohapeya.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepo accountRepo;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${tokohapeya.superadmin.email}")
    private String superAdminEmail;
    @Value("${tokohapeya.superadmin.username}")
    private String superAdminUname;
    @Value("${tokohapeya.superadmin.password}")
    private String superAdminPass;

    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccount> userSuperAdmin = accountRepo.findByUsername(superAdminUname);
        if (userSuperAdmin.isPresent()) return;

        Role superAdmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role ownerShop = roleService.getOrSave(UserRole.ROLE_OWNER_SHOP);
        Role customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserAccount account = UserAccount.builder()
                .email(superAdminEmail)
                .username(superAdminUname)
                .password(passwordEncoder.encode(superAdminPass))
                .roles(List.of(superAdmin, admin, ownerShop, customer))
                .isEnable(true)
                .build();
        accountRepo.save(account);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = UserAccount.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashedPassword)
                .roles(List.of(role))
                .isEnable(true)
                .build();
        accountRepo.saveAndFlush(account);

        CreateProfileRequest profile = CreateProfileRequest.builder()
                .accountId(account.getId())
                .name(request.getName())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .build();
        profileService.create(profile);

        return RegisterResponse.builder()
                .email(account.getEmail())
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) throws DataIntegrityViolationException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);
        UserAccount account = (UserAccount) authenticated.getPrincipal();
        String token = jwtService.generateToken(account);
        return LoginResponse.builder()
                .token(token)
                .email(account.getEmail())
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
