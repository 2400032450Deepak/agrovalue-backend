package com.agrovalue.backend.dto;

public abstract class OAuth2UserInfo {
    protected String id;
    protected String email;
    protected String name;
    protected String imageUrl;

    public OAuth2UserInfo(String id, String email, String name, String imageUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
}