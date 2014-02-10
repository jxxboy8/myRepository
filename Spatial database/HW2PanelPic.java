
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
public class HW2PanelPic extends JPanel{
    private Graphics2D g2;
    private Image img;  //img
    private Point rangeChoosen1;  //save the first point to rect
    private Point rangeChoosen2;  //save the second point to rect
    private Point redChoosen;   //save the red cross
    private Point selected;
    private int selectLeftOnce; //record how many times you have click mouse
    private int selectRightOnce;
    private int inputNumber;
    private boolean rangeOn;
    private boolean NearestNeighborOn;
    private boolean FindAssigingDCPointsOn;
    private boolean FindRelevantUsersOn;
    private boolean isDCPoint;  //feature is dcpoint
    private boolean isUsers;    //feature is users
    private ArrayList<QueryInfo> info; //record choosen dcpoint
    private int numberOfTarget; //input number
    private boolean submitquery;    //have submitted or not
    private boolean submitLastTwo;
    
    public static final int POINT = 1;
    public static final int RECTANGLE = 2;
    public static final int TRIANGLE = 3;
    public static final int CIRCLE = 4;
    public static final int NORMAL = 1;
    public static final int QUERY = 2;

    public static final int RANDOM = 3;
    public int ColorCounter = 0;


    public HW2PanelPic() {
        img = null;
        rangeChoosen1 = new Point();
        rangeChoosen2 = new Point();
        redChoosen = new Point();
        selected = new Point();
        selectLeftOnce = 0;
        selectRightOnce = 0;
        rangeOn = false;
        NearestNeighborOn = false;
        FindAssigingDCPointsOn = false;
        FindRelevantUsersOn = false;
        info = new ArrayList<QueryInfo>();
        isDCPoint = false;
        isUsers = false;
        numberOfTarget = 0;
        submitquery = false;
        inputNumber = 0;

        this.addMouseListener(new java.awt.event.MouseAdapter() //panel mouse listener
        {
            public void mouseClicked(MouseEvent e)
            {
                this_mouseClicked(e);
            }
         });
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        loadImg(g);
    }

    public   void   paint(Graphics g) {
        loadImg(g);

        
    }

