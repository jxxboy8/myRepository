
import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
public class QueryInfo {
    public QueryInfo(){

    }
    
    public QueryInfo(QueryInfo q){
        this.id = q.id;
        this.method = q.method;
        this.name = q.name;
        this.phone = q.phone;
        this.type = q.type;
        this.x = q.x;
        this.y = q.y;
    }

    public QueryInfo(String id, String name, int x, int y, String phone, String method, boolean type){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.phone = phone;
        this.method = method;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isDCPoints() {
        if(this.type == false)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "QueryInfo{" + "id=" + id + "name=" + name + "x=" + x + "y=" + y + "phone=" + phone + "method=" + method + "type=" + type + '}';
    }


    public boolean contains(Point pt){
        int tempX = (int)pt.getX();
        int tempY = (int)pt.getY();
        int xMin = this.x - 8;
        int yMin = this.y - 8;
        int xMax = this.x + 8;
        int yMax = this.y + 8;
        if(tempX >= xMin && tempX <= xMax && tempY >= yMin && tempY <= yMax)
            return true;
        return false;
    }

    public String id;
    public String name;
    public int x;
    public int y;
    public String phone;
    public String method;
    public boolean type;
}
