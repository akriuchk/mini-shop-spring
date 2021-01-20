package org.akriuchk.minishop.service;

import org.akriuchk.minishop.model.UserProfile;
import org.akriuchk.minishop.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserProfileRepository userRepo;


    public void addProfile(UserProfile userProfile) {
        userRepo.save(userProfile);
    }

    public Iterable<UserProfile> listProfiles() {
        return userRepo.findAll();
    }

}
