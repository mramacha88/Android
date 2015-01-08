package iit.edu.iitmramacha_androidfinalproject;

import java.util.ArrayList;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class Parameters {
	public static String TAG_EVENT = "event";
	public static String TAG_EVENTS = "events";
	public static String TAG_CATEGORY = "category";
	public static String TAG_NAME = "name";
	public static String TAG_ADDRESS = "address";
	public static String TAG_LATITUDE = "latitude";
	public static String TAG_LONGITUDE = "longitude";
	public static String TAG_CITY = "city";
	public static String TAG_POSTAL_CODE = "postal_code";
	public static String TAG_ORGANIZER = "organizer";
	public static String TAG_VENUE = "venue";
	public static String TAG_REGION = "region";
	public static String TAG_DESCRIPTION = "description";
	public static String TAG_TITLE = "title";
	public static String TAG_START_DATE = "start_date";
	public static String TAG_END_DATE = "end_date";
	public static String TAG_URL = "url";
	public static String TAG_NOT_AVAILABLE = "NOT AVAILABLE";
	public static String TAG_DISPLAY_CATEGORY = "Category : ";
	public static String TAG_DISPLAY_ADDRESS = "Address : ";
	public static String TAG_DISPLAY_ORGANIZER = "Organizer : ";
	public static String TAG_DISPLAY_URL = "URL : ";
	public static String TAG_DISPLAY_START_DATE = "Start Time : ";
	public static String TAG_DISPLAY_END_DATE = "End Time : ";
	public static String TAG_DISPLAY_DESCRIPTION = "Description : ";
	public static String TAG_DISPLAY_CATEGORY_NOT_SPECDIFIED = "Category : Not Specified";
	public static String TAG_DISPLAY_ORGANIZER_NOT_SPECDIFIED = "Organizer : Not Specified";
	public static String TAG_DISPLAY_ADDRESS_NOT_SPECDIFIED = "Address : Not Specified";
	public static String TAG_NOT_SPECDIFIED = "Not Specified";
	public static String TITLE_EVENTSDATA = "Events Data";
	public static String TITLE_EVENTSCATEGORYLIST = "Events Category List";
	public static String TITLE_LOCATIONEVENTS = "Location Events";
	public static final int RESPONSE_SUCCESS = 1;
	public static final int RESPONSE_FAILURE = 0;
	public static final int RESPONSE_ERROR_RADIOBUTTON = 2;
	public static final int VERSION_FAILURE = 4;
	public static final int GPS_FAILURE = 3;
	public static final String URLForChicagoEvents = "https://www.eventbrite.com/json/event_search?&city=chicago&date=this+week&state=IL&app_key=3XPDJ6KIMWJOUTHQPV";
	public static final String Address_Retrival_Error = "Geocoder Error: Can Not Locate the Address, Please check that you have Android version 2.3 or Greater";
	public static final String Version_Error = "Upgrade Android Version to 2.3 or Greater";
	public static final String RadioButton_Error = "Please select any of the sort option";
	public static final String GPS_Error = "GPS signal not available";
	public static final String Events_Not_Found = "No Events Found in your loacation";
	public static final String MAP_COORDINATES_ERROR = "Co-ordinates not able to retrieve";

	public final static ForegroundColorSpan fcs = new ForegroundColorSpan(
			Color.rgb(0, 0, 128));
	static ArrayList<EventsData> Events_Data;

	public static SpannableStringBuilder modifyStringToRequiredTypeface(
			String Value, int length1, int length2) {
		SpannableStringBuilder dataString = new SpannableStringBuilder(Value);
		final StyleSpan toBold = new StyleSpan(android.graphics.Typeface.BOLD);
		final StyleSpan toItalic = new StyleSpan(
				android.graphics.Typeface.ITALIC);
		dataString.setSpan(toBold, 0, length1,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		dataString.setSpan(toItalic, length1, length2,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		dataString.setSpan(fcs, length1, length2,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return dataString;
	}

	public static SpannableStringBuilder makeStringBold(String value, int length) {
		SpannableStringBuilder dataString = new SpannableStringBuilder(value);
		StyleSpan toBold = new StyleSpan(android.graphics.Typeface.BOLD);
		dataString.setSpan(toBold, 0, length,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return dataString;
	}

}