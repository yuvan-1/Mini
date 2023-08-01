import java.util.*;
import java.io.IOException;
import java.sql.*;

abstract class newUser {
    abstract public void insert() throws Exception;

}

class Create extends newUser {
    private String name;
    private String pwd;

    Create() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void insert() throws Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "tiger");
        String q2 = "insert into users(user_name,password) values(?,?)";
        PreparedStatement st = con.prepareStatement(q2);
        st.setString(1, name);
        st.setString(2, pwd);
        st.executeUpdate();
        con.close();
    }

}

class HM {
    static int id, age, wbc, rbc;
    static String Pname, gender, address, type, doc, bg, bp, diabetes, specialist, Symptoms, Dname;
    static Time ATime;
    static long phone;
    static boolean EOF = true;
    private static String username = null;
    private static String password = null;

    public static void display() throws Exception {
        boolean EOF = true;
        Scanner sc = new Scanner(System.in);
        System.out.println("\t\tPress 1-------------Patient Registration");
        System.out.println("\t\tPress 2-------------Patient Appointments");
        System.out.println("\t\tPress 3-------------Patient Health Record");
        System.out.println("\t\tPress 4-------------Exit");
        int ta = Integer.parseInt(sc.nextLine());
        do {
            if (ta == 1) {
                patient1 p = new patient1();
                p.patient();
                break;
            }
            if (ta == 2) {
                appointment1 a = new appointment1();
                a.appointment();
                break;
            }
            if (ta == 3) {
                medicalrecord1 m = new medicalrecord1();
                m.medicalrecord();
                break;
            }
            if (ta == 4) {
                System.exit(0);
            }

        } while (EOF);
    }

    public static void main(String args[]) throws SQLException, Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "------------------------------------\nHOSPITAL MANAGEMENT SYSTEM\n------------------------------------\n\n");
        System.out.println("Login (l) or SignUp? (s)");
        Create create = new Create();
        if (sc.next().charAt(0) == 's') {
            sc.nextLine();
            System.out.print("Enter Username: ");
            String nuser = sc.nextLine();
            create.setName(nuser);
            System.out.print("Enter Password: ");
            String pwd = sc.nextLine();
            create.setPwd(pwd);
            create.insert();
            System.out.println("User created ");
        }

        System.out.println("LOGIN");
        sc.nextLine();
        System.out.println("****************************");
        System.out.print("Enter Username: ");
        username = sc.nextLine();
        System.out.print("Enter Password: ");
        password = sc.nextLine();

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "tiger");

        System.out.println("****************************");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from users");
        // try{
        while (rs.next()) {
            if (password.equals(rs.getString(2)) && username.equals(rs.getString(1))) {
                System.out.println("Welcome again " + rs.getString(1) + " !");
                System.out.println("________________________");
                display();
                break;
            }
        }
        // }catch(Exception e){
        // System.out.println("Thank You !");
        // System.exit(0);
        // }
        rs.close();
        con.close();
        sc.close();
    }
}
