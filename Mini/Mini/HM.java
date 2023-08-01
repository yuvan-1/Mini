import java.util.*;
import java.io.IOException;
import java.sql.*;

class HM {
    int id, age, wbc, rbc;
    String Pname, gender, address, sym, type, doc, bg, bp, diabetes;
    long phone;
    boolean EOF = true;

    public void mainmenu() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("____________________________________________________________");
        System.out.println();
        System.out.println("\t\t Press 1--------------Patient Registration");
        // Other menu options...

        int ch = Integer.parseInt(sc.nextLine());
        switch (ch) {
            case 1:
                while (EOF) {
                    System.out.println("\t\t Press 1--------------Enter Patient Details");
                    // Other sub-menu options...
                    System.out.println();
                    int choose = Integer.parseInt(sc.nextLine());
                    switch (choose) {
                        case 1:
                            System.out.println("________________________________");
                            System.out.println("Enter the Patient id");
                            id = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                            System.out.println("Enter the Patient name");
                            Pname = sc.nextLine();
                            System.out.println("Enter the Patient age");
                            age = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                            System.out.println("Enter the Patient gender");
                            gender = sc.nextLine();
                            System.out.println("Enter the Patient Phone Number");
                            phone = sc.nextLong();
                            sc.nextLine(); // Consume the newline character
                            System.out.println("Enter the Patient Address");
                            address = sc.nextLine();

                            // Store patient details in the database
                            savePatientDetails();

                            System.out.println("\t\tDo You Want To Apply One More Application(y/n)");
                            String z = sc.nextLine();
                            if (z.equalsIgnoreCase("n")) {
                                break;
                            } else {
                                continue;
                            }
                            // Other sub-menu options...
                    }
                }
        }
    }

    // Method to store patient details in the database
    private void savePatientDetails() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // JDBC connection details
            String jdbcUrl = "jdbc:mysql://localhost:3306/hms";
            String username = "root";
            String password = "tiger";

            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create the SQL insert statement
            String insertSql = "INSERT INTO patients (id, Pname, age, gender, phone, address) VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the statement and set parameters
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, Pname);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender);
            preparedStatement.setLong(5, phone);
            preparedStatement.setString(6, address);

            // Execute the insert statement
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();

            System.out.println("Patient details saved to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving patient details to the database.");
        }
    }

    public static void main(String args[]) throws IOException {
        boolean EOF = true;
        Scanner sc = new Scanner(System.in);
        HM obj = new HM();
        while (EOF) {
            System.out.println("\t\tPress 1-------------Patient Registration");
            System.out.println("\t\tPress 2-------------Patient Appointments");
            System.out.println("\t\tPress 3-------------Patient Health Record");
            System.out.println("\t\tPress 4-------------Exit");
            int ta=Integer.parseInt(sc.nextLine());
            if(ta==1) {
                obj.mainmenu();
            }
            if(ta==4)
            {
                System.exit(0);
            }
            break;
        }
    }
}
