import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Scanner;

import java.sql.Statement;

public class appointment1 {
    static int id, age, wbc, rbc;
    static String Pname, gender, address, type, doc, bg, bp, diabetes, specialist, Symptoms, Dname;
    static Time ATime;
    static long phone;
    static boolean EOF = true;

    public static void appointment() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("____________________________________________________________");
        System.out.println();
        System.out.println("\t\t Press 1--------------Appointment Registration");
        System.out.println("\t\t Press 2--------------Appointment Details");
        System.out.println("\t\t Press 3--------------Update Appointment Details");
        System.out.println("\t\t Press 4--------------Delete Appointment Details");
        System.out.println("\t\t Press 5--------------Appointment Based on Doctor Accomodation");
        int ch = Integer.parseInt(sc.nextLine());
        switch (ch) {
            case 1:
                System.out.println("\t\t Enter Appointment Details");
                while (EOF) {
                    System.out.println();
                    System.out.println("________________________________");
                    System.out.println("Enter the Patient name");
                    Pname = sc.nextLine();
                    System.out.println("Enter the Symptoms");
                    Symptoms = sc.nextLine();
                    System.out.println("Enter the Specialist");
                    specialist = sc.nextLine();
                    System.out.println("Enter the Appointment Time (HH:mm:ss)");
                    ATime = Time.valueOf(sc.nextLine());
                    System.out.println("Enter the Phone Number");
                    phone = sc.nextLong();
                    sc.nextLine();
                    System.out.println("Enter the Doctor's Name");
                    Dname = sc.nextLine();
                    System.out.println("Whether the person is disabled or not (y/n): ");
                    char c = sc.nextLine().charAt(0);
                    if (c == 'y') {
                        String dis = "YES";
                        SpecialMedicalRecord specialRecord = new SpecialMedicalRecord(id, Pname, age, gender, Dname,
                                Symptoms, phone, dis);
                        specialRecord.printSpecialRecord();
                    } else {
                        saveAppointmentDetails();
                        retrieveMedicalTable();
                    }
                    System.out.println("\t\tDo You Want To Apply One More Application(y/n)");
                    String z = sc.nextLine();
                    if (z.equalsIgnoreCase("n")) {
                        break;
                    } else {
                        continue;
                    }
                }
                break;
            case 2:
                System.out.println("\t\t Retrieve Appointment Details");
                retrieveAppointmentDetails();
                break;
            case 3:
                System.out.println("\t\t Update Appointment Details");
                updateAppointmentDetails();
                retrieveMedicalTable();
                break;
            case 4:
                System.out.println("\t\t Delete Appointment Details");
                deleteAppointmentDetails();
                break;
            case 5:
                System.out.println("\t\t Appointment Based on Doctor Name");
                System.out.println("\t\t Enter the Doctor Name");
                String Dname = sc.nextLine();
                retrieveAppointmentsByDoctor(Dname);
                break;
        }
    }

    private static void updateAppointmentDetails() throws ClassNotFoundException {
        try {
            Scanner sc = new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");

            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";

            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Display current appointment details
            retrieveAppointmentDetails();
            System.out.println("Enter the Name of the patient whose appointment details you want to update:");
            String patientIdToUpdate = sc.nextLine();

            String selectSql = "SELECT * FROM appointment WHERE Pname = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, patientIdToUpdate);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Current Appointment Details:");
                System.out.println(String.format("Patient Name: %-20s", resultSet.getString("Pname")));
                System.out.println(String.format("Symptoms: %-20s", resultSet.getString("Symptoms")));
                System.out.println(String.format("Specialist: %-20s", resultSet.getString("specialist")));
                System.out.println(String.format("Appointment Time: %-20s", resultSet.getTime("ATime")));
                System.out.println(String.format("Phone: %-20s", resultSet.getLong("phone")));
                System.out.println(String.format("Doctor's Name: %-20s", resultSet.getString("Dname")));
                System.out.println("________________________________");
                System.out.println("Enter updated Patient name");
                Pname = sc.nextLine();
                System.out.println("Enter updated Symptoms");
                Symptoms = sc.nextLine();
                System.out.println("Enter updated Specialist");
                specialist = sc.nextLine();
                System.out.println("Enter updated Appointment Time (HH:mm:ss)");
                ATime = Time.valueOf(sc.nextLine());
                System.out.println("Enter updated Phone Number");
                phone = Long.parseLong(sc.nextLine());
                System.out.println("Enter updated Doctor's Name");
                Dname = sc.nextLine();
                String updateSql = "UPDATE appointment SET Pname=?, Symptoms=?, specialist=?, ATime=?, phone=?, Dname=? WHERE Pname=?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, Pname);
                updateStatement.setString(2, Symptoms);
                updateStatement.setString(3, specialist);
                updateStatement.setTime(4, ATime);
                updateStatement.setLong(5, phone);
                updateStatement.setString(6, Dname);
                updateStatement.setString(7, Pname);
                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Appointment details updated successfully!");
                } else {
                    System.out.println("Error updating appointment details.");
                }

                updateStatement.close();
            } else {
                System.out.println("Appointment with patient ID " + patientIdToUpdate + " not found.");
            }
            resultSet.close();
            selectStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating appointment details.");
        }
    }

    private static void saveAppointmentDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String insertSql = "INSERT INTO appointment ( Pname, Symptoms, specialist, ATime, phone,Dname) VALUES (  ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, Pname);
            preparedStatement.setString(2, Symptoms);
            preparedStatement.setString(3, specialist);
            preparedStatement.setTime(4, ATime);
            preparedStatement.setLong(5, phone);
            preparedStatement.setString(6, Dname);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Appointment details saved to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving patient details to the database.");
        }
    }

    private static void retrieveMedicalTable() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String dropSql = "DROP TABLE medical";
            Statement dropStatement = connection.createStatement();
            dropStatement.executeUpdate(dropSql);

            // Create the SQL create statement to create a new medical table with the latest
            // data
            String createSql = "CREATE TABLE medical AS " +
                    "SELECT a.id, a.Pname AS name, b.age, b.gender, a.Dname, a.Symptoms, b.phone " +
                    "FROM appointment a " +
                    "JOIN patient b ON a.Pname = b.Pname AND a.phone = b.phone";

            // Prepare the statement and execute the create query
            Statement createStatement = connection.createStatement();
            createStatement.executeUpdate(createSql);

            // Close resources
            dropStatement.close();
            createStatement.close();
            connection.close();

            System.out.println("Medical table updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating the medical table.");
        }
    }

    private static void retrieveAppointmentDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String selectSql = "SELECT * FROM appointment";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("Pname");
                String Symptoms = resultSet.getString("Symptoms"); // Corrected the column name
                String specialist = resultSet.getString("specialist");
                Time ATime = resultSet.getTime("ATime");
                long phone = resultSet.getLong("phone");
                String Dname = resultSet.getString("Dname");

                System.out.println("ID: " + id);
                System.out.println(String.format("Patient Name: %-20s", Pname));
                System.out.println(String.format("Symptoms: %-20s", Symptoms));
                System.out.println(String.format("Specialist: %-20s", specialist));
                System.out.println(String.format("Appointment Time: %-20s", ATime));
                System.out.println(String.format("Phone: %-20s", phone));
                System.out.println(String.format("Doctor's Name: %-20s", Dname));
                System.out.println("________________________________");
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving patient details from the database.");
        }
    }

    private static void retrieveAppointmentsByDoctor(String doctorName) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String selectSql = "SELECT * FROM appointment WHERE Dname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, doctorName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("Pname");
                String Symptoms = resultSet.getString("Symptoms");
                String specialist = resultSet.getString("specialist");
                Time ATime = resultSet.getTime("ATime");
                long phone = resultSet.getLong("phone");
                String Dname = resultSet.getString("Dname");

                System.out.println("The following are the appointments for the Doctor: " + Dname);
                System.out.println("ID: " + id);
                System.out.println(String.format("Patient Name: %-20s", Pname));
                System.out.println(String.format("Symptoms: %-20s", Symptoms));
                System.out.println(String.format("Specialist: %-20s", specialist));
                System.out.println(String.format("Appointment Time: %-20s", ATime));
                System.out.println(String.format("Phone: %-20s", phone));
                System.out.println(String.format("Doctor's Name: %-20s", Dname));
                System.out.println("________________________________");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving patient details from the database.");
        }
    }

    public static void deleteAppointmentDetails() throws ClassNotFoundException {
        try {
            Scanner sc = new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");

            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            retrieveAppointmentDetails();
            System.out.println("Enter the Name of the patient whose appointment details you want to delete:");
            String patientNameToDelete = sc.nextLine();
            String deleteSql = "DELETE FROM appointment WHERE Pname=?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, patientNameToDelete);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment details for patient " + patientNameToDelete + " deleted successfully!");
            } else {
                System.out.println("Appointment for patient " + patientNameToDelete + " not found.");
            }
            deleteStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting appointment details.");
        }
    }

    public static class SpecialMedicalRecord extends appointment1 {
        private String specialAttribute;

        // Constructor to initialize the fields
        public SpecialMedicalRecord(int id, String name, int age, String gender, String doctorName, String symptoms,
                long phone, String specialAttribute) {
            super(); // Call the constructor of the superclass (medicalrecord1) - not required in
                     // this case, but shown for reference
            medicalrecord1.id = id;
            medicalrecord1.Pname = name;
            medicalrecord1.age = age;
            medicalrecord1.gender = gender;
            medicalrecord1.Dname = doctorName;
            medicalrecord1.Symptoms = symptoms;
            medicalrecord1.phone = phone;
            this.specialAttribute = specialAttribute;
        }

        // Getter method for specialAttribute
        public String getSpecialAttribute() {
            return specialAttribute;
        }

        // Setter method for specialAttribute
        public void setSpecialAttribute(String specialAttribute) {
            this.specialAttribute = specialAttribute;
        }

        public void printSpecialRecord() {
            System.out.println("Special Record:");
            System.out.println("ID: " + id);
            System.out.println("Name: " + Pname);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);
            System.out.println("Doctor's Name: " + Dname);
            System.out.println("Symptoms: " + Symptoms);
            System.out.println("Phone: " + phone);
            System.out.println("Special Attribute: " + specialAttribute);
            System.out.println("________________________________");
        }
    }
}
