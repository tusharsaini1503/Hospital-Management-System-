import java.util.*;

class TreeNode {
    Object data;
    TreeNode left;
    TreeNode right;

    public TreeNode(Object data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

class Department {
    String name;
    TreeNode root;

    public Department(String name) {
        this.name = name;
        this.root = null;
    }

    public void insert(Object data) {
        root = insertRec(root, data);
    }

    private TreeNode insertRec(TreeNode root, Object data) {
        if (root == null) {
            root = new TreeNode(data);
            return root;
        }

        if (data instanceof Doctor) {
            root.left = insertRec(root.left, data);
        } else if (data instanceof Patient) {
            root.right = insertRec(root.right, data);
        }

        return root;
    }
}

public class HospitalManagementSystem {
    ArrayList<Department> departments;
    Scanner scanner;

    public HospitalManagementSystem() {
        this.departments = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void addDepartment(String name) {
        departments.add(new Department(name));
    }

    public void addDoctorToDepartment(String departmentName, Doctor doctor) {
        for (Department dept : departments) {
            if (dept.name.equals(departmentName)) {
                dept.insert(doctor);
                return;
            }
        }
        System.out.println("Department not found.");
    }

    public void assignPatientToDoctor(String departmentName, int doctorID, Patient patient) {
        for (Department dept : departments) {
            if (dept.name.equals(departmentName)) {
                assignPatientToDoctorRec(dept.root, doctorID, patient);
                return;
            }
        }
        System.out.println("Department not found.");
    }

    private void assignPatientToDoctorRec(TreeNode root, int doctorID, Patient patient) {
        if (root == null)
            return;

        if (root.data instanceof Doctor) {
            Doctor doctor = (Doctor) root.data;
            if (doctor.doctorID == doctorID) {
                doctor.assignPatient(patient);
                return;
            }
            assignPatientToDoctorRec(root.left, doctorID, patient);
            assignPatientToDoctorRec(root.right, doctorID, patient);
        }
    }

    public void displayDetails() {
        System.out.println("1. Display Departments");
        System.out.println("2. Display Doctors and Patients");
        System.out.println("Enter your choice:");
        String choice = scanner.next();
        switch (choice) {
            case "1":
                displayDepartments();
                break;
            case "2":
                displayDoctorsAndPatients();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void displayDepartments() {
        System.out.println("Departments:");
        for (Department dept : departments) {
            System.out.println(dept.name);
        }
    }

    private void displayDoctorsAndPatients() {
        for (Department dept : departments) {
            System.out.println("Department: " + dept.name);
            displayDoctorsAndPatients(dept.root);
        }
    }

    private void displayDoctorsAndPatients(TreeNode root) {
        if (root == null)
            return;

        if (root.data instanceof Doctor) {
            Doctor doctor = (Doctor) root.data;
            System.out.println("Doctor: " + doctor.name);
            System.out.println("Patients:");
            for (Patient patient : doctor.patients) {
                System.out.println("- " + patient.name);
            }

            System.out.println("Do you want to display complete details of patients? (yes/no)");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("yes")) {
                for (Patient patient : doctor.patients) {
                    displayPatientDetails(patient);
                }
            }
        }

        displayDoctorsAndPatients(root.left);
        displayDoctorsAndPatients(root.right);
    }

    private void displayPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.patientID);
        System.out.println("Name: " + patient.name);
        System.out.println("Age: " + patient.age);
        System.out.println("Medical Condition: " + patient.medicalCondition);
    }

    public void start() {
        boolean running = true;
        String docDepartment = ""; // Declare docDepartment here

        while (running) {
            System.out.println("\nHospital Management System");
            System.out.println("1. Add Department");
            System.out.println("2. Add Doctor");
            System.out.println("3. Add Patient");
            System.out.println("4. Display Details");
            System.out.println("5. Exit");
            System.out.println("Enter your choice:");
            String choice = scanner.next();

            switch (choice) {
                case "1":
                    System.out.println("Enter department name:");
                    scanner.nextLine(); // Consume newline
                    String departmentName = scanner.nextLine();
                    addDepartment(departmentName);
                    break;
                case "2":
                    System.out.println("Enter doctor ID:");
                    String doctorIDInput = scanner.next();
                    int doctorID = 0;
                    try {
                        doctorID = Integer.parseInt(doctorIDInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid doctor ID. Please enter a valid Doctor ID.");
                        break;
                    }
                    System.out.println("Enter doctor name:");
                    scanner.nextLine(); // Consume newline
                    String doctorName = scanner.nextLine();
                    System.out.println("Enter department name:");
                    docDepartment = scanner.nextLine(); // Assign department here
                    Doctor doctor = new Doctor(doctorID, doctorName, docDepartment);
                    addDoctorToDepartment(docDepartment, doctor);
                    break;
                case "3":
                    System.out.println("Enter patient ID:");
                    int patientID = scanner.nextInt();
                    System.out.println("Enter patient name:");
                    scanner.nextLine(); // Consume newline
                    String patientName = scanner.nextLine();
                    System.out.println("Enter patient age:");
                    int patientAge = scanner.nextInt();
                    System.out.println("Enter patient medical condition:");
                    scanner.nextLine(); // Consume newline
                    String patientCondition = scanner.nextLine();
                    Patient patient = new Patient(patientID, patientName, patientAge, patientCondition);
                    System.out.println("Enter doctor ID to assign the patient:");
                    int assignedDoctorID = scanner.nextInt();
                    assignPatientToDoctor(docDepartment, assignedDoctorID, patient);
                    break;
                case "4":
                    displayDetails();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void main(String[] args) {
        HospitalManagementSystem hospital = new HospitalManagementSystem();
        hospital.start();
    }
}

class Patient {
    int patientID;
    String name;
    int age;
    String medicalCondition;

    public Patient(int id, String name, int age, String condition) {
        this.patientID = id;
        this.name = name;
        this.age = age;
        this.medicalCondition = condition;
    }
}

class Doctor {
    int doctorID;
    String name;
    String department;
    ArrayList<Patient> patients;

    public Doctor(int id, String name, String department) {
        this.doctorID = id;
        this.name = name;
        this.department = department;
        this.patients = new ArrayList<>();
    }

    public void assignPatient(Patient patient) {
        patients.add(patient);
    }
}