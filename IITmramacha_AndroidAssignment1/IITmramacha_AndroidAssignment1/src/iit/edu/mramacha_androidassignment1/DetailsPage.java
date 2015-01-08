package iit.edu.mramacha_androidassignment1;

import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

public class DetailsPage {
	
	/*Name	        Address	             Building Code
	Alumni Memorial	3201 South Dearborn	AM
	Crown Hall	    3360 South State	CR
	Engineering 1	10 West 32nd	    E1
	Life Science	3105 South Dearborn	LS
	Perlstein Hall	10 West 33rd	     PH
	Siegel Hall	    3301 South Dearborn	SH
	Stuart Building	10 West 31st	     SB
	Wishnick Hall	3255 South Dearborn	WH
	
	*/
	// Initializing buildings data .
	public static  List<String> header = new ArrayList<String>(asList("Alumni Memorial","Crown Hall","Engineering 1",
		                              "Life Science"	,"Perlstein Hall","Siegel Hall", "Stuart Building",	"Wishnick Hall"));
	
	public static List<String> address = new ArrayList<String>(asList("3201 South Dearborn","3360 South State","10 West 32nd","3105 South Dearborn", "10 West 33rd",  "3301 South Dearborn", "10 West 31st",  "3255 South Dearborn"));
	public static List<String> buildingCodes = new ArrayList<String>(asList("AM","CM","E1","LS","PH", "SH","SB","WH"));
	public static List<String> description = new ArrayList<String>(asList("Alumni Memorial was designed by Mies van der Rohe and built in 1945. Alumni Memorial has 2 classrooms and 1 computer lab available for use through the CCC.",
											"S. R. Crown Hall, designed by the German-born Modernist architect Ludwig Mies van der Rohe, is the home of the College of Architecture.",
											"Engineering 1 was designed by Myron Goldsmith, a student of Mies van der Rohe, and it was built in 1966. E1 has 16 classrooms, 1 computer lab, and 1 auditorium available for use through the CCC.",
											"Life Sciences was designed by Myron Goldsmith, a student of Mies van der Rohe, and it was built in 1966. Life Sciences has 6 classrooms and 1 auditorium available for use through the CCC.",
											"Perlstein Hall was designed by Mies van der Rohe and built in 1945. Perlstein Hall has 2 classrooms and 1 auditorium available for use through the CCC.",
											"Siegel Hall was designed by Mies van der Rohe and built in 1956. Siegel Hall has 3 classrooms, 2 computer labs, and 1 auditorium available for use through the CCC.",
											"Stuart Building was designed by Myron Goldsmith, a student of Mies van der Rohe, and it was built in 1971. Stuart Building has 9 classrooms, 3 computer labs, and 1 auditorium available for use through the CCC.",
											"Wishnick Hall was designed by Mies van der Rohe and built in 1945. Wishnick Hall has 5 classrooms and 1 auditorium available for use through the CCC."));
	
}