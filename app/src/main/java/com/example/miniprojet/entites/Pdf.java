package com.example.miniprojet.entites;

public class Pdf {

    private  String name,url,idUser;
    public Pdf() {

    }

    public Pdf(String name, String url,String idUser) {
        this.name = name;
        this.url = url;
        this.idUser=idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
