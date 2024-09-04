package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.dto.request.CreateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.request.UpdateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.response.AccountResponse;
import com.enigma.challenge.tokohapeya.dto.response.ProfileResponse;
import com.enigma.challenge.tokohapeya.entity.Profile;
import com.enigma.challenge.tokohapeya.entity.UserAccount;
import com.enigma.challenge.tokohapeya.repository.ProfileRepo;
import com.enigma.challenge.tokohapeya.repository.UserAccountRepo;
import com.enigma.challenge.tokohapeya.service.ProfileService;
import com.enigma.challenge.tokohapeya.service.UserService;
import com.enigma.challenge.tokohapeya.utils.ConverterUtil;
import com.enigma.challenge.tokohapeya.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserAccountRepo accountRepo;
    private final ProfileRepo profileRepo;
    private final UserService accountService;
    private final ValidationUtil validation;
    private final ConverterUtil converter;

    private AccountResponse toAccountResponse(UserAccount account) {
        return AccountResponse.builder()
                .accountId(account.getId())
                .email(account.getEmail())
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private ProfileResponse toProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .account(toAccountResponse(profile.getAccount()))
                .name(profile.getName())
                .address(profile.getAddress())
                .birthDate(profile.getBirthDate())
                .createdAt(profile.getCreatedAt())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfileResponse create(CreateProfileRequest request) {
        validation.validate(request);
        Profile profile = Profile.builder()
                .account(accountRepo.findById(request.getAccountId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")))
                .name(request.getName())
                .address(request.getAddress())
                .birthDate(converter.convertToDate(request.getBirthDate()))
                .createdAt(LocalDateTime.now())
                .build();
        profileRepo.saveAndFlush(profile);
        return toProfileResponse(profile);
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileResponse getById(String id) {
        Profile profile = profileRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        return toProfileResponse(profile);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProfileResponse> getAll() {
        return profileRepo.findAll().stream().map(this::toProfileResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfileResponse update(UpdateProfileRequest request) {
        Profile profile = getProfileById(request.getId());
        validation.validate(request);
        profile.setName(request.getName());
        profile.setAddress(request.getAddress());
        profile.setBirthDate(converter.convertToDate(request.getBirthDate()));
        profileRepo.saveAndFlush(profile);
        return toProfileResponse(profile);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Profile profile = getProfileById(id);
        accountService.deleteById(profile.getAccount().getId());
        profileRepo.delete(profile);
    }

    @Transactional(readOnly = true)
    @Override
    public Profile getProfileById(String id) {
        Profile profile = profileRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        UserAccount accountByContext = accountService.getByContext();
        if (!accountByContext.getId().equals(profile.getAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.getReasonPhrase() + ": User not match");
        }
        return profile;
    }
}
