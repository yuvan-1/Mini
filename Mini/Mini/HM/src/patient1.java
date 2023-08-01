import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Scanner;

public class patient1 {

    static int id, age, wbc, rbc;
    static String Pname, gender, address, type, doc, bg, bp, diabetes, specialist, Symptoms, Dname;
    static Time ATime;
    static long phone;
    static boolean EOF = true;

    public static void patient() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("____________________________________________________________");
        System.out.println();
        System.out.println("\t\t Press 1--------------Patient Registration");
        System.out.println("\t\t Press 2--------------Patient Details");
        System.out.println("\t\t Press 3--------------Update Patient Details");
        System.out.println("\t\t Press 4--------------Delete Patient Details");
        System.out.println("\t\t Press 5--------------Exit");
        // Other menu options...

        int ch = Integer.parseInt(sc.nextLine());
        switch (ch) {
            case 1:
                System.out.println("\t\t Enter Patient Details");
                while (EOF) {
                    System.out.println();
                    System.out.println("________________________________");
                    System.out.println("Enter the Patient name");
                    Pname = sc.nextLine();
                    System.out.println("Enter the Patient age");
                    age = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the Patient gender");
                    gender = sc.nextLine();
                    System.out.println("Enter the Patient Phone Number");
                    phone = sc.nextLong();
                    sc.nextLine();
                    System.out.println("Enter the Patient Address");
                    address = sc.nextLine();
                    savePatientDetails();

                    System.out.println("\t\tDo You Want To Apply One More Application(y/n)");
                    String z = sc.nextLine();
                    if (z.equalsIgnoreCase("n")) {
                        patient();
                    } else {
                        continue;
                    }
                }
                break;
            case 2:
                System.out.println("\t\t Retrieve Patient Details");
                // Retrieve patient details from the database
                retrievePatientDetails();
                patient();
            case 3:
                System.out.println("\t\t Update Patient Details");
                updatePatientDetails();
                patient();
            case 4:
                System.out.println("\t\t Delete Patient Details");
                deletePatient();
                patient();

            case 5:
                System.exit(0);
                // Other menu options...
        }
    }
    private static void savePatientDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String insertSql = "INSERT INTO patient ( Pname, age, gender, phone, address) VALUES ( ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, Pname);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setLong(4, phone);
            preparedStatement.setString(5, address);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Patient details saved to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving patient details to the database.");
        }
    }

    private static void retrievePatientDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String selectSql = "SELECT * FROM patient";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Pname = resultSet.getString("Pname");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                long phone = resultSet.getLong("phone");
                String address = resultSet.getString("address");
                System.out.println("ID: " + id);
                System.out.println("Name: " + Pname);
                System.out.println("Age: " + age);
                System.out.println("Gender: " + gender);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
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

    private static void updatePatientDetails() throws ClassNotFoundException {
        try {
            Scanner sc = new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");

            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";

            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Display current patient details
            retrievePatientDetails();
            System.out.println("Enter the ID of the patient whose details you want to update:");
            int idToUpdate = Integer.parseInt(sc.nextLine());

            String selectSql = "SELECT * FROM patient WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, idToUpdate);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Current Patient Details:");
                System.out.println("Name: " + resultSet.getString("Pname"));
                System.out.println("Age: " + resultSet.getInt("age"));
                System.out.println("Gender: " + resultSet.getString("gender"));
                System.out.println("Phone: " + resultSet.getLong("phone"));
                System.out.println("Address: " + resultSet.getString("address"));

                // Prompt for updated details
                System.out.println("Enter updated Patient name");
                Pname = sc.nextLine();
                System.out.println("Enter updated Patient age");
                age = Integer.parseInt(sc.nextLine());
                System.out.println("Enter updated Patient gender");
                gender = sc.nextLine();
                System.out.println("Enter updated Patient Phone Number");
                phone = Long.parseLong(sc.nextLine());
                System.out.println("Enter updated Patient Address");
                address = sc.nextLine();

                // Create the SQL update statement
                String updateSql = "UPDATE patient SET Pname=?, age=?, gender=?, phone=?, address=? WHERE id=?";

                // Prepare the statement and set parameters
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, Pname);
                updateStatement.setInt(2, age);
                updateStatement.setString(3, gender);
                updateStatement.setLong(4, phone);
                updateStatement.setString(5, address);
                updateStatement.setInt(6, idToUpdate);

                // Execute the update statement
                int rowsAffected = updateStatement.executeUpdate();

                // Check if the update was successful
                if (rowsAffected > 0) {
                    System.out.println("Patient details updated successfully!");
                } else {
                    System.out.println("Error updating patient details.");
                }

                updateStatement.close();
            } else {
                System.out.println("Patient with ID " + idToUpdate + " not found.");
            }

            // Close resources
            resultSet.close();
            selectStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating patient details.");
        }
    }

    public static void deletePatient() throws ClassNotFoundException {
        try {
            Scanner sc = new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            retrievePatientDetails();
            System.out.println("Enter the ID of the patient you want to delete:");
            int idToDelete = Integer.parseInt(sc.nextLine());
            String deleteSql = "DELETE FROM patient WHERE id=?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, idToDelete);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient with ID " + idToDelete + " deleted successfully!");
            } else {
                System.out.println("Patient with ID " + idToDelete + " not found.");
            }
            deleteStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting patient.");
        }
    }

}
