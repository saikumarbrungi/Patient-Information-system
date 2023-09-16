package com.bskgsa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SessionFactory sessionFactory = buildSessionFactory();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (checkLogin(username, password, sessionFactory)) {
                System.out.println("Login successful!");
                loggedIn = true;
            } else {
                System.out.println("Invalid credentials. Do you want to retry? (y/n)");
                char choice = scanner.next().charAt(0);
                if (choice == 'y' || choice == 'Y') {
                    // Continue to the next iteration of the loop
                    scanner.nextLine(); // Clear the newline character
                    continue;
                } else {
                    System.out.println("Exiting program.");
                    System.exit(0);
                }
            }
        }

        // After successful login, display the menu options
        displayMenu(scanner, sessionFactory);

        sessionFactory.close();
        scanner.close();
    }

    private static void displayMenu(Scanner scanner, SessionFactory sessionFactory) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Doctors");
            System.out.println("3. Manage Appointments");
            System.out.println("4. Manage Diagnoses");
            System.out.println("5. Manage Billings");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character

            switch (choice) {
                case 1:
                    managePatients(sessionFactory, scanner);
                    break;
                case 2:
                    manageDoctors(sessionFactory, scanner);
                    break;
                case 3:
                    manageAppointments(sessionFactory, scanner);
                    break;
                case 4:
                    manageDiagnoses(sessionFactory, scanner);
                    break;
                case 5:
                    manageBillings(sessionFactory, scanner);
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void managePatients(SessionFactory sessionFactory, Scanner scanner) {
        // Implement patient management here...
    
    	    boolean exit = false;
    	    
    	    while (!exit) {
    	        System.out.println("\nPatient Management:");
    	        System.out.println("1. Add Patient");
    	        System.out.println("2. View Patients");
    	        System.out.println("3. Update Patient Details");
    	        System.out.println("4. Delete Patient");
    	        System.out.println("5. Go back to main menu");

    	        System.out.print("Enter your choice: ");
    	        int choice = scanner.nextInt();
    	        scanner.nextLine(); // Clear the newline character

    	        switch (choice) {
    	            case 1:
    	                addPatient(sessionFactory, scanner);
    	                break;
    	            case 2:
    	                viewPatients(sessionFactory);
    	                break;
    	            case 3:
    	                updatePatientDetails(sessionFactory, scanner);
    	                break;
    	            case 4:
    	                deletePatient(sessionFactory, scanner);
    	                break;
    	            case 5:
    	                exit = true;
    	                System.out.println("Returning to main menu.");
    	                break;
    	            default:
    	                System.out.println("Invalid choice. Please select a valid option.");
    	        }
    	    }
    }

    	private static void addPatient(SessionFactory sessionFactory, Scanner scanner) {
    	    // Implement adding a new patient here...
    		 try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        System.out.println("Enter patient details:");
    		        System.out.print("First Name: ");
    		        String firstName = scanner.nextLine();
    		        System.out.print("Last Name: ");
    		        String lastName = scanner.nextLine();
    		        System.out.print("Gender: ");
    		        String gender = scanner.nextLine();
    		        System.out.print("Date of Birth (yyyy-MM-dd): ");
    		        String dobString = scanner.nextLine();
    		        Date dateOfBirth = parseDate(dobString);
    		        System.out.print("Phone Number: ");
    		        String phoneNumber = scanner.nextLine();
                        String phoneNumberRegex = "^[0-9]{10}$"; // 10-digit number
    		        Pattern pattern = Pattern.compile(phoneNumberRegex);
    		        Matcher matcher = pattern.matcher(phoneNumber);

    		        if (!matcher.matches()) {
    		            System.out.println("Invalid phone number format. Please enter a 10-digit number.");
    		            return;
    		        }
    		        System.out.print("Address: ");
    		        String address = scanner.nextLine();

    		        Patient patient = new Patient();
    		        patient.setFirstName(firstName);
    		        patient.setLastName(lastName);
    		        patient.setGender(gender);
    		        patient.setDateOfBirth(dateOfBirth);
    		        patient.setPhoneNumber(phoneNumber);
    		        patient.setAddress(address);

    		        session.save(patient);
    		        transaction.commit();

    		        System.out.println("Patient added successfully.");
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    	}

    	private static void viewPatients(SessionFactory sessionFactory) {
    	    try (Session session = sessionFactory.openSession()) {
    	        String hql = "FROM Patient";
    	        Query<Patient> query = session.createQuery(hql, Patient.class);
    	        List<Patient> patients = query.list();

    	        if (!patients.isEmpty()) {
    	            System.out.println("List of Patients:");
    	            for (Patient patient : patients) {
    	                System.out.println("Patient ID: " + patient.getId());
    	                System.out.println("Patient Name: " + patient.getFirstName() + " " + patient.getLastName());
    	                System.out.println("Gender: " + patient.getGender());
    	                System.out.println("Date of Birth: " + patient.getDateOfBirth());
    	                System.out.println("Phone Number: " + patient.getPhoneNumber());
    	                System.out.println("Address: " + patient.getAddress());
    	                System.out.println("--------------------");
    	            }
    	        } else {
    	            System.out.println("No patients found.");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        System.out.println("An error occurred while viewing patients.");
    	    }
    	}


    	private static void updatePatientDetails(SessionFactory sessionFactory, Scanner scanner) {
    	    // Implement updating patient details here...
    		 try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        System.out.print("Enter patient ID to update: ");
    		        long patientId = scanner.nextLong();
    		        scanner.nextLine(); // Clear the newline character

    		        Patient patient = session.get(Patient.class, patientId);

    		        if (patient == null) {
    		            System.out.println("Patient not found.");
    		        } else {
    		            System.out.println("Current patient details:");
    		            System.out.println(patient);

    		            System.out.println("Enter updated patient details:");
    		            System.out.print("First Name: ");
    		            patient.setFirstName(scanner.nextLine());
    		            System.out.print("Last Name: ");
    		            patient.setLastName(scanner.nextLine());
    		            System.out.print("Gender: ");
    		            patient.setGender(scanner.nextLine());
    		            System.out.print("Date of Birth (yyyy-MM-dd): ");
    		            String dobString = scanner.nextLine();
    		            patient.setDateOfBirth(parseDate(dobString));
    		            System.out.print("Phone Number: ");
    		            patient.setPhoneNumber(scanner.nextLine());
    		            System.out.print("Address: ");
    		            patient.setAddress(scanner.nextLine());

    		            session.update(patient);
    		            transaction.commit();

    		            System.out.println("Patient details updated successfully.");
    		        }
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    	}

    	private static void deletePatient(SessionFactory sessionFactory, Scanner scanner) {
    	    // Implement deleting a patient here...
    		 try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        System.out.print("Enter patient ID to delete: ");
    		        long patientId = scanner.nextLong();
    		        scanner.nextLine(); // Clear the newline character

    		        Patient patient = session.get(Patient.class, patientId);

    		        if (patient == null) {
    		            System.out.println("Patient not found.");
    		        } else {
    		            session.delete(patient);
    		            transaction.commit();

    		            System.out.println("Patient deleted successfully.");
    		        }
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    	}

    

    private static void manageDoctors(SessionFactory sessionFactory, Scanner scanner) {
        // Implement doctor management here...
    	 while (true) {
    	        System.out.println("\nDoctor Management Menu:");
    	        System.out.println("1. Add New Doctor");
    	        System.out.println("2. View Doctors");
    	        System.out.println("3. Update Doctor Details");
    	        System.out.println("4. Delete Doctor");
    	        System.out.println("5. Back to Main Menu");
    	        System.out.print("Enter your choice: ");
    	        
    	        int choice = scanner.nextInt();
    	        scanner.nextLine(); // Clear the newline character

    	        switch (choice) {
    	            case 1:
    	                addDoctor(sessionFactory, scanner);
    	                break;
    	            case 2:
    	                viewDoctors(sessionFactory);
    	                break;
    	            case 3:
    	                updateDoctorDetails(sessionFactory, scanner);
    	                break;
    	            case 4:
    	                deleteDoctor(sessionFactory, scanner);
    	                break;
    	            case 5:
    	                return; // Exit the method to go back to the main menu
    	            default:
    	                System.out.println("Invalid choice. Please choose again.");
    	        }
    	    }
    }
    	 private static void addDoctor(SessionFactory sessionFactory, Scanner scanner) {
    		    try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        Doctor doctor = new Doctor();

    		        System.out.print("Enter First Name: ");
    		        doctor.setFirstName(scanner.nextLine());

    		        System.out.print("Enter Last Name: ");
    		        doctor.setLastName(scanner.nextLine());

    		        System.out.print("Enter Specialization: ");
    		        doctor.setSpecialization(scanner.nextLine());

    		        System.out.print("Enter Email: ");
    		        String email = scanner.nextLine();
    		        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Basic email format
    		        Pattern pattern = Pattern.compile(emailRegex);
    		        Matcher matcher = pattern.matcher(email);

    		        if (!matcher.matches()) {
    		            System.out.println("Invalid email address format. Please enter a valid email address.");
    		            return;
    		        }


    		        System.out.print("Enter Phone Number: ");
    		        String phoneNumber = scanner.nextLine();

    		        String phoneNumberRegex = "^[0-9]{10}$"; // 10-digit number
    		        Pattern phonePattern = Pattern.compile(phoneNumberRegex);
    		        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);

    		        if (!phoneMatcher.matches()) {
    		            System.out.println("Invalid phone number format. Please enter a 10-digit number.");
    		            return;
    		        }

    		        doctor.setPhoneNumber(phoneNumber);
    		        

    		        System.out.print("Enter Address: ");
    		        doctor.setAddress(scanner.nextLine());

    		        session.save(doctor);
    		        transaction.commit();

    		        System.out.println("Doctor added successfully!");
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    		}
    	 
    	 private static void viewDoctors(SessionFactory sessionFactory) {
    		    try (Session session = sessionFactory.openSession()) {
    		        String hql = "FROM Doctor";
    		        Query<Doctor> query = session.createQuery(hql, Doctor.class);
    		        List<Doctor> doctors = query.list();

    		        if (!doctors.isEmpty()) {
    		            System.out.println("List of Doctors:");
    		            for (Doctor doctor : doctors) {
    		                System.out.println("Doctor ID: " + doctor.getId());
    		                System.out.println("Doctor Name: " + doctor.getFirstName() + " " + doctor.getLastName());
    		                System.out.println("Specialization: " + doctor.getSpecialization());
    		                System.out.println("Email: " + doctor.getEmail());
    		                System.out.println("Phone Number: " + doctor.getPhoneNumber());
    		                System.out.println("Address: " + doctor.getAddress());
    		                System.out.println("--------------------");
    		            }
    		        } else {
    		            System.out.println("No doctors found.");
    		        }
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		        System.out.println("An error occurred while viewing doctors.");
    		    }
    		}

    	 private static void updateDoctorDetails(SessionFactory sessionFactory, Scanner scanner) {
    		    try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        System.out.print("Enter Doctor ID to update: ");
    		        long doctorId = scanner.nextLong();
    		        scanner.nextLine(); // Consume newline

    		        Doctor doctor = session.get(Doctor.class, doctorId);

    		        if (doctor == null) {
    		            System.out.println("Doctor not found.");
    		            return;
    		        }

    		        System.out.println("Current Doctor Details:");
    		        System.out.println(doctor);

    		        System.out.print("Enter New First Name (or press Enter to skip): ");
    		        String newFirstName = scanner.nextLine();
    		        if (!newFirstName.isEmpty()) {
    		            doctor.setFirstName(newFirstName);
    		        }

    		        // Repeat similar steps for other fields

    		        session.update(doctor);
    		        transaction.commit();

    		        System.out.println("Doctor details updated successfully!");
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    		}
          
    	 private static void deleteDoctor(SessionFactory sessionFactory, Scanner scanner) {
    		    try (Session session = sessionFactory.openSession()) {
    		        Transaction transaction = session.beginTransaction();

    		        System.out.print("Enter Doctor ID to delete: ");
    		        long doctorId = scanner.nextLong();
    		        scanner.nextLine(); // Consume newline

    		        Doctor doctor = session.get(Doctor.class, doctorId);

    		        if (doctor == null) {
    		            System.out.println("Doctor not found.");
    		            return;
    		        }

    		        session.delete(doctor);
    		        transaction.commit();

    		        System.out.println("Doctor deleted successfully!");
    		    } catch (Exception e) {
    		        e.printStackTrace();
    		    }
    		}


    private static void manageAppointments(SessionFactory sessionFactory, Scanner scanner) {
        // Implement appointment management here...
    	 boolean exit = false;

    	    while (!exit) {
    	        System.out.println("Appointments Management");
    	        System.out.println("1. Add Appointment");
    	        System.out.println("2. View Appointments");
    	        System.out.println("3. Update Appointment");
    	        System.out.println("4. Delete Appointment");
    	        System.out.println("5. Back to Main Menu");
    	        System.out.print("Enter your choice: ");

    	        int choice = scanner.nextInt();
    	        scanner.nextLine(); // Consume newline

    	        switch (choice) {
    	            case 1:
    	                addAppointment(sessionFactory, scanner);
    	                break;
    	            case 2:
    	                viewAppointments(sessionFactory);
    	                break;
    	            case 3:
    	                updateAppointmentDetails(sessionFactory, scanner);
    	                break;
    	            case 4:
    	                deleteAppointment(sessionFactory, scanner);
    	                break;
    	            case 5:
    	                exit = true;
    	                break;
    	            default:
    	                System.out.println("Invalid choice. Please select a valid option.");
    	        }
    	    }
       }
    private static void addAppointment(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Appointment appointment = new Appointment();

            System.out.print("Enter Patient ID: ");
            long patientId = scanner.nextLong();
            scanner.nextLine(); // Consume newline
            Patient patient = session.get(Patient.class, patientId);
            if (patient == null) {
                System.out.println("Patient not found.");
                return;
            }
            appointment.setPatient(patient);

            System.out.print("Enter Doctor ID: ");
            long doctorId = scanner.nextLong();
            scanner.nextLine(); // Consume newline
            Doctor doctor = session.get(Doctor.class, doctorId);
            if (doctor == null) {
                System.out.println("Doctor not found.");
                return;
            }
            appointment.setDoctor(doctor);

            System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
            String appointmentDateStr = scanner.nextLine();
            try {
                Date appointmentDate = parseDate(appointmentDateStr);
                appointment.setAppointmentDate(appointmentDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Appointment not added.");
                return;
            }

            System.out.print("Enter Appointment Status: ");
            appointment.setStatus(scanner.nextLine());

            System.out.print("Enter Notes: ");
            appointment.setNotes(scanner.nextLine());

            session.save(appointment);
            transaction.commit();

            System.out.println("Appointment added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void viewAppointments(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Appointment";
            Query<Appointment> query = session.createQuery(hql, Appointment.class);
            List<Appointment> appointments = query.list();

            if (!appointments.isEmpty()) {
                System.out.println("List of Appointments:");
                for (Appointment appointment : appointments) {
                    System.out.println("Appointment ID: " + appointment.getId());
                    System.out.println("Patient Name: " + appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
                    System.out.println("Doctor Name: " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
                    System.out.println("Appointment Date: " + appointment.getAppointmentDate());
                    System.out.println("Status: " + appointment.getStatus());
                    System.out.println("Notes: " + appointment.getNotes());
                    System.out.println("--------------------");
                }
            } else {
                System.out.println("No appointments found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while viewing appointments.");
        }
    }

    private static void updateAppointmentDetails(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            System.out.print("Enter Appointment ID to update: ");
            long appointmentId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            Appointment appointment = session.get(Appointment.class, appointmentId);

            if (appointment == null) {
                System.out.println("Appointment not found.");
                return;
            }

            System.out.println("Current Appointment Details:");
            System.out.println(appointment);

            System.out.print("Enter New Appointment Date (yyyy-MM-dd) (or press Enter to skip): ");
            String newAppointmentDateStr = scanner.nextLine();
            if (!newAppointmentDateStr.isEmpty()) {
                try {
                    Date newAppointmentDate = parseDate(newAppointmentDateStr);
                    appointment.setAppointmentDate(newAppointmentDate);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Appointment date not updated.");
                }
            }

            // Repeat similar steps for other fields

            session.update(appointment);
            transaction.commit();

            System.out.println("Appointment details updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void deleteAppointment(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            System.out.print("Enter Appointment ID to delete: ");
            long appointmentId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            Appointment appointment = session.get(Appointment.class, appointmentId);

            if (appointment == null) {
                System.out.println("Appointment not found.");
                return;
            }

            session.delete(appointment);
            transaction.commit();

            System.out.println("Appointment deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void manageDiagnoses(SessionFactory sessionFactory, Scanner scanner) {
        // Implement diagnosis management here...
    	boolean exit = false;

        while (!exit) {
            System.out.println("Diagnosis Management");
            System.out.println("1. Add Diagnosis");
            System.out.println("2. View Diagnoses");
            System.out.println("3. Update Diagnosis");
            System.out.println("4. Delete Diagnosis");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addDiagnosis(sessionFactory, scanner);
                    break;
                case 2:
                    viewDiagnoses(sessionFactory);
                    break;
                case 3:
                    updateDiagnosisDetails(sessionFactory, scanner);
                    break;
                case 4:
                    deleteDiagnosis(sessionFactory, scanner);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    private static void addDiagnosis(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Gather necessary information from the user
            // For example:
            System.out.print("Enter patient ID: ");
            long patientId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter doctor ID: ");
            long doctorId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter diagnosis date (yyyy-MM-dd): ");
            String diagnosisDateStr = scanner.nextLine();
            Date diagnosisDate = parseDate(diagnosisDateStr);

            System.out.print("Enter medical condition: ");
            String medicalCondition = scanner.nextLine();

            System.out.print("Enter diagnosis notes: ");
            String notes = scanner.nextLine();

            // Create a new Diagnosis instance and set its properties
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setPatient(session.get(Patient.class, patientId));
            diagnosis.setDoctor(session.get(Doctor.class, doctorId));
            diagnosis.setDiagnosisDate(diagnosisDate);
            diagnosis.setMedicalCondition(medicalCondition);
            diagnosis.setNotes(notes);

            // Save the new diagnosis record
            session.save(diagnosis);
            
            transaction.commit();
            System.out.println("Diagnosis added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void viewDiagnoses(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            // Query to retrieve all diagnoses
            String hql = "FROM Diagnosis";
            Query<Diagnosis> query = session.createQuery(hql, Diagnosis.class);
            List<Diagnosis> diagnoses = query.list();

            if (diagnoses.isEmpty()) {
                System.out.println("No diagnoses found.");
            } else {
                System.out.println("List of Diagnoses:");
                for (Diagnosis diagnosis : diagnoses) {
                    System.out.println("Diagnosis ID: " + diagnosis.getId());
                    System.out.println("Patient: " + diagnosis.getPatient().getFirstName() + " " + diagnosis.getPatient().getLastName());
                    System.out.println("Doctor: " + diagnosis.getDoctor().getFirstName() + " " + diagnosis.getDoctor().getLastName());
                    System.out.println("Diagnosis Date: " + diagnosis.getDiagnosisDate());
                    System.out.println("Medical Condition: " + diagnosis.getMedicalCondition());
                    System.out.println("Notes: " + diagnosis.getNotes());
                    System.out.println("----------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void updateDiagnosisDetails(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter Diagnosis ID: ");
            long diagnosisId = Long.parseLong(scanner.nextLine());

            Diagnosis diagnosis = session.get(Diagnosis.class, diagnosisId);

            if (diagnosis == null) {
                System.out.println("Diagnosis with ID " + diagnosisId + " not found.");
                return;
            }

            System.out.println("Current Medical Condition: " + diagnosis.getMedicalCondition());
            System.out.print("Enter New Medical Condition: ");
            String newMedicalCondition = scanner.nextLine();
            diagnosis.setMedicalCondition(newMedicalCondition);

            System.out.println("Current Notes: " + diagnosis.getNotes());
            System.out.print("Enter New Notes: ");
            String newNotes = scanner.nextLine();
            diagnosis.setNotes(newNotes);

            Transaction transaction = session.beginTransaction();
            session.update(diagnosis);
            transaction.commit();

            System.out.println("Diagnosis details updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void deleteDiagnosis(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter Diagnosis ID: ");
            long diagnosisId = Long.parseLong(scanner.nextLine());

            Diagnosis diagnosis = session.get(Diagnosis.class, diagnosisId);

            if (diagnosis == null) {
                System.out.println("Diagnosis with ID " + diagnosisId + " not found.");
                return;
            }

            Transaction transaction = session.beginTransaction();
            session.delete(diagnosis);
            transaction.commit();

            System.out.println("Diagnosis with ID " + diagnosisId + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void manageBillings(SessionFactory sessionFactory, Scanner scanner) {
        // Implement billing management here...
    	 while (true) {
    	        System.out.println("\nBilling Management Menu:");
    	        System.out.println("1. Add Billing");
    	        System.out.println("2. View Billings");
    	        System.out.println("3. Update Billing Details");
    	        System.out.println("4. Delete Billing");
    	        System.out.println("5. Back to Main Menu");
    	        System.out.print("Enter your choice: ");

    	        int choice = scanner.nextInt();
    	        scanner.nextLine(); // Consume the newline character
    	        
    	        switch (choice) {
    	            case 1:
    	                addBilling(sessionFactory, scanner);
    	                break;
    	            case 2:
    	                viewBillings(sessionFactory);
    	                break;
    	            case 3:
    	                updateBillingDetails(sessionFactory, scanner);
    	                break;
    	            case 4:
    	                deleteBilling(sessionFactory, scanner);
    	                break;
    	            case 5:
    	                return; // Return to main menu
    	            default:
    	                System.out.println("Invalid choice. Please enter a valid option.");
    	        }
    	    }
    }
    private static void addBilling(SessionFactory sessionFactory, Scanner scanner) {
        Transaction transaction = null;
        
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            System.out.println("Enter Patient ID: ");
            Long patientId = scanner.nextLong();
            
            // Retrieve the corresponding patient
            Patient patient = session.get(Patient.class, patientId);
            
            // Gather billing details from the user
            System.out.println("Enter Description: ");
            String description = scanner.nextLine();
            
            System.out.println("Enter Amount: ");
            BigDecimal amount = scanner.nextBigDecimal();
            
            System.out.println("Enter Billing Date (yyyy-MM-dd): ");
            String billingDateStr = scanner.next();
            Date billingDate = parseDate(billingDateStr);
            
            System.out.println("Enter Payment Status: ");
            String paymentStatus = scanner.next();
            
            Billing billing = new Billing();
            billing.setPatient(patient);
            billing.setDescription(description);
            billing.setAmount(amount);
            billing.setBillingDate(billingDate);
            billing.setPaymentStatus(paymentStatus);
            
            session.save(billing);
            
            transaction.commit();
            System.out.println("Billing added successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("An error occurred while adding billing.");
        }
    }
    private static void viewBillings(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Billing";
            Query<Billing> query = session.createQuery(hql, Billing.class);
            
            List<Billing> billings = query.list();
            
            System.out.println("Billing Entries:");
            for (Billing billing : billings) {
                System.out.println("Billing ID: " + billing.getId());
                System.out.println("Patient Name: " + billing.getPatient().getFirstName() + " " + billing.getPatient().getLastName());
                System.out.println("Description: " + billing.getDescription());
                System.out.println("Amount: " + billing.getAmount());
                System.out.println("Billing Date: " + billing.getBillingDate());
                System.out.println("Payment Status: " + billing.getPaymentStatus());
                System.out.println("--------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while viewing billings.");
        }
    }
    private static void updateBillingDetails(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter Billing ID to update: ");
            long billingId = scanner.nextLong();
            scanner.nextLine(); // Consume the newline character
            
            Billing billing = session.get(Billing.class, billingId);
            
            if (billing != null) {
                System.out.println("Updating billing details for Billing ID: " + billingId);
                
                System.out.print("Enter new Description: ");
                String newDescription = scanner.nextLine();
                billing.setDescription(newDescription);
                
                System.out.print("Enter new Amount: ");
                BigDecimal newAmount = scanner.nextBigDecimal();
                billing.setAmount(newAmount);
                
                System.out.print("Enter new Payment Status: ");
                String newPaymentStatus = scanner.next();
                billing.setPaymentStatus(newPaymentStatus);
                
                Transaction transaction = session.beginTransaction();
                session.update(billing);
                transaction.commit();
                
                System.out.println("Billing details updated successfully.");
            } else {
                System.out.println("Billing with ID " + billingId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating billing details.");
        }
    }
    private static void deleteBilling(SessionFactory sessionFactory, Scanner scanner) {
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter Billing ID to delete: ");
            long billingId = scanner.nextLong();
            scanner.nextLine(); // Consume the newline character
            
            Billing billing = session.get(Billing.class, billingId);
            
            if (billing != null) {
                Transaction transaction = session.beginTransaction();
                session.delete(billing);
                transaction.commit();
                
                System.out.println("Billing with ID " + billingId + " deleted successfully.");
            } else {
                System.out.println("Billing with ID " + billingId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while deleting billing.");
        }
    }
    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure() // Load hibernate.cfg.xml by default
                .build();

        return new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }

    private static boolean checkLogin(String username, String password, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Login WHERE username = :username AND password = :password";
            Query<Login> query = session.createQuery(hql, Login.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            List<Login> result = query.list();

            return !result.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    }
}
