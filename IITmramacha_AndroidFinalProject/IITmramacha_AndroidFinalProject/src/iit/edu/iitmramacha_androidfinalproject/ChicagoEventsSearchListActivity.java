package iit.edu.iitmramacha_androidfinalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Meghashree M Ramachandra Connects to browser and displays events or
 *         category based upon the requirements selected from user.
 */
public class ChicagoEventsSearchListActivity extends Activity {
	String URLLinkTogetResponse;
	boolean isOptionSelected = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eventlist);
		URLLinkTogetResponse = getIntent().getExtras().getString("URLValue");
		isOptionSelected = getIntent().getExtras().getBoolean(
				"radioOptionSelected");

		new AsyncTask<Void, Void, List<EventsData>>() {
			private ProgressDialog dialog = new ProgressDialog(
					ChicagoEventsSearchListActivity.this);

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Start ProgressDialog Loading.... message
				this.dialog.setMessage("Loading Events.....");
				this.dialog.show();
			}

			// Connects to browser and retrieves data,otherwise exception is
			// thrown if error occurs
			// which returns emptylist

			@Override
			protected List<EventsData> doInBackground(Void... params) {
				String eventsDetails = readeventDetails();
				try {
					return processJson(eventsDetails);
				} catch (JSONException e) {
					return Collections.<EventsData> emptyList();
				}
			}

			// Events or Events categories are displayed accordingly based on
			// choice.
			@Override
			protected void onPostExecute(final List<EventsData> businesses) {

				if (businesses.isEmpty()) {
					new AlertDialog.Builder(
							ChicagoEventsSearchListActivity.this)
							.setTitle(Parameters.TITLE_EVENTSDATA)
							.setMessage(Parameters.Events_Not_Found)
							.setNegativeButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											finish();
										}

									}).show();
				} else if (isOptionSelected == true) {
					TextView mTextView_EventsCategoryList = (TextView) findViewById(R.id.TextViewEventListHeader);
					mTextView_EventsCategoryList
							.setText(Parameters.TITLE_EVENTSCATEGORYLIST);
					ArrayList<EventsData> eventsDataForCategory = (ArrayList<EventsData>) businesses;
					ArrayList<String> category = new ArrayList<String>();
					String compareStringOccurance1;
					String compareStringOccurance2;

					for (int i = 0; i < eventsDataForCategory.size(); i++) {
						if (eventsDataForCategory.get(i).eventCategory
								.toString().contains(",")) {
							String[] splitData = eventsDataForCategory.get(i).eventCategory
									.toString().split(",");
							compareStringOccurance1 = splitData[0] + ","
									+ splitData[1];
							compareStringOccurance2 = splitData[1] + ","
									+ splitData[0];
						} else {
							compareStringOccurance1 = eventsDataForCategory
									.get(i).eventCategory;
							compareStringOccurance2 = eventsDataForCategory
									.get(i).eventCategory;
						}
						if (category.contains(compareStringOccurance1)
								|| category.contains(compareStringOccurance2)) {
							continue;

						} else if (compareStringOccurance1.equals("")) {
							if (category
									.contains(Parameters.TAG_NOT_SPECDIFIED)) {
								continue;
							} else {
								category.add(Parameters.TAG_NOT_SPECDIFIED);
							}
						} else {

							category.add(compareStringOccurance1);
						}

					}
					ListView lv = (ListView) findViewById(R.id.ListViewEvent);

					final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							ChicagoEventsSearchListActivity.this,
							android.R.layout.simple_list_item_1,
							android.R.id.text1, category);
					lv.setAdapter(adapter);
					lv.setSelector(getResources().getDrawable(
							R.drawable.textselector));
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// TODO Auto-generated method stub
							Parameters.Events_Data = (ArrayList<EventsData>) businesses;
							Intent eventsDisplayOnTypeOfCategory = new Intent(
									ChicagoEventsSearchListActivity.this,
									EventsBasedOnCategory.class);

							eventsDisplayOnTypeOfCategory.putExtra(
									"CategorySelected",
									adapter.getItem(position).toString());
							startActivity(eventsDisplayOnTypeOfCategory);
						}
					});
				} else {
					ListView lv = (ListView) findViewById(R.id.ListViewEvent);
					lv.setAdapter(new EventAdapter(
							ChicagoEventsSearchListActivity.this,
							R.layout.activity_eventsviewrow,
							(ArrayList<EventsData>) businesses));
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// TODO Auto-generated method stub
							showEventDetailsScreen(position,
									(ArrayList<EventsData>) businesses);
						}
					});
				}

				if (dialog.isShowing()) {
					dialog.dismiss();
				}

			}
		}.execute();

	}

	// Events addition details such as URL, Start End time, Description are
	// displayed
	private void showEventDetailsScreen(final int pos,
			final ArrayList<EventsData> eventItems) {
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater
				.from(ChicagoEventsSearchListActivity.this);
		View eventDescripView = factory.inflate(
				R.layout.activity_eventdescription, null);
		TextView selectedEventURL = (TextView) eventDescripView
				.findViewById(R.id.TextViewActivityDescpEventURL);
		TextView selectedEventName = (TextView) eventDescripView
				.findViewById(R.id.TextViewActivityDescriptionEventName);
		TextView selectedEventDescription = (TextView) eventDescripView
				.findViewById(R.id.TextViewActivityDescpEventDescription);
		TextView selectedEventStartDate = (TextView) eventDescripView
				.findViewById(R.id.TextViewActivityDescpStartDate);
		TextView selectedEventEndDate = (TextView) eventDescripView
				.findViewById(R.id.TextViewActivityDescpEndDate);

		selectedEventURL.setText(Parameters.makeStringBold(
				Parameters.TAG_DISPLAY_URL
						+ eventItems.get(pos).eventURI.toString(),
				Parameters.TAG_DISPLAY_URL.length()));
		selectedEventName.setText(eventItems.get(pos).eventName.toString());
		selectedEventStartDate.setText(Parameters.makeStringBold(
				Parameters.TAG_DISPLAY_START_DATE
						+ eventItems.get(pos).eventStartDate.toString(),
				Parameters.TAG_DISPLAY_START_DATE.length()));
		selectedEventEndDate.setText(Parameters.makeStringBold(
				Parameters.TAG_DISPLAY_END_DATE
						+ eventItems.get(pos).eventEndDate.toString(),
				Parameters.TAG_DISPLAY_END_DATE.length()));
		String data = eventItems.get(pos).eventDescription.replaceAll(
				"\\<.*?>", "");
		selectedEventDescription.setText(Parameters.makeStringBold(
				Parameters.TAG_DISPLAY_DESCRIPTION + data.toString(),
				Parameters.TAG_DISPLAY_DESCRIPTION.length()));

		new AlertDialog.Builder(ChicagoEventsSearchListActivity.this)
				.setView(eventDescripView).setNeutralButton("OK", null)
				.create().show();
	}

	// Events are displayed in listview
	public class EventAdapter extends ArrayAdapter<EventsData> {

		private ArrayList<EventsData> eventItems;

		public EventAdapter(Context context, int textViewResourceId,
				ArrayList<EventsData> eventItems) {
			super(context, textViewResourceId, eventItems);
			this.eventItems = eventItems;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.activity_eventsviewrow, null);
			}

			EventsData ed = eventItems.get(position);
			if (ed != null) {
				TextView eventName = (TextView) view
						.findViewById(R.id.TextView_EventName);
				final TextView eventCategory = (TextView) view
						.findViewById(R.id.TextView_Category);
				TextView eventOrganizer = (TextView) view
						.findViewById(R.id.TextView_EventOrganizer);
				final TextView eventAddress = (TextView) view
						.findViewById(R.id.TextView_EventAddress);

				eventCategory.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Parameters.Events_Data = (ArrayList<EventsData>) eventItems;
						Intent eventsDisplayOnTypeOfCategory = new Intent(
								ChicagoEventsSearchListActivity.this,
								EventsBasedOnCategory.class);
						String data = eventCategory.getText().toString();
						String[] value = data.split(" : ");
						eventsDisplayOnTypeOfCategory.putExtra(
								"CategorySelected", value[1]);
						startActivity(eventsDisplayOnTypeOfCategory);
					}
				});
				eventAddress.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Parameters.Events_Data = (ArrayList<EventsData>) eventItems;
						int selectedAddressPosition = 0;
						String data = eventAddress.getText().toString();
						String[] value = data.split(" : ");
						for (int i = 0; i < Parameters.Events_Data.size(); i++) {
							if (Parameters.Events_Data.get(i).eventAddress
									.equals(value[1].toString())) {
								selectedAddressPosition = i;
								break;
							}
						}

						double Latitude = Double.parseDouble(Parameters.Events_Data
								.get(selectedAddressPosition).eventLatitude);
						double Longitude = Double.parseDouble(Parameters.Events_Data
								.get(selectedAddressPosition).eventlongitude);
						if (Latitude != 0.0 && Longitude != 0.0) {
							Intent eventsVenueCoOrdinates = new Intent(
									ChicagoEventsSearchListActivity.this,
									MapViewActivity.class);
							eventsVenueCoOrdinates.putExtra("Latitude",
									Latitude);
							eventsVenueCoOrdinates.putExtra("Longitude",
									Longitude);
							startActivity(eventsVenueCoOrdinates);
						} else
							new AlertDialog.Builder(
									ChicagoEventsSearchListActivity.this)
									.setTitle(Parameters.TITLE_EVENTSDATA)
									.setMessage(
											Parameters.MAP_COORDINATES_ERROR)
									.setNegativeButton("OK", null).create();
					}
				});

				if (eventName != null) {
					eventName.setText(ed.eventName.replaceAll("[\\r\\n]", "")
							.replaceAll("\\<.*?>", ""));
				}
				if (eventCategory != null) {
					if (ed.eventCategory.equals("")) {
						eventCategory
								.setText(Parameters
										.modifyStringToRequiredTypeface(
												Parameters.TAG_DISPLAY_CATEGORY_NOT_SPECDIFIED,
												9,
												Parameters.TAG_DISPLAY_CATEGORY_NOT_SPECDIFIED
														.length()));
					} else {

						eventCategory.setText(Parameters
								.modifyStringToRequiredTypeface(
										Parameters.TAG_DISPLAY_CATEGORY
												+ ed.eventCategory,
										Parameters.TAG_DISPLAY_CATEGORY
												.length(),
										Parameters.TAG_DISPLAY_CATEGORY
												.length()
												+ ed.eventCategory.length()));
					}
				}
				if (eventOrganizer != null) {
					if (ed.eventOrganizerName.equals("")) {
						eventOrganizer
								.setText(Parameters
										.modifyStringToRequiredTypeface(
												Parameters.TAG_DISPLAY_ORGANIZER_NOT_SPECDIFIED,
												10,
												Parameters.TAG_DISPLAY_ORGANIZER_NOT_SPECDIFIED
														.length()));
					} else {
						eventOrganizer.setText(Parameters
								.modifyStringToRequiredTypeface(
										Parameters.TAG_DISPLAY_ORGANIZER
												+ ed.eventOrganizerName,
										Parameters.TAG_DISPLAY_ORGANIZER
												.length(),
										Parameters.TAG_DISPLAY_ORGANIZER
												.length()
												+ ed.eventOrganizerName
														.length()));
					}
				}
				if (eventAddress != null) {
					if (ed.eventAddress.equals("")) {
						eventAddress
								.setText(Parameters
										.modifyStringToRequiredTypeface(
												Parameters.TAG_DISPLAY_ADDRESS_NOT_SPECDIFIED,
												8,
												Parameters.TAG_DISPLAY_ADDRESS_NOT_SPECDIFIED
														.length()));
					} else {
						eventAddress
								.setText(Parameters
										.modifyStringToRequiredTypeface(
												Parameters.TAG_DISPLAY_ADDRESS
														+ ed.eventAddress,
												Parameters.TAG_DISPLAY_ADDRESS
														.length(),
												Parameters.TAG_DISPLAY_ADDRESS
														.length()
														+ ed.eventAddress
																.length()));
					}
				}

			}

			return view;
		}

	}

	// Sending the Url to connect.
	public String readeventDetails() {
		return postJSON(URLLinkTogetResponse);
	}

	// Connects to respective URL and retrieves data
	private String postJSON(String stringURL) {
		StringBuilder builder = new StringBuilder();
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(stringURL);

		try {
			httppost.addHeader("Content-Type",
					"application/json; charset=utf-8");
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.toString();
	}

	// Obtained response is parsed and required data are obtained.
	List<EventsData> processJson(String jsonStuff) throws JSONException {
		JSONObject json = new JSONObject(jsonStuff);
		JSONArray businesses = json.getJSONArray(Parameters.TAG_EVENTS);
		if (businesses.length() == 0) {
			ArrayList<EventsData> businessNames = new ArrayList<EventsData>();
			return businessNames;
		} else {
			ArrayList<EventsData> businessNames = new ArrayList<EventsData>(
					businesses.length());
			for (int i = 1; i < businesses.length(); i++) {
				JSONObject business = businesses.getJSONObject(i);
				JSONObject business1 = business
						.getJSONObject(Parameters.TAG_EVENT);
				JSONObject eventOrganizor = business1
						.getJSONObject(Parameters.TAG_ORGANIZER);
				JSONObject eventVenue = business1
						.getJSONObject(Parameters.TAG_VENUE);
				try {
					businessNames.add(new EventsData(business1
							.getString(Parameters.TAG_TITLE), business1
							.getString(Parameters.TAG_CATEGORY), eventOrganizor
							.getString(Parameters.TAG_NAME), eventVenue
							.getString(Parameters.TAG_NAME)
							+ ","
							+ eventVenue.getString(Parameters.TAG_ADDRESS)
							+ ","
							+ eventVenue.getString(Parameters.TAG_CITY)
							+ ","
							+ eventVenue.getString(Parameters.TAG_REGION)
							+ ","
							+ eventVenue.getString(Parameters.TAG_POSTAL_CODE),
							eventVenue.getString(Parameters.TAG_LATITUDE),
							eventVenue.getString(Parameters.TAG_LONGITUDE),
							business1.getString(Parameters.TAG_START_DATE),
							business1.getString(Parameters.TAG_END_DATE),
							business1.getString(Parameters.TAG_URL), business1
									.getString(Parameters.TAG_DESCRIPTION)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return businessNames;
		}

	}

}
