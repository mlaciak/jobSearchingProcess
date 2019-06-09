package com.laciak;

import com.laciak.Model.Job;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.Collection;

public class JobsManager {
    private static EntityManagerFactory factory;
    private static EntityManager entityManager;


    private static void begin() {
//        factory = Persistence.createEntityManagerFactory("localJob");
        factory = Persistence.createEntityManagerFactory("JobUnit");
        entityManager = factory.createEntityManager(); // ctrl+1
        entityManager.getTransaction().begin();
    }

    static void end() {
        entityManager.getTransaction().commit();
        entityManager.close();
        factory.close();
    }

    protected static void create(String company, String position, LocalDate applicationDate, boolean wasTest, String description, String typeOfApplication, String typeOfResponse) {
        begin();
        Job newJob = new Job();
        newJob.setCompany(company);
        newJob.setPosition(position);
        newJob.setDate(applicationDate);
        newJob.setTest(wasTest);
        newJob.setDescription(description);
        newJob.setTypeOfApplication(typeOfApplication);
        newJob.setTypeOfResponse(typeOfResponse);
        entityManager.persist(newJob);
        end();
    }

    protected static void create(String company, String position, LocalDate applicationDate, LocalDate responseDate, boolean wasTest, String description, String typeOfApplication, String typeOfResponse) {
        begin();
        Job newJob = new Job();
        newJob.setCompany(company);
        newJob.setPosition(position);
        newJob.setDate(applicationDate);
        newJob.setResponseDate(responseDate);
        newJob.setTest(wasTest);
        newJob.setDescription(description);
        newJob.setTypeOfApplication(typeOfApplication);
        newJob.setTypeOfResponse(typeOfResponse);
        entityManager.persist(newJob);
        end();
    }

    protected static void update(Job job) {
        begin();
        Job existingJob = new Job();
        existingJob.setJobId(job.getJobId());
        existingJob.setCompany(job.getCompany());
        existingJob.setPosition(job.getPosition());
        existingJob.setTest(job.isTest());
        existingJob.setDate(job.getDate());
        existingJob.setResponseDate(job.getResponseDate());
        existingJob.setDescription(job.getDescription());
        existingJob.setTypeOfApplication(job.getTypeOfApplication());
        existingJob.setTypeOfResponse(job.getTypeOfResponse());
        entityManager.merge(existingJob);
        end();
    }

    protected static Job find(Integer ID) {
        begin();
        Job job = entityManager.find(Job.class, ID);
        end();
        return job;
    }

    static Collection<Job> findAll() {
        begin();
        Query query = entityManager.createQuery("SELECT e FROM Job e");
        return (Collection<Job>) query.getResultList();
    }

    protected static void remove(Integer ID) {
        begin();
        Job reference = entityManager.getReference(Job.class, ID);
        entityManager.remove(reference);
        end();
    }
}
