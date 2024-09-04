package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.dto.request.CreateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.request.UpdateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.response.ProfileResponse;
import com.enigma.challenge.tokohapeya.entity.Profile;

import java.util.List;

public interface ProfileService {
    ProfileResponse create(CreateProfileRequest profile);
    ProfileResponse getById(String id);
    List<ProfileResponse> getAll();
    ProfileResponse update(UpdateProfileRequest profile);
    void deleteById(String id);
    Profile getProfileById(String id);
}
