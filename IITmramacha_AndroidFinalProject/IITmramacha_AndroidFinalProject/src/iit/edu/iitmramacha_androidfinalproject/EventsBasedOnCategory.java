package iit.edu.iitmramacha_androidfinalproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Meghashree M Ramachandra Based on the category selected respective
 *         events are displayed and addition details of the events are also
 *         displayed.
 */
public class EventsBasedOnCategory extends Activity {

	String CategorySelected = "";
	ArrayList<EventsData> UpdateEventsData = null;
	String compareStringOccurance1 = "";
	String compareStringOccurance2 = "";

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eventlist);

		CategorySelected = getIntent().getExtras()
				.getString("CategorySelected");
		TextView mTextView_Header = (TextView) findViewById(R.id.TextViewEventListHeader);
		mTextView_Header.setText(CategorySelected);

		if (CategorySelected.contains(",")) {
			String[] splitData = CategorySelected.split(",");
			compareStringOccurance1 = splitData[0] + "," + splitData[1];
			compareStringOccurance2 = splitData[1] + "," + splitData[0];
		} else {
			compareStringOccurance1 = "";
			compareStringOccurance2 = "";
		}
		new AsyncTask<Void, Void, List<EventsData>>() {
			private ProgressDialog dialog = new ProgressDialog(
					EventsBasedOnCategory.this);

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Start ProgressDialog Loading.... message
				this.dialog.setMessage("Loading Events.....");
				this.dialog.show();
			}

			// Selected category events are searched and stored in array.
			@Override
			protected List<EventsData> doInBackground(Void... params) {
				try {
					UpdateEventsData = new ArrayList<EventsData>();
					for (int i = 0; i < Parameters.Events_Data.size(); i++) {
						if (Parameters.Events_Data.get(i).eventCategory
								.toString().equals(compareStringOccurance1)
								| Parameters.Events_Data.get(i).eventCategory
										.toString().equals(
												compareStringOccurance2)) {
							UpdateEventsData
									.add(new EventsData(
											Parameters.Events_Data.get(i).eventName
													.toString(),
											Parameters.Events_Data.get(i).eventOrganizerName
													.toString(),
											Parameters.Events_Data.get(i).eventAddress
													.toString(),
											Parameters.Events_Data.get(i).eventLatitude
													.toString(),
											Parameters.Events_Data.get(i).eventlongitude
													.toString(),
											Parameters.Events_Data.get(i).eventStartDate
													.toString(),
											Parameters.Events_Data.get(i).eventEndDate
													.toString(),
											Parameters.Events_Data.get(i).eventURI
													.toString(),
											Parameters.Events_Data.get(i).eventDescription
													.toString()));
						}

					}
					return UpdateEventsData;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return Collections.<EventsData> emptyList();
				}
			}

			// Events searched are displayed in listview
			@Override
			protected void onPostExecute(final List<EventsData> businesses) {
				ListView lv = (ListView) findViewById(R.id.ListViewEvent);
				lv.setAdapter(new EventAdapter(EventsBasedOnCategory.this,
						R.layout.activity_categoryevents,
						(ArrayList<EventsData>) businesses));
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						showEventsDetailsScreen(position,
								(ArrayList<EventsData>) businesses);

					}
				});
				if (dialog.isShowing()) {
					dialog.dismiss();
				}

			}
		}.execute();

	}

	// Events addition details such as URL, Start End time, Description are
	// displayed
	private void showEventsDetailsScreen(final int pos,
			final ArrayList<EventsData> eventItems) {
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater
				.from(EventsBasedOnCategory.this);
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

		new AlertDialog.Builder(EventsBasedOnCategory.this)
				.setView(eventDescripView).setNeutralButton("OK", null)
				.create().show();
	}

	// Events are displayes in listview
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
				view = vi.inflate(R.layout.activity_categoryevents, null);

			}

			EventsData ed = eventItems.get(position);
			if (ed != null) {
				TextView eventName = (TextView) view
						.findViewById(R.id.TextView_EventName);

				TextView eventOrganizer = (TextView) view
						.findViewById(R.id.TextView_EventOrganizer);
				final TextView eventAddress = (TextView) view
						.findViewById(R.id.TextView_EventAddress);

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
									EventsBasedOnCategory.this,
									MapViewActivity.class);
							eventsVenueCoOrdinates.putExtra("Latitude",
									Latitude);
							eventsVenueCoOrdinates.putExtra("Longitude",
									Longitude);
							startActivity(eventsVenueCoOrdinates);
						} else
							new AlertDialog.Builder(EventsBasedOnCategory.this)
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

}
