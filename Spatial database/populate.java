/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.*;
import java.util.StringTokenizer;
public class populate {
    public static void main(String args[]){
        //System.out.println(args[0] + "\n" + args[1] + "\n");
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        } catch (SQLException ex) {
            Logger.getLogger(populate.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(populate.class.getName()).log(Level.SEVERE, null, ex);
        }
        String host = "127.0.0.1";
        String port = "1521";
	String dbName = "orcl";
	String userName = "clara8974";
	String password = "TJTtwn1960";
		// Construct the JDBC URL
        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        try {
            con = DriverManager.getConnection(dbURL, userName, password);
        } catch (SQLException ex) {
            Logger.getLogger(populate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            System.out.println(args.length);
            popPositionData(args[0]);
            popUserData(args[1]);
            HW2Frame hw = new HW2Frame();
            hw.setVisible(true);
            hw.setTitle("Jingtao Tong 5531-9818-66");
            hw.setLocationRelativeTo(null);
        } catch (IOException ex) {
            Logger.getLogger(populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(populate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void popUserData(String filename) throws IOException, SQLException {
        //System.out.println(filename);
        FileInputStream fis = new FileInputStream(filename);
	DataInputStream dis = new DataInputStream(fis);
	BufferedReader br = new BufferedReader(new InputStreamReader(dis));

	String str = null;
	String id = null;
	String name = null;
	int x=0;
	int y=0;
        String number = null;
        String data = null;
	PreparedStatement ps = null;
	ps = con.prepareStatement("DELETE FROM users");
	ps.executeUpdate();
	while ((str = br.readLine()) != null){
            StringTokenizer st = new StringTokenizer(str, ", ");
            while(st.hasMoreTokens()) {
		id = st.nextToken();
		name = st.nextToken();
		x = Integer.valueOf(st.nextToken());
		y = Integer.valueOf(st.nextToken());
                number = st.nextToken();
                data = st.nextToken();
		ps = con.prepareStatement("INSERT INTO users VALUES(?,?,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),?,?)");
		ps.setString(1,id);
		ps.setString(2,name);
		ps.setInt(3,x);
		ps.setInt(4,y);
                ps.setString(5,number);
                ps.setString(6,data);
		ps.executeUpdate();
		con.commit();
            }
	}
    }
    private static void popPositionData(String filename) throws IOException, SQLException {
        FileInputStream fis = new FileInputStream(filename);
	DataInputStream dis = new DataInputStream(fis);
	BufferedReader br = new BufferedReader(new InputStreamReader(dis));

	String str = null;
	String id = null;
	String address = null;
	int x=0;
	int y=0;
        String data = null;
	PreparedStatement ps = null;
	ps = con.prepareStatement("DELETE FROM position");
	ps.executeUpdate();
	while ((str = br.readLine()) != null){
            StringTokenizer st = new StringTokenizer(str, ",");
            while(st.hasMoreTokens()) {
		id = st.nextToken();
		address = st.nextToken();
		x = Integer.valueOf(st.nextToken());
		y = Integer.valueOf(st.nextToken());
                data = st.nextToken();
		ps = con.prepareStatement("INSERT INTO position VALUES(?,?,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),?)");
		ps.setString(1,id);
		ps.setString(2,address);
		ps.setInt(3,x);
		ps.setInt(4,y);
                ps.setString(5,data);
		ps.executeUpdate();
		con.commit();
            }
	}
         //System.out.println(filename);
        
    }
    static Connection con;
    PreparedStatement ps;
}
