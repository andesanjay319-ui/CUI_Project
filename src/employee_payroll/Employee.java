package employee_payroll;

import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class Employee implements Serializable {
    private int id;
    private String name;
    private double basicSalary;
    private double hra;
    private double da;
    private double deductions;

    public Employee(int id, String name, double basicSalary, double hra, double da, double deductions) {
        this.id = id;
        this.name = name;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.deductions = deductions;
    }

    public double calculateNetSalary() {
        return basicSalary + hra + da - deductions;
    }

    public String generatePayslip() {
        return "Payslip for Employee ID: " + id +
               "\nName: " + name +
               "\nBasic Salary: " + basicSalary +
               "\nHRA: " + hra +
               "\nDA: " + da +
               "\nDeductions: " + deductions +
               "\nNet Salary: " + calculateNetSalary();
    }

    public int getId() { return id; }
    public String getName() { return name; }
}

 class PayrollSystem {
    private List<Employee> employees = new ArrayList<>();
    private final String filePath = "employees.dat";

    public PayrollSystem() {
        loadEmployees();
    }

    public void addEmployee(Employee emp) throws FileNotFoundException, IOException {
        employees.add(emp);
        saveEmployees();
    }

    public Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) return emp;
        }
        return null;
    }

    public void generatePayslip(int id) throws java.io.IOException {
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            String payslip = emp.generatePayslip();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payslip_" + id + ".txt"))) {
                writer.write(payslip);
                System.out.println("Payslip generated for " + emp.getName());
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

    private void saveEmployees() throws FileNotFoundException, java.io.IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(employees);
        }
    }

    private void loadEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            employees = (List<Employee>) ois.readObject();
        } catch (Exception e) {
            employees = new ArrayList<>();
        }
    }
}

 class Main {
    public static void main(String[] args) throws IOException {
        PayrollSystem system = new PayrollSystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Employee Payroll System ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Generate Payslip");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Basic Salary: ");
                    double basic = sc.nextDouble();
                    System.out.print("Enter HRA: ");
                    double hra = sc.nextDouble();
                    System.out.print("Enter DA: ");
                    double da = sc.nextDouble();
                    System.out.print("Enter Deductions: ");
                    double deductions = sc.nextDouble();

                    Employee emp = new Employee(id, name, basic, hra, da, deductions);
                    system.addEmployee(emp);
                    System.out.println("Employee added successfully.");
                    break;

                case 2:
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    system.generatePayslip(empId);
                    break;

                case 3:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
