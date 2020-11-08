/**
 * @Author: Guillermo Escobero, Alvaro Santos
 * @Date:   08-11-2020
 * @Project: Data Protection Lab
 * @Filename: DB.java
 * @Last modified by:   Guille
 * @Last modified time: 08-11-2020
 */
 package thebookseller;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.LinkedList;
 import java.util.logging.Level;
 import java.util.logging.Logger;

 public class DB {
   private Connection con;

   static String DBlocation = "";

   public DB(String loc) {
     loadDriver();
     DBlocation = loc;
   }

   private void loadDriver() {
     try {
       Class.forName("org.sqlite.JDBC");
     } catch (ClassNotFoundException e) {
       System.out.println("Error al crear el puente JDBC-ODBC");
     }
   }

   public Connection conectar() {
     try {
       this.con = DriverManager.getConnection("jdbc:sqlite:" + DBlocation);
     } catch (SQLException ex) {
       Logger.getLogger(DB.class.getName()).log(Level.SEVERE, (String)null, ex);
     }
     return this.con;
   }

   public boolean closeConecction() {
     try {
       this.con.close();
     } catch (SQLException sqle) {
       return false;
     }
     return true;
   }

   public String[] login(String name) {
     ResultSet rs = null;
     conectar();
     String[] results = new String[2];
     results[0] = "-1";
     results[1] = "-1";
     try {
       Statement stm = this.con.createStatement();
       String id = "";
       if (name.equals("Charles")) {
         id = "1";
       } else if (name.equals("Daniel")) {
         id = "2";
       } else if (name.equals("Tim")) {
         id = "3";
       } else if (name.equals("Anna")) {
         id = "4";
       } else if (name.equals("Admin200")) {
         id = "5";
       }
       String strSQL = "SELECT Pass,IDUser FROM Users WHERE Id=" + id + ";";
       rs = stm.executeQuery(strSQL);
       rs.next();
       results[0] = rs.getString("Pass");
       String userID = rs.getString("IDUser");
       if (userID.equals("Admin200"))
         results[1] = "1";
       rs.close();
       stm.close();
     } catch (SQLException sQLException) {}
     boolean isClosed = closeConecction();
     if (!isClosed)
       System.out.println("Error al cerrar la conexion");
     return results;
   }

   public void updatePlus(String name, int newSales) {
     ResultSet rs = null;
     conectar();
     int sales = 0;
     int plus = 0;
     String id = "";
     try {
       Statement stm = this.con.createStatement();
       if (name.equals("Charles")) {
         id = "1";
       } else if (name.equals("Daniel")) {
         id = "2";
       } else if (name.equals("Tim")) {
         id = "3";
       } else if (name.equals("Anna")) {
         id = "4";
       } else if (name.equals("Admin200")) {
         id = "5";
       }
       String strSQL = "SELECT Sales_euros_year, Plus_euros_year from Sales WHERE Id=" + id + ";";
       rs = stm.executeQuery(strSQL);
       rs.next();
       sales = Integer.parseInt(rs.getString("Sales_euros_year"));
       plus = Integer.parseInt(rs.getString("Plus_euros_year"));
       sales += newSales;
       plus += newSales;
       rs.close();
       stm.close();
     } catch (SQLException sQLException) {}
     finalUpdate(sales, plus, id);
     boolean isClosed = closeConecction();
     if (!isClosed)
       System.out.println("Error al cerrar la conexion");
   }

   public void finalUpdate(int sales, int plus, String id) {
     conectar();
     try {
       String strSQL;
       Statement stm = this.con.createStatement();
       if (sales > 0) {
         strSQL = "UPDATE Sales SET Sales_euros_year=" + sales + ", Plus_euros_year=" + plus + " WHERE Id=" + id + ";";
       } else {
         strSQL = "UPDATE Sales SET Plus_euros_year=" + plus + " WHERE Id=" + id + ";";
       }
       stm.executeUpdate(strSQL);
       stm.close();
     } catch (SQLException sQLException) {}
     boolean isClosed = closeConecction();
     if (!isClosed)
       System.out.println("Error al cerrar la conexion");
   }

   public LinkedList<LinkedList<String>> selectAll() {
     ResultSet rs = null;
     conectar();
     LinkedList<LinkedList<String>> allSales = new LinkedList<>();
     try {
       Statement stm = this.con.createStatement();
       String strSQL = "SELECT Name, Profile, Sales_euros_year, Plus_euros_year, Surname FROM Sales;";
       rs = stm.executeQuery(strSQL);
       while (rs.next()) {
         LinkedList<String> list = new LinkedList<>();
         list.add(rs.getString("Name"));
         list.add(rs.getString("Surname"));
         list.add(rs.getString("Profile"));
         list.add(rs.getString("Sales_euros_year"));
         list.add(rs.getString("Plus_euros_year"));
         allSales.add(list);
       }
       rs.close();
       stm.close();
     } catch (SQLException sQLException) {}
     boolean isClosed = closeConecction();
     if (!isClosed)
       System.out.println("Error al cerrar la conexion");
     return allSales;
   }
 }
