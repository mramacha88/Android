package iit.edu.mramacha_androidassignment3;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Meghashree M Ramachandra This Activity displays building information
 *         of the respective building selected Building Information is retrieved
 *         from web using JSON and respective data and images are handled using
 *         Async Task.
 * 
 */

public class BuildingsDetailsUsingJsonAsyncTask extends Activity {

	private Button mButton_BackButton = null;
	private TextView mTextView_BuildingTitle = null;
	private ImageView mImageView_BuildingImage = null;
	private TextView mTextView_BuildingCode = null;
	private TextView mTextView_BuildingAddress = null;
	private TextView mTextView_BuildingDescription = null;
	static Bitmap mainBitmap;
	String buildingSelectedFromList;
	private Building buildDetails = new Building();
	private String TAG_NAME = "Name";
	private String TAG_IMAGE = "Image";
	private String TAG_ADDRESS = "Address";
	private String TAG_CODE ="Code" ;
	private String TAG_DESCRIPTION = "Description";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_building_details);
		// Start Async Task to download image and retrieve building details
		// and update fields respectively
		new JSONDialogUIAsyncTask(BuildingsDetailsUsingJsonAsyncTask.this)
				.execute();
		// back button listener to navigate from current activity to previous
		// activity.
		mButton_BackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	// Retrieves building image and data from the respective URL's and
	// displays the values on activity.
	private class JSONDialogUIAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;

		public JSONDialogUIAsyncTask(Activity activity) {
			this.dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Start ProgressDialog Loading.... message
			this.dialog.setMessage("Loading.....");
			this.dialog.show();
			buildingSelectedFromList = getIntent().getExtras().getString(
					"selectedPosition");
			mTextView_BuildingTitle = (TextView) findViewById(R.id.TextViewBuildingTitle);
			mImageView_BuildingImage = (ImageView) findViewById(R.id.ImageViewBuilding);
			mTextView_BuildingCode = (TextView) findViewById(R.id.TextViewBuldingCode);
			mTextView_BuildingAddress = (TextView) findViewById(R.id.TextViewBuldingAddress);
			mTextView_BuildingDescription = (TextView) findViewById(R.id.TextViewBuldingDescription);
			mButton_BackButton = (Button) findViewById(R.id.ButtonBack);

		}

		protected Void doInBackground(Void... params) {
			try {
				String buildings = readBuildingDetails();
				JSONObject jsonObject = new JSONObject(buildings);
				JSONObject d = jsonObject.getJSONObject("d");
				buildDetails.setName(d.getString(TAG_NAME).toString());
				buildDetails.setBuildingCode(d.getString(TAG_CODE).toString());
				buildDetails.setBuildingAddress(d.getString(TAG_ADDRESS)
						.toString());
				if (d.getString(TAG_DESCRIPTION).toString().equals("null")) {
					if (buildDetails.getName().equals(
							getResources().getString(
									R.string.alumni_memorial_text))) {
						buildDetails.setBuildingDescription(getResources().getString(
								R.string.alumni_memorial_description));
					}
				} else {
					buildDetails.setBuildingDescription(d.getString(
							TAG_DESCRIPTION).toString());
				}
				String imageURL = d.getString(TAG_IMAGE);// "http://www.iit.edu/campus_and_conference_centers/meeting_spaces/academic_space/images/stuart_hall.jpg";//d.getString("Image");
				if (imageURL.equals("null")) {
					if (buildDetails.getName().equals(
							getResources().getString(
									R.string.alumni_memorial_text))) {
						buildDetails
								.setImageURL("http://sdngnet.com/Gallery2/d/6204-2/Chicago_IIT_Alumni_Memorial_Hall_by_Mies_Van_der_Rohe_3_Mini.jpg");
					}
				} else {
					buildDetails.setImageURL(imageURL);
				}
				mainBitmap = BuildingsDetailsUsingJsonAsyncTask
						.getImageFromWeb(getBaseContext(),
								buildDetails.getImageURL());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mTextView_BuildingTitle.setText(buildDetails.getName());
			mTextView_BuildingCode.setText(getResources().getString(
					R.string.textview_buildingcode_text)
					+ ' ' + buildDetails.getBuildingCode());
			mTextView_BuildingAddress.setText(getResources().getString(
					R.string.textview_address_text)
					+ ' ' + buildDetails.getBuildingAddress());
			mTextView_BuildingDescription.setText(buildDetails
					.getBuildingDescription());
			if (buildDetails.getImageURL().equals(
					getResources().getString(R.string.no_image))) {
				mImageView_BuildingImage
						.setBackgroundResource(R.drawable.notfound);
			} else {
				mImageView_BuildingImage.setImageBitmap(mainBitmap);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	// URL to retrieve building data is passed for HTTP connection.
	public String readBuildingDetails() {
		return issueJSONWebRequest(
				"http://brookfield.rice.iit.edu/jmeyers/mobile/classsource/Buildings/BuildingService.aspx/getBuildingDetailsByName",
				"{'Name' : '" + buildingSelectedFromList + "'}");
	}

	// Establishes the connection with the URL mentioned and retrieves data
	private static String issueJSONWebRequest(String stringURL, String data) {
		StringBuilder builder = new StringBuilder();
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(stringURL);

		try {
			StringEntity se = new StringEntity(data);
			se.setContentType("text/xml");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));

			httppost.setEntity(se);

			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-Type", "application/json");
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
		}

		return builder.toString();
	}

	// To download Image from URL
	public static Bitmap getImageFromWeb(Context c, String imageLoc) {

		Bitmap bt = downloadFile(imageLoc);
		try {
			FileOutputStream fos = c.openFileOutput(imageLoc,
					Context.MODE_WORLD_READABLE);
			bt.compress(CompressFormat.JPEG, 90, fos);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bt;
	}

	// Download a file from the respective URL
	public static Bitmap downloadFile(String fileUrl) {
		Bitmap bmImg = null;
		URL myFileUrl = null;
		try {
			// base url for file
			myFileUrl = new URL(fileUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection URLConnection = (HttpURLConnection) myFileUrl
					.openConnection();
			URLConnection.setDoInput(true);
			URLConnection.connect();
			URLConnection.getContentLength();
			InputStream is = URLConnection.getInputStream();
			bmImg = BitmapFactory.decodeStream(is);
			URLConnection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmImg;
	}

}
