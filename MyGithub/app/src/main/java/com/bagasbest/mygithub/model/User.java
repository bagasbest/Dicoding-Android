package com.bagasbest.mygithub.model;

public class User {

    private int id;
    private String name, organization, image;

    public User (){
    }

    public User(int id, String name, String organization, String image) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
