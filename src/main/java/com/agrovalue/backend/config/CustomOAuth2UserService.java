package com.agrovalue.backend.config;

import com.agrovalue.backend.dto.GoogleOAuth2UserInfo;
import com.agrovalue.backend.entity.Role;
import com.agrovalue.backend.entity.User;
import com.agrovalue.backend.repository.RoleRepository;
import com.agrovalue.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // Get provider name
        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        // Create user info based on provider
        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        
        // Save or update user
        User user = saveOrUpdateUser(userInfo, provider);
        
        // Return custom OAuth2 user with additional details
        return new CustomOAuth2User(oAuth2User, user);
    }
    
    @Transactional
    private User saveOrUpdateUser(GoogleOAuth2UserInfo userInfo, String provider) {
        User user = userRepository.findByProviderAndProviderId(provider, userInfo.getId())
                .orElseGet(() -> userRepository.findByEmail(userInfo.getEmail())
                        .orElse(new User()));
        
        user.setEmail(userInfo.getEmail());
        user.setName(userInfo.getName());
        user.setProvider(provider);
        user.setProviderId(userInfo.getId());
        user.setImageUrl(userInfo.getImageUrl());
        user.setEmailVerified(true);
        user.setVerified(true); // OAuth users are automatically verified
        
        // Set default role if new user
        if (user.getId() == null) {
            // Create default roles if they don't exist
            initializeDefaultRoles();
            
            // Fetch the BUYER role from database
            Role buyerRole = roleRepository.findByName("ROLE_BUYER")
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_BUYER not found"));
            Set<Role> roles = new HashSet<>();
            roles.add(buyerRole);
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }
    
    private void initializeDefaultRoles() {
        // Create default roles if they don't exist
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            
            Role farmerRole = new Role();
            farmerRole.setName("ROLE_FARMER");
            
            Role buyerRole = new Role();
            buyerRole.setName("ROLE_BUYER");
            
            roleRepository.save(adminRole);
            roleRepository.save(farmerRole);
            roleRepository.save(buyerRole);
        }
    }
}