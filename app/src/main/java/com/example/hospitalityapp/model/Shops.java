package com.example.hospitalityapp.model;

public class Shops {


    private String   image,Name,Phone,Status,Location,ShopIdentity,Category,GeneralRates;

    public Shops() {
    }

    public String getGeneralRates() {
        return GeneralRates;
    }

    public void setGeneralRates(String generalRates) {
        GeneralRates = generalRates;
    }

    public String getShopIdentity() {
        return ShopIdentity;
    }

    public void setShopIdentity(String shopIdentity) {
        ShopIdentity = shopIdentity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
