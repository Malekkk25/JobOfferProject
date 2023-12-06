package com.example.miniprojet.entites;

public class Post {
    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    String idUser,idJob;

    Long idPost;


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public Post(Long idPost,String idUser, String idJob) {
        this.idPost=idPost;
        this.idUser = idUser;
        this.idJob = idJob;
    }
    public Post() {

    }

    @Override
    public String toString() {
        return "Post{" +
                "idUser='" + idUser + '\'' +
                ", idJob='" + idJob + '\'' +
                ", idPost=" + idPost +
                '}';
    }
}
