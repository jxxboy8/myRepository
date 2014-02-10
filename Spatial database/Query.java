
import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import java.util.StringTokenizer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
public class Query {
    private Connection con;
    private String query;   //save the query sql statement


    public Query() {
        query = "";
        try{
            con = openConnection();
        }
        catch(SQLException e){
            System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
        }
    }
/**
 * open connection with database
 */
   private Connection openConnection() throws SQLException{
        // Load the Oracle database driver
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        String host = "127.0.0.1";
        String port = "1521";
        String dbName = "orcl";
        String userName = "clara8974";
        String password = "TJTtwn1960";

        // Construct the JDBC URL
        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        return DriverManager.getConnection(dbURL, userName, password);
    }
/**
 * close the connection
 */
   private void closeConnection() {
        try{
            con.close();
        }catch (SQLException e) {
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }

   public ArrayList<QueryInfo> queryNearest(boolean DCPointsOn, boolean UsersOn,Point redChoosen, int numberOfTarget){
        String queryDCPoints = "";
        String queryUsers = "";
         ArrayList<QueryInfo> queryList = new ArrayList<QueryInfo>();
         if(DCPointsOn == true){
            queryDCPoints += "SELECT p.p_id, p.address, p.Location.SDO_POINT.X AS x, p.Location.SDO_POINT.Y AS y, p.mechanism FROM position p, (SELECT p1.p_id, SDO_GEOM.SDO_DISTANCE(p1.Location, ";
            queryDCPoints += "MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE(" + redChoosen.getX() + "," + redChoosen.getY() + ", NULL), NULL, NULL), 0.01) AS dist ";
            queryDCPoints += "FROM position p1 ORDER BY dist) tmp WHERE p.p_id = tmp.p_id AND ROWNUM <= " + numberOfTarget;
            try{
                //System.out.println(queryDCPoints);
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(queryDCPoints);
            while (results.next()){
                queryList.add(new QueryInfo(results.getString("p_id"), results.getString("address"), results.getInt("x"), results.getInt("y"), null, results.getString("mechanism"), false));

            }
            if(UsersOn == false)
                this.query += queryDCPoints + "\n";
            else{
                this.query += "Query 1: " + queryDCPoints + "\n";
            }
            results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table positions for range: " + e.getMessage());
            }
        }
        if(UsersOn == true){
            queryUsers += "SELECT u.user_id, u.u_name, u.Location.SDO_POINT.X AS x, u.Location.sdo_point.y as y, u.u_phone, u.mechanism FROM users u, (SELECT u1.user_id, SDO_GEOM.SDO_DISTANCE(u1.Location, ";
            queryUsers += "MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE(" + redChoosen.getX() + "," + redChoosen.getY() + ", NULL), NULL, NULL), 0.01) AS dist ";
            queryUsers += "FROM users u1 ORDER BY dist) tmp WHERE u.user_id = tmp.user_id AND ROWNUM <=" + numberOfTarget;
            try{
                System.out.println(queryUsers);
                Statement statement = con.createStatement();
                ResultSet results = statement.executeQuery(queryUsers);
                while (results.next()){
                    queryList.add(new QueryInfo(results.getString("user_id"), results.getString("u_name"), results.getInt("x"), results.getInt("y"), results.getString("u_phone"), results.getString("mechanism"), true));

                }
                if(DCPointsOn == false)
                    this.query += queryUsers + "\n";
                else
                    this.query += "Query 2: " + queryUsers + "\n";
                results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table users for range: " + e.getMessage());
            }
        }
        return queryList;
   }

    public ArrayList<QueryInfo> queryRange(boolean DCPointsOn, boolean UsersOn, Point rangeChoosen1, Point rangeChoosen2){
        String queryDCPoints = "";
        String queryUsers = "";
        ArrayList<QueryInfo> queryList = new ArrayList<QueryInfo>();
        if(DCPointsOn == true){
            queryDCPoints += "select p.p_id, p.address, p.Location.SDO_POINT.X AS x, p.Location.SDO_POINT.Y AS y, p.mechanism from position p ";
            queryDCPoints += "where sdo_relate(p.Location, mdsys.sdo_geometry( 2003, NULL, NULL, mdsys.sdo_elem_info_array(1,1003,3),";
            queryDCPoints += "mdsys.sdo_ordinate_array(" + rangeChoosen1.getX() + "," + rangeChoosen1.getY() + "," + rangeChoosen2.getX() + "," + rangeChoosen2.getY() + ")),";
            queryDCPoints += "'mask=anyinteract querytype=window')='TRUE'";
            try{
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(queryDCPoints);
            while (results.next()){
                queryList.add(new QueryInfo(results.getString("p_id"), results.getString("address"), results.getInt("x"), results.getInt("y"), null, results.getString("mechanism"), false));
          
            }
            if(UsersOn == false)
                this.query += queryDCPoints + "\n";
            else{
                this.query += "Query 1: " + queryDCPoints + "\n";
            }
            results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table positions for range: " + e.getMessage());
            }
        }
        if(UsersOn == true){
            queryUsers += "select u.user_id, u.u_name, u.Location.SDO_POINT.X AS x, u.Location.sdo_point.y as y, u.u_phone, u.mechanism from users u ";
            queryUsers += "where sdo_relate(u.Location, mdsys.sdo_geometry( 2003, NULL, NULL, mdsys.sdo_elem_info_array(1,1003,3),";
            queryUsers += "mdsys.sdo_ordinate_array(" + rangeChoosen1.getX() + "," + rangeChoosen1.getY() + "," + rangeChoosen2.getX() + "," + rangeChoosen2.getY() + ")),";
            queryUsers += "'mask=anyinteract querytype=window')='TRUE'";
            try{
                Statement statement = con.createStatement();
                ResultSet results = statement.executeQuery(queryUsers);
                while (results.next()){
                    queryList.add(new QueryInfo(results.getString("user_id"), results.getString("u_name"), results.getInt("x"), results.getInt("y"), results.getString("u_phone"), results.getString("mechanism"), true));
                 
                }
                if(DCPointsOn == false)
                    this.query += queryUsers + "\n";
                else
                    this.query += "Query 2: " + queryUsers + "\n";
                results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table users for range: " + e.getMessage());
            }
        }
        return queryList;
    }

    public String querySelected(QueryInfo selected){
        String s = "";
        String querySelected = "";

        if(selected.isDCPoints()){
            querySelected += "select p.p_id, p.address, p.location.SDO_POINT.X AS x, p.location.SDO_POINT.Y AS y, p.mechanism from position p";
            querySelected += " where p.p_id = '" + selected.getId() + "'";
        }
        else{
                            //System.out.println(selected);
            querySelected += "select u.user_id, u.u_name, u.location.SDO_POINT.X AS x, u.location.SDO_POINT.Y AS y, u.u_phone, u.mechanism from users u";
            querySelected += " where u.user_id = '" + selected.getId() + "'";
                        //System.out.println(querySelected);
        }
        try{
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(querySelected);
            while(results.next()){
                if(selected.isDCPoints()){  //DCPoints
                    s += results.getString("p_id") + "\n";
                    s += "Address: " + results.getString("address") + "\n";
                    s += "Location: " + results.getString("x") + ", " + results.getString("y") + "\n";
                    s += "Sensor type: " + results.getString("mechanism") + "\n";
                }
                else{
                    s += "Name: " + results.getString("u_name") + "\n";
                    s += "Location: " + results.getString("x") + ", " + results.getString("y") + "\n";
                    s += "Phone: " + results.getString("u_phone") + "\n";
                    s += "Sensor type: " + results.getString("mechanism") + "\n";

                }
            }
        }
        catch(SQLException e){
            System.err.println("Errors occurs when querying data for specific DCPoints or Users: " + e.getMessage());
        }
        return s;
    }
    public ArrayList<QueryInfo> queryRelevantUsers(QueryInfo selected, int numberOfTarget){
        String queryRelevantUsers = "";
        ArrayList<QueryInfo> queryList = new ArrayList<QueryInfo>();
        ArrayList<String> methodString = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(selected.getMethod(), ";");
        while(st.hasMoreTokens()){
            methodString.add(st.nextToken());
        }
        queryRelevantUsers +="SELECT u1.user_id, u1.u_name, u1.Location.SDO_POINT.X AS x, u1.Location.sdo_point.y as y, u1.u_phone, u1.mechanism FROM users u1, (SELECT u.user_id, SDO_GEOM.SDO_DISTANCE(u.location, p.location, 0.01) AS dist FROM users u, position p ";
        queryRelevantUsers +="WHERE p.p_id = '" +selected.getId() + "' AND (";
        for(int i = 0;i < methodString.size(); i++){
        queryRelevantUsers +="p.mechanism LIKE '%" + methodString.get(i) + "%' AND u.mechanism LIKE '%" + methodString.get(i)+ "%'";
                    if(i != methodString.size() - 1){
                queryRelevantUsers += " OR ";
            }
        }
        queryRelevantUsers +=" ) ORDER BY dist) tmp WHERE u1.user_id = tmp.user_id AND ROWNUM <=" + numberOfTarget;
        //System.out.println(queryRelevantUsers);
               try{
                Statement statement = con.createStatement();
                
                ResultSet results = statement.executeQuery(queryRelevantUsers);
                while (results.next()){
                    queryList.add(new QueryInfo(results.getString("user_id"), results.getString("u_name"), results.getInt("x"), results.getInt("y"), results.getString("u_phone"), results.getString("mechanism"), true));

                }
                    this.query += queryRelevantUsers + "\n";
                results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table users for range: " + e.getMessage());
            }

     return queryList;
    }

    public ArrayList<QueryInfo> queryAssignDCPoint(QueryInfo selected){
        String queryAssignDCPoint = "";
        ArrayList<QueryInfo> queryList = new ArrayList<QueryInfo>();
        ArrayList<String> methodString = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(selected.getMethod(), ";");
        while(st.hasMoreTokens()){
            methodString.add(st.nextToken());
        }
        queryAssignDCPoint += "SELECT p2.p_id, p2.address, p2.Location.SDO_POINT.X AS x, p2.Location.SDO_POINT.Y AS y, p2.mechanism FROM position p2 WHERE p2.p_id IN (SELECT DISTINCT tmp.p_id FROM (SELECT u.user_id, p.p_id,SDO_GEOM.SDO_DISTANCE(u.location, p.location, 0.01) AS dist FROM users u, position p WHERE ";
        for(int i = 0;i < methodString.size(); i++){
            queryAssignDCPoint += "u.mechanism LIKE '%" + methodString.get(i) +"%' AND p.mechanism LIKE '%" + methodString.get(i)+ "%'";
            if(i != methodString.size() - 1){
                queryAssignDCPoint += " OR ";
            }
        }
        queryAssignDCPoint += ") tmp WHERE tmp.user_id = '" + selected.getId() + "' AND tmp.dist <= ALL(SELECT SDO_GEOM.SDO_DISTANCE(u.location, p.location, 0.01) AS dist FROM users u, position p WHERE (";
        for(int i = 0;i < methodString.size(); i++){
        queryAssignDCPoint += "u.mechanism LIKE '%" + methodString.get(i) +"%'AND p.mechanism LIKE '%" + methodString.get(i) + "%'";
            if(i != methodString.size() - 1){
                queryAssignDCPoint += " OR ";
            }
        }
        queryAssignDCPoint += ")AND p.p_id = tmp.p_id))";

        try{
                Statement statement = con.createStatement();
                //System.out.println(queryAssignDCPoint);
                ResultSet results = statement.executeQuery(queryAssignDCPoint);
                while (results.next()){
                    queryList.add(new QueryInfo(results.getString("p_id"), results.getString("address"), results.getInt("x"), results.getInt("y"), null, results.getString("mechanism"), false));

                }
                    this.query += queryAssignDCPoint + "\n";
                results.close();
            }catch(SQLException e){
                System.err.println("Errors occurs when querying data from table users for range: " + e.getMessage());
            }

        return queryList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public static void main(String[] args) {
        Query q = new Query();

        System.out.println("Test 1: ");
        ArrayList<QueryInfo> myList1 = q.queryRange(true, true, new Point(0,0), new Point(300,300));
        for(int i = 0; i < myList1.size(); i++)
            if(myList1.get(i).isDCPoints())
                System.out.println("DCPoints: " + myList1.get(i).getId() + " " + myList1.get(i).getName() + " " + myList1.get(i).getX() + " " + myList1.get(i).getY());
            else
                System.out.println("Users: " + myList1.get(i).getId() + " " + myList1.get(i).getName() + " " + myList1.get(i).getX() + " " + myList1.get(i).getY());
        System.out.println(q.getQuery());
    }
}
