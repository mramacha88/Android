package iit.edu.iitmramacha_androidfinalproject;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 
 * @author Meghashree M Ramachandra The App is named EventAlly and is used to
 *         retrieve all chicago and current location events, In addition to
 *         that, events can be diaplayed based on category or events relevance
 *         based on user requirement, also events details can be viewed which
 *         includes start time, end time, description, organizer, Url open in
 *         the browser and tickets can be booked for the event. Venue location
 *         is marked on the map for user help to view venue position
 * 
 */
// Start up activity of the app, which has options to retreive the events based
// on need.

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private Button mButton_Chicago_Events = null;
	private Button mButton_MyLocation_Events = null;
	private RadioGroup mRadioGroup_SelectOptions = null;
	private RadioButton mRadioButton_OptionSelected = null;
	static String URLToConnectForEventsData = "";
	static String city = "";
	static String State = "";
	static boolean categorySelected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mButton_Chicago_Events = (Button) findViewById(R.id.Button_Chicago_Events);
		mButton_MyLocation_Events = (Button) findViewById(R.id.Button_Location_Events);
		mRadioGroup_SelectOptions = (RadioGroup) findViewById(R.id.radioGroup);
		setEventListeners();
	}

	// For setting listeners to Buttons
	private void setEventListeners() {

		// Retrieves chicago events
		mButton_Chicago_Events.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkForRadioButton()) {
					URLToConnectForEventsData = Parameters.URLForChicagoEvents;
					Intent intent = new Intent(MainActivity.this,
							ChicagoEventsSearchListActivity.class);
					intent.putExtra("URLValue", URLToConnectForEventsData);
					intent.putExtra("radioOptionSelected", categorySelected);
					startActivity(intent);
				}

			}
		});

		// Retrieves cuurent location events
		mButton_MyLocation_Events.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkForRadioButton()) {
					try {
						double latitude = 0;
						double longitude = 0;
						
						LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
						Criteria criteria = new Criteria();
						String provider = locManager.getBestProvider(criteria, false);
						locManager.requestLocationUpdates(
								provider, 1000L, 500.0f,
								locationListener);
						Location location = locManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
								&& Geocoder.isPresent()) {

							try {

								city="Arlington+Heights";
								State="IL";
									response_Handler
											.sendEmptyMessage(Parameters.RESPONSE_SUCCESS);
	//							}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								response_Handler
										.sendEmptyMessage(Parameters.RESPONSE_FAILURE);
							}

						} else {
							response_Handler
									.sendEmptyMessage(Parameters.VERSION_FAILURE);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						response_Handler
								.sendEmptyMessage(Parameters.GPS_FAILURE);

					}
				}
			}
		});

	}

	// Checks if radiobutton is selected.
	protected boolean checkForRadioButton() {
		// TODO Auto-generated method stub
		boolean isSelected = false;
		int selectedRadioButton = mRadioGroup_SelectOptions
				.getCheckedRadioButtonId();
		if (selectedRadioButton != -1) {
			isSelected = true;
			mRadioButton_OptionSelected = (RadioButton) findViewById(selectedRadioButton);
			String radioSelected = mRadioButton_OptionSelected.getText()
					.toString();

			if (radioSelected.equals(getResources().getString(
					R.string.radiobutton_categoryrelevance))) {
				categorySelected = true;
			} else {
				categorySelected = false;
			}

		} else {
			isSelected = false;
			response_Handler
					.sendEmptyMessage(Parameters.RESPONSE_ERROR_RADIOBUTTON);
		}
		return isSelected;
	}

	final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	// To continue upon success or error are displayed if app fails
	private Handler response_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Parameters.RESPONSE_SUCCESS:
				URLToConnectForEventsData = "https://www.eventbrite.com/json/event_search?&city="
						+ city
						+ "&date=this+week&state="
						+ State
						+ "&app_key=3XPDJ6KIMWJOUTHQPV";
				Intent intent = new Intent(MainActivity.this,
						ChicagoEventsSearchListActivity.class);
				intent.putExtra("URLValue", URLToConnectForEventsData);
				intent.putExtra("radioOptionSelected", categorySelected);
				startActivity(intent);
				break;

			case Parameters.RESPONSE_FAILURE:
				showAlertDialog(Parameters.Address_Retrival_Error);
				break;

			case Parameters.RESPONSE_ERROR_RADIOBUTTON:
				showAlertDialog(Parameters.RadioButton_Error);
				break;

			case Parameters.GPS_FAILURE:
				showAlertDialog(Parameters.GPS_Error);
				break;

			case Parameters.VERSION_FAILURE:
				showAlertDialog(Parameters.Version_Error);
				break;

			default:
				break;
			}
		}

	};

	public void showAlertDialog(String msg) {
		new AlertDialog.Builder(MainActivity.this)
				.setTitle(Parameters.TITLE_LOCATIONEVENTS).setMessage(msg)
				.setNeutralButton("Ok", null).show();
	}

	public void showAboutScreen() {
		LayoutInflater factory = LayoutInflater.from(this);
		View aboutView = factory.inflate(R.layout.activity_about, null);
		new AlertDialog.Builder(MainActivity.this).setView(aboutView)
				.setNeutralButton("OK", null).create().show();
	}

	// Adding menu data.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	// About activity started from menu.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_about:
			showAboutScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
