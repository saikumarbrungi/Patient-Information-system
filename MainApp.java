package com.bskgsa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
public class MainApp {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (isValidCredentials(username, password)) {
            System.out.println("Login successful!");

            // Load Hibernate configuration
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            SessionFactory sessionFactory = configuration.buildSessionFactory();

            // Create and save patient, doctor, appointment, diagnosis, and billing entries
            createAndSaveEntities(sessionFactory);

            // Close the session factory
            sessionFactory.close();
        } else {
            System.out.println("Invalid credentials. Login failed.");
        }

        scanner.close();
    }

    private static boolean isValidCredentials(String username, String password) {
        // Replace with your actual authentication logic
        String validUsername = "saikumar";
        String validPassword = "sai2410";

        return validUsername.equals(username) && validPassword.equals(password);
    }

    private static void createAndSaveEntities(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Create and save patient
            Patient patient = new Patient();
            patient.setFirstName("John");
            patient.setLastName("Doe");
            patient.setGender("Male");
            patient.setDateOfBirth(parseDate("1990-01-01"));
            patient.setPhoneNumber("9234567890");
            patient.setAddress("123 Main Street, City");
            session.save(patient);

            // Create and save doctor
            Doctor doctor = new Doctor();
            doctor.setFirstName("Jane");
            doctor.setLastName("Smith");
            doctor.setSpecialization("Cardiology");
            doctor.setEmail("jane@gamil.com");
            doctor.setPhoneNumber("9876543210");
            doctor.setAddress("456 Medical Avenue, City");
            session.save(doctor);

            // Create and save appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(new Date());
            appointment.setStatus("Scheduled");
            appointment.setNotes("Routine checkup");
            session.save(appointment);

            // Create and save diagnosis
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setPatient(patient);
            diagnosis.setDoctor(doctor);
            diagnosis.setDiagnosisDate(new Date());
            diagnosis.setMedicalCondition("Hypertension");
            diagnosis.setNotes("Prescribed medication and lifestyle changes");
            session.save(diagnosis);

            // Create and save billing entry
            Billing billing = new Billing();
            billing.setPatient(patient);
            billing.setBillingDate(new Date());
            billing.setDescription("Consultation Fee");
            billing.setAmount(BigDecimal.valueOf(100.00));
            billing.setPaymentStatus("Unpaid");
            billing.setPaymentDate(null);
            session.save(billing);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    }

}
