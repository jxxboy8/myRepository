window.onload = initall;
 
var cityname;
var req;
var doc;

    function initall(){
       document.getElementById("submit").onclick = locationCheck;
    }

    function locationCheck()
    {  
       var url;
       var urlstring;
       cityname = document.forms[0].city.value;
       if(cityname.length==0){
          alert("Please enter a city name.");
       }
       else{
          urlstring = "http://cs-server.usc.edu:15652/examples/servlet/ajax_hotel?city="+document.forms[0].city.value+"&hotels="+document.forms[0].hotels.value;
          url = urlstring.replace(" ", "+");
          loadXMLDoc(url);
       }
    }

function loadXMLDoc(url){
       req = false;

       if(window.XMLHttpRequest){
          try{ req = new XMLHttpRequest(); }
          catch(e)
          { req = false; }
       }
       else if (window.ActiveXObject){
          try{
             req = new ActiveXObject("Msxm12.XMLHTTP");
          } catch(e) {
          try{
             req = new ActiveXObject("Microsoft.XMLHTTP");
          } catch(e) {
             req = false;
          }
          }
       }
       if(req) {
          req.open("GET", url, true);
          req.onreadystatechange = myCallback;
          req.setRequestHeader("Connection", "Close");
          req.setRequestHeader("Method", "GET" + url + "HTTP/1.1");
          req.send("");
       }
       else{
          alert("Can not connect.");
       }
    }

    function myCallback(){
       if (req.readyState == 4){
          if (req.status == 200) {
             doc = eval('(' + req.responseText + ')');
             var hotel_list = doc.hotels.hotel;
             if(doc.hotels.hotel.length!=0) {
             var html = "<table border><tr><td align=center>Image</td><td align=center>Name</td><td align=center>Location</td><td align=center>Rating out of 5</td><td align=center>Reviews</td><td align=center>Post to Facebook</td>";
             for(var i = 0; i < hotel_list.length; i++){
                if(doc.hotels.hotel[i].image_url=="Not Found"){
                   html += "<tr><td align=center>Not Found</td>";
                }
                else{
                   html += "<tr><td align=center><img src='"+doc.hotels.hotel[i].image_url+"' width=60px height=60px></td>";
                }
                html += "<td align=center>"+doc.hotels.hotel[i].name+"</td><td align=center>"+doc.hotels.hotel[i].location+"</td><td align=center>"+doc.hotels.hotel[i].no_of_stars+"</td>";
                if(doc.hotels.hotel[i].no_of_reviews == "Not Found"){
                   html += "<td align=center>Not Found</td>";
                }
                else{
                   html += "<td align=center><a href=\""+doc.hotels.hotel[i].review_url+"\">"+doc.hotels.hotel[i].no_of_reviews+"</a></td>";
                }
                html += "<td align=center><input type='image' src='facebook_logo.jpg' width=130px height=50px onclick='processfb("+i+")'></td>";  

             }
             html += "</table>";
             document.getElementById('hotel_list').innerHTML = html;
             }
             else{
                var html1 = "No hotels found.";
                document.getElementById('hotel_list').innerHTML = html1;
             }
          } else {
             alert("There was a problem retrieving the XML data:\n" + req.statusText);
          }
       }
    }
