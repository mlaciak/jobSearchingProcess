package com.laciak.Model;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;


public class LocalJob {

    private Integer jobId;
    private String company;
    private String position;
    private LocalDate date;
    private boolean test;
    private LocalDate responseDate;
    private String description;
    private String typeOfApplication;
    private String typeOfResponse;


    private Integer days;


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public LocalDate getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfApplication() { return typeOfApplication; }

    public void setTypeOfApplication(String typeOfApplication) { this.typeOfApplication = typeOfApplication; }

    public String getTypeOfResponse() { return typeOfResponse; }

    public void setTypeOfResponse(String typeOfResponse) { this.typeOfResponse = typeOfResponse; }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public int dateToDays() {
//        Calendar calendar = new GregorianCalendar();
//        Date currentDate = calendar.getTime();
//        return (int) ((Objects.requireNonNullElse(responseDate, currentDate).getTime() - date.getTime()) / 1000 / 60 / 60 / 24);
        return (int) DAYS.between(date, Objects.requireNonNullElseGet(responseDate, LocalDate::now));
    }
}