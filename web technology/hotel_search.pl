#!/usr/bin/perl -w
use URI;
use LWP::Simple;

$form_info=$ENV{'QUERY_STRING'};
@pairs = split(/&/,$form_info);
foreach $pair(@pairs){
   ($name,$value)=split(/=/,$pair);
   $in{$name} = $value;
}
$city_name = $in{'city'};
$hotel_name = $in{'hotels'};
$city_name =~ tr/A-Z/a-z/;
$hotel_name=~ tr/A-Z/a-z/;
$city_name=~ tr/ /+/;
$base_url = "http://www.tripadvisor.com/Search?q=";
$rel_url = $hotel_name . "+";
$rel_url = $rel_url . $city_name;
$abs_url = $base_url . $rel_url;
$html_content = LWP::Simple::get($abs_url);

print "Content-type: text/xml\n\n";
print "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

$city_name1 = $in{'city'};
$hotel_name1 = $in{'hotels'};
$city_name1 =~ tr/+/ /;
$hotel_name1 =~ s/^(\w*)/\u$1/;

$html_content =~ m/<i>of<\/i>(.*)<\/span>(.*)<\/div>(\n*)<div(.*)srLODGING/;
$results_num = $1;
if($results_num==0){
    print "<hotels total=\"$results_num\">\n";
}
else{
    print "<hotels total=\"$results_num\">\n";
    $html_content =~ m/<span>1\-(.*?)<\/span>/;
    $results_page = $1;
    for($i=0;$i<$results_page;$i++){

        $html_content =~ m/<a href=(.*)onclick\=\"setOneTimeCookie(.*)\;\">(.*?)<\/a>/g;
        $detail_name = $3;
        $detail_name =~ s/(<b>|<\/b>)//g;

        $html_content =~ m/<span class=\"srHitType\">(.*)<\/span>/g;
        $result_type = $1;

        $html_content =~ m/\&ndash\;\ (.*?)\ <\/div>/g;
        $hotel_location = $1;
        
        $detail_name1 = $detail_name;
        $detail_name1 =~ s/\W/\_/g;
        $detail_name1 =~ s/(\_\_\_|\_\_)/\_/g;
        if($html_content =~ /$detail_name1\-(.*)>(\n*)<div(.*)>(\n*)<img\ src=\"(.*)\"\ class=\"photo\_image\"/){
        $html_content =~ m/$detail_name1\-(.*)>(\n*)<div(.*)>(\n*)<img\ src=\"(.*)\"\ class=\"photo\_image\"/;
        $hotel_image = $5;}
        else{
	    $hotel_image = 'Not Found';
        }

        $hotel_order = $i+1;
        $html_content =~ m/<a href=\"(.*)\"\ onclick=\"setOneTimeCookie(.*)B$hotel_order\'\)/;
        $review_link = "http\:\/\/www\.tripadvisor\.com" . $1;

        
        $hardcoded_str = 'REVIEWS\"\ onclick\=\"setOneTimeCookie\(\'NORDINAL\'\,\'B';
        $review_tag = $hardcoded_str . $hotel_order;
        if($html_content =~ /$review_tag/){
	$html_content =~ m/B$hotel_order\'\)\;\">\n<span(.*)alt\=\"(.*)\ of\ 5\ stars\"\/><\/span>/;
        $hotel_rating = $2;

        $html_content =~ m/\n(.*)\ reviews/g;
        $review_count = $1;
    }
        else {
	    $hotel_rating = 'Not Found';
            $review_count = 'Not Found';}
        if($result_type eq 'Hotel'){
            $detail_name =~ s/&/&amp;/g;
	    print "<hotel name=\"$detail_name\" location=\"$hotel_location\" no_of_stars=\"$hotel_rating\" no_of_reviews=\"$review_count\" image_url=\"$hotel_image\" review_url=\"$review_link\"\ />\n";
    }
     
    }
    
}
print "</hotels>\n";
