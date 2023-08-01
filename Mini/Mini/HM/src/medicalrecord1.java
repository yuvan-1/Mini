// medicalrecord1.java
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Scanner;

public class medicalrecord1 {
    static int id, age, wbc, rbc;
    static String Pname, gender, address, type, doc, bg, bp, diabetes, specialist, Symptoms, Dname;
    static Time ATime;
    static long phone, Phone;
    static boolean EOF = true;

    public static void medicalrecord() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("____________________________________________________________");
        System.out.println();
        System.out.println("\t\t Press 1--------------Medical Details");
        System.out.println("\t\t Press 2--------------Patient Details");
        // Other menu options...

        int ch = Integer.parseInt(sc.nextLine());
        switch (ch) {
            case 1:
                System.out.println("\t\t Retrieve Medical Details");
                showMedicalDetails();
                break;
            case 2:
                System.out.println("\t\t Patient Details");
                System.out.println("\t\t Press1-------------Find by Patient Name");
                System.out.println("\t\t Press2-------------Find by Patient Phone");
                int n = sc.nextInt();
                if (n == 1) {
                    System.out.println("\t\t Enter the patient name");
                    Pname = sc.nextLine();
                    retrievePatientDetailsFromMedical(Pname);
                    break;
                }
                if (n == 2) {
                    System.out.println("\t\t Enter the patient phone number");
                    Phone = sc.nextLong();
                    retrievePatientDetailsFromMedical(Phone);
                    break;
                }

                if (n != 1 && n != 2) {
                    System.exit(0);
                }
                // Other menu options...
        }
    }

    private static void showMedicalDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String selectSql = "SELECT * FROM medical";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String Dname = resultSet.getString("Dname");
                String Symptoms = resultSet.getString("Symptoms");
                long phone = resultSet.getLong("phone");

                // Do something with the retrieved data (e.g., print it)

                System.out.println(String.format("ID: %-5s", id));
                System.out.println(String.format("Name: %-20s", Pname));
                System.out.println(String.format("Age: %-5s", age));
                System.out.println(String.format("Gender: %-10s", gender));
                System.out.println(String.format("Doctor's Name: %-20s", Dname));
                System.out.println(String.format("Symptoms: %-20s", Symptoms));
                System.out.println(String.format("Phone: %-20s", phone));
                System.out.println("________________________________");
            }
            // Close resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving medical details from the database.");
        }
    }

    private static void retrievePatientDetailsFromMedical(String name) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";

            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create the SQL select statement with the JOIN operation
            String selectSql = "SELECT p.* FROM patient p " +
                    "JOIN medical m ON p.Pname = m.name " +
                    "WHERE m.name = ?";

            // Prepare the statement and set the parameter
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, name);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("Pname");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String address = resultSet.getString("address");
                long phone = resultSet.getLong("phone");

                // Do something with the retrieved data (e.g., print it)
                System.out.println("ID: " + id);
                System.out.println("Name: " + Pname);
                System.out.println("Age: " + age);
                System.out.println("Gender: " + gender);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
                System.out.println("________________________________");
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving patient details from the database.");
        }
    }

    private static void retrievePatientDetailsFromMedical(long Phone) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";

            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create the SQL select statement with the JOIN operation
            String selectSql = "SELECT p.* FROM patient p " +
                    "JOIN medical m ON p.phone = m.phone " +
                    "WHERE m.phone = ?";

            // Prepare the statement and set the parameter
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, Phone);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("Pname");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String address = resultSet.getString("address");
                long phone = resultSet.getLong("phone");

                // Do something with the retrieved data (e.g., print it)
                System.out.println("ID: " + id);
                System.out.println("Name: " + Pname);
                System.out.println("Age: " + age);
                System.out.println("Gender: " + gender);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
                System.out.println("________________________________");
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving patient details from the database.");
        }
    }

    
}
