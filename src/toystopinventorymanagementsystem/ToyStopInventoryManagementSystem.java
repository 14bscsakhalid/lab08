package toystopinventorymanagementsystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author Fahad Satti
 */
public class ToyStopInventoryManagementSystem implements java.io.Serializable {

    
    ToyStopService tsService = new ToyStopService();

    public void init() {

        tsService.initEmployees();
        tsService.initStores();
        tsService.initToys();
        System.out.println("Init complete");

    }

    /**
     * @param args the command line arguments
     *
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String URL = "jdbc:mysql://localhost/lab8";
        String user = "root";
        String pass = "root";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, user, pass);
        
        ToyStopInventoryManagementSystem tsims = new ToyStopInventoryManagementSystem();
        System.out.println("RUN Init? 1-Yes  2-No");
        Scanner scan = new Scanner(System.in);
        int in = scan.nextInt();
        if (in == 1) {
            tsims.init();
        }
        if (in == 0) {
            tsims.loadData(conn);
        }

        tsims.showMenu();
        int m = scan.nextInt();
        switch (m) {
            case 1:
                tsims.printAll();
                break;
            case 2:
                tsims.addStore();
                break;
            case 3:
                tsims.addEmployee();
                break;

            case 9:
                tsims.loadData(conn);
                break;
            case 0:
                tsims.saveData(conn);
                break;

            default:
                System.out.println("Invalid input");
        }
    }

    private void showMenu() {
        System.out.println("Welcome to Toy Stop Inventory Management System");
        System.out.println("Enter 1 to show all data");
        System.out.println("Enter 2 to add a new Store");
        System.out.println("Enter 3 to add a new Employee");
        System.out.println("Enter 4 to add a new Toy");
        System.out.println("Enter 5 to Simulate one day");
        System.out.println("Enter 9 to load state");
        System.out.println("Enter 0 to save state");
    }

    private void printAll() {
        System.out.println(this.tsService.stores);
    }

    private int addStore() {
        return tsService.addStore();
    }

    private int addEmployee() {
        return tsService.addEmployee();
    }

    private void loadData(Connection conn) throws SQLException {
        try {
            //code for obtaining data from database and into memory
            //
            //
            //
            //
            
            
            FileInputStream fileIn = new FileInputStream("stores_data.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tsService.stores = (ArrayList<Store>) in.readObject();
            in.close();
            fileIn.close();
            FileInputStream fileIn1 = new FileInputStream("employee_data.ser");
            ObjectInputStream in1 = new ObjectInputStream(fileIn1);
            tsService.employees = (ArrayList<Employee>) in1.readObject();
            in1.close();
            fileIn1.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Error!");
            c.printStackTrace();
            return;
        }
    }

    private void saveData(Connection conn) throws SQLException {
        try {
            String sql = "INSERT INTO lab8.employee VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, null);
            stmt.setString(2, null);
            stmt.setString(3, null);
            stmt.setInt(4, 0);
            stmt.executeUpdate(sql);
            
            String sql1 = "INSERT INTO lab8.store VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            stmt1.setInt(1, 0);
            stmt1.setString(2, null);
            stmt1.setString(3, null);
            stmt1.setString(4, null);
            stmt1.setString(5, null);
            stmt1.executeUpdate(sql1);
            
            
            FileOutputStream fileOut = new FileOutputStream("stores_data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.tsService.stores);
            out.close();
            fileOut.close();
            FileOutputStream fileOut1 = new FileOutputStream("employee_data.ser");
            ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
            out1.writeObject(this.tsService.employees);
            out1.close();
            fileOut1.close();
            System.out.println("Serialized");
        } catch (IOException i) {
            i.printStackTrace();

        }
    }
}