    public void loadImg(Graphics g)
    {
        img= createImage(getWidth(), getHeight());
        ImageIcon imageIcon = new ImageIcon("la.jpg");
        img = imageIcon.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    public Point getRangeChoosen1() {
        return rangeChoosen1;
    }

    public Point getRangeChoosen2() {
        return rangeChoosen2;
    }

    public Point getRedChoosen() {
        return redChoosen;
    }

    public void setRangeChoosen1(Point rangeChoosen1) {
        this.rangeChoosen1 = rangeChoosen1;
    }

    public void setRangeChoosen2(Point rangeChoosen2) {
        this.rangeChoosen2 = rangeChoosen2;
    }

    public void setRedChoosen(Point redChoosen) {
        this.redChoosen = redChoosen;
    }

    public void setNearestNeighborOn(boolean NearestNeighborOn) {
        this.NearestNeighborOn = NearestNeighborOn;
    }

    public void setRangeOn(boolean rangeOn) {
        this.rangeOn = rangeOn;
    }

    public int getNumberOfTarget() {
        return numberOfTarget;
    }

    public void setNumberOfTarget(int numberOfTarget) {
        this.numberOfTarget = numberOfTarget;
    }

    
    private void this_mouseClicked(MouseEvent e){
        if(rangeOn == true){
            if (e.getButton() == MouseEvent.BUTTON1){
                Point pt = new Point(e.getX(), e.getY());
                if(selectLeftOnce == 0){
                    setRangeChoosen1(pt);
                    selectLeftOnce = 1;
                    Graphics g = getGraphics();
                    panelDraw(g, POINT, pt, NORMAL);
                    //System.out.println(pta);
                }
                else if(selectLeftOnce == 1){
                    setRangeChoosen2(pt);
                    //System.out.println(selectOnce);
                    Graphics g = getGraphics();
                    panelDraw(g, POINT, pt, NORMAL);
                    panelDraw(g, RECTANGLE, pt, NORMAL);
                    selectLeftOnce = 2;
                    //System.out.println(ptb);
                }
            }
            //System.out.println(e.getX()+ " " + e.getY());
        }
        else if(NearestNeighborOn == true){   //handle mouse right button click
             if (e.getButton() == MouseEvent.BUTTON3){
                 Point pt = new Point(e.getX(), e.getY());
                 if(selectRightOnce == 0){
                     setRedChoosen(pt);
                     selectRightOnce = 1;
                     Graphics g = getGraphics();
                     panelDraw(g, POINT, pt, QUERY);
                 }
            }
        }
    }

    public void panelDraw(Graphics g, int type, Point pt, int status) {
        Graphics2D g2d = (Graphics2D)g;
        BasicStroke bs = new BasicStroke(2);    //set line width
        g2d.setStroke(bs);
        switch(type)
        {
            case POINT:
                g2d.setColor(Color.blue);
                if(status == NORMAL)
                    g2d.setColor(Color.blue);
                else if(status == QUERY)
                    g2d.setColor(Color.red);
                g2d.drawLine((int)pt.getX()- 5, (int)pt.getY() - 5, (int)pt.getX() + 5, (int)pt.getY() + 5);
                g2d.drawLine((int)pt.getX() - 5, (int)pt.getY() + 5, (int)pt.getX() + 5, (int)pt.getY() - 5);
                System.out.println("input are noe");
                break;

            case RECTANGLE:
                Point pt1 = new Point(rangeChoosen1);
                Point pt2 = new Point(rangeChoosen2);
                int width = Math.abs((int)pt2.getX() - (int)pt1.getX());
                int height = Math.abs((int)pt2.getY() - (int)pt1.getY());
                if((pt2.getY() < pt1.getY()) && (pt1.getX() < pt2.getX())){
                    pt1.setLocation(pt1.getX(), pt1.getY() - height);
                }
                else if((pt1.getX() > pt2.getX()) && (pt1.getY() < pt2.getY())){
                    pt1.setLocation(pt1.getX() - width, pt1.getY());
                }
                else if((pt1.getX() > pt2.getX()) && (pt1.getY() > pt2.getY())){
                    pt1.setLocation(pt1.getX() - width, pt1.getY() - height);
                }
                g2d.setColor(Color.blue);
                g2d.drawRect((int)pt1.getX(), (int)pt1.getY(), width, height);
                break;
             case TRIANGLE:
                g2d.setColor(Color.yellow);
                Point pttra1 = new Point((int)pt.getX(), (int)pt.getY() - 8);
                Point pttra2 = new Point((int)pt.getX() - 6, (int)pt.getY() + 3);
                Point pttra3 = new Point((int)pt.getX() + 6, (int)pt.getY() + 3);
                g2d.drawLine((int)pttra1.getX(), (int)pttra1.getY(), (int)pttra2.getX(), (int)pttra2.getY());
                g2d.drawLine((int)pttra1.getX(), (int)pttra1.getY(), (int)pttra3.getX(), (int)pttra3.getY());
                g2d.drawLine((int)pttra2.getX(), (int)pttra2.getY(), (int)pttra3.getX(), (int)pttra3.getY());
                int xPoints[] = {(int)pttra1.getX(), (int)pttra2.getX(), (int)pttra3.getX()};
                int yPoints[] = {(int)pttra1.getY(), (int)pttra2.getY(), (int)pttra3.getY()};
                int nPoints = 3;
                if(status == NORMAL)
                    g2d.setColor(Color.yellow);
                else if(status == QUERY)
                    g2d.setColor(Color.green);
                else if(status == RANDOM){
                    int []a = findColor();
                    g2d.setColor(new Color(a[0],a[1],a[2]));

                }
                g2d.fillPolygon(xPoints, yPoints, nPoints);
                break;
            case CIRCLE:
                g2d.setColor(Color.red);
                g2d.drawOval((int)pt.getX() - 5, (int)pt.getY() - 5, 10, 10);
                if(status == NORMAL)
                    g2d.setColor(Color.red);
                else if(status == QUERY)
                    g2d.setColor(Color.magenta);
                g2d.fillOval((int)pt.getX() - 5, (int)pt.getY() - 5, 10, 10);
                break;
            default:
                break;
        }
        
    }

    public void stringSequence(Graphics g, Point pt, int i)
    {
        Graphics2D g2d = (Graphics2D)g;
        BasicStroke bs = new BasicStroke(2);    //set line width
        g2d.setStroke(bs);

        g2d.drawString("" + i, (int)pt.getX() + 10, (int)pt.getY() + 10);
    }

    public void clearAll(){
        Graphics g = this.getGraphics();
        Color col = g.getColor();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(col);
        rangeChoosen1 = new Point();
        rangeChoosen2 = new Point();
        redChoosen = new Point();
        info = new ArrayList<QueryInfo>();
        submitquery = false;
        rangeOn = false;
        NearestNeighborOn = false;
        isDCPoint = false;
        isUsers = false;
        numberOfTarget = 0;
        selectLeftOnce = 0;
        selectRightOnce = 0;
        paint(g);
    }

    public boolean isSubmitquery() {
        return submitquery;
    }

    public int[] findColor(){
        //System.out.println(inputNumber);
        int col[] = new int [3];

        col[0] = (ColorCounter *123456+98765432)%255;
        col[1] = (ColorCounter *453746+87368463)%255;
        col[2] =  (ColorCounter * 2465380+345)%255;
            //System.out.println("R: " + r + " G: " + g + " B: " + b + "\n");
        return col;

    }
    
    


    public void setSubmitquery(boolean submitquery) {
        this.submitquery = submitquery;
    }

    public ArrayList<QueryInfo> getInfo() {
        return info;
    }

    public boolean isIsDCPoint() {
        return isDCPoint;
    }

    public boolean isIsUsers() {
        return isUsers;
    }

    public void setIsDCPoint(boolean isDCPoint) {
        this.isDCPoint = isDCPoint;
    }

    public void setIsUsers(boolean isUsers) {
        this.isUsers = isUsers;
    }

    public void setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
    }

    public int getInputNumber() {
        return inputNumber;
    }

    public boolean isSubmitLastTwo() {
        return submitLastTwo;
    }

    public void setSubmitLastTwo(boolean submitLastTwo) {
        this.submitLastTwo = submitLastTwo;
    }

}

    

