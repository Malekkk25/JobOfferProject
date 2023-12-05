package com.example.miniprojet.entites;

public class Job {
    private String jobTitle;
    private String jobDescription;
    private String jobLocation;
    private String category;
    private String jobDate;
    private String experience;
    private String skills;
    private String key;
    private Long idJob;

    private String idComp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getIdComp() {
        return idComp;
    }

    public void setIdComp(String idComp) {
        this.idComp = idComp;
    }

    public Job() {
    }


    public Job(Long idJob,String jobTitle, String jobDescription, String jobLocation, String category, String jobDate, String experience, String skills) {
        this.idJob = idJob;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.category = category;
        this.jobDate = jobDate;
        this.experience = experience;
        this.skills = skills;
    }
    public Job(String jobTitle, String jobDescription, String jobLocation, String category, String jobDate, String experience, String skills) {
        this.idJob = idJob;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.category = category;
        this.jobDate = jobDate;
        this.experience = experience;
        this.skills = skills;
    }


    public Long getIdJob() {
        return idJob;
    }

    public void setIdJob(Long idJob) {
        this.idJob = idJob;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }


}
