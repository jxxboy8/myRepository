import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.lang.String;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ajax_hotel extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String city = request.getParameter("city");
        String hotels = request.getParameter("hotels");
        String urlstring = "http://cs-server.usc.edu:15651/cgi-bin/hotel_search.pl?city="+city+"&hotels="+hotels;
	URL url = new URL(urlstring);
	URLConnection urlConnection = url.openConnection();
	urlConnection.setAllowUserInteraction(false);
	InputStream urlStream = url.openStream();
	JSONObject json = new JSONObject();
        JSONObject sub_json = new JSONObject();
	JSONArray hotel_array = new JSONArray();
        try{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.parse(urlStream);
            Element root = document.getDocumentElement();
            NodeList hotel_list = root.getElementsByTagName("hotel");
            for(int i=0;i<hotel_list.getLength();i++){
		JSONObject member = new JSONObject();
		Element hotel = (Element)hotel_list.item(i);
                member.put("name", hotel.getAttribute("name"));
		member.put("location", hotel.getAttribute("location"));
		member.put("no_of_stars", hotel.getAttribute("no_of_stars"));
		member.put("no_of_reviews", hotel.getAttribute("no_of_reviews"));
		member.put("image_url", hotel.getAttribute("image_url"));
		member.put("review_url", hotel.getAttribute("review_url"));
		hotel_array.put(i, member);
	    }
            sub_json.put("hotel", hotel_array);
            json.put("hotels", sub_json);
	    out.print(json.toString());
	}
	catch(MalformedURLException e1){
            e1.printStackTrace();
	}
	catch(IOException e2){
            e2.printStackTrace();
        }
	}
}
