package iit.edu.mramacha_androidassignment3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Meghashree M Ramachandra
 * 
 * This Activity display the lists of IIT Main Campus Buildings in a List view. 
 * This is done by retrieving data from web using JSON and handling the same in Async Task
 * When an building is selected from listview next activity is started which displays the details about the building selected.
 *
 */
public class BuildingsListUsingJsonAsyncTask extends Activity {
	public static ArrayList<String> buildingsList = new ArrayList<String>();
	ListView buildingsListView =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_buildings_list);
		new JSONDialogUIAsyncTask(BuildingsListUsingJsonAsyncTask.this)
				.execute();		
	}

	private class JSONDialogUIAsyncTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;
		private Activity activity;
		
		//Constructor
		public JSONDialogUIAsyncTask(Activity activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Start ProgressDialog Loading.... message
			this.dialog.setMessage("Loading.....");
			this.dialog.show();
		}
		
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				String buildings = readBuildings();
				JSONObject jsonObject = new JSONObject(buildings);
				JSONArray bulidingsListtoArray = jsonObject.getJSONArray("d");
				for (int i = 0; i < bulidingsListtoArray.length(); i++) {
					buildingsList.add(bulidingsListtoArray.getString(i)
							.toString());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			buildingsListView = (ListView) findViewById(R.id.buldingsListView);
			buildingsListView.setAdapter(new BuildingListAdapter(activity, R.layout.activity_buildings_row, buildingsList));
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			buildingsListView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int pos,
						long id) {
					// TODO Auto-generated method stub
					Intent detailsDisplay = new Intent(BuildingsListUsingJsonAsyncTask.this,BuildingsDetailsUsingJsonAsyncTask.class);
					detailsDisplay.putExtra("selectedPosition", buildingsList.get(pos));
					startActivity(detailsDisplay);				
				}
			});
		}
	}
	
	public String readBuildings() {
		return issueJSONWebRequest("http://brookfield.rice.iit.edu/jmeyers/mobile/classsource/Buildings/BuildingService.aspx/getBuildingNames");
	}

	private String issueJSONWebRequest(String stringURL) {
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
		}
		return builder.toString();
	}

	//Custom List View
	private class BuildingListAdapter extends ArrayAdapter<String> {		
		private ArrayList<String> items;
		public BuildingListAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId,items);	
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.activity_buildings_row, null);
			}
			if (items.get(position) != null) {
				TextView buildingName = (TextView) v.findViewById(R.id.buildingName);
				if (buildingName != null) {
					buildingName.setText(items.get(position));
				}
			}
			return v;
		}
		
	}

	 //Adding menu data.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }
    
    //About activity started from menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_about:
            Intent aboutIntent = new Intent(BuildingsListUsingJsonAsyncTask.this, About.class);
            startActivity(aboutIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }	

}
