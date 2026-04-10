package com.agrovalue.backend.dto;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(
            (String) attributes.get("sub"),
            (String) attributes.get("email"),
            (String) attributes.get("name"),
            (String) attributes.get("picture")
        );
    }
}