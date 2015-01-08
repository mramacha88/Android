package iit.edu.iitmramacha_androidfinalproject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Events Venue position are displayed on the map.
 * 
 */

public class MapViewActivity extends android.support.v4.app.FragmentActivity {

	private static double latitude;
	private static double longitude;

	private static CameraPosition venuePosition;
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mapview);

		latitude = getIntent().getExtras().getDouble("Latitude");
		longitude = getIntent().getExtras().getDouble("Longitude");

		venuePosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(15.5f)
				.bearing(300).tilt(50).build();
		setUpMapIfNeeded();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// We will provide our own zoom controls.
		mMap.getUiSettings().setZoomControlsEnabled(false);

		if (!checkReady()) {
			return;
		}

		changeCamera(CameraUpdateFactory.newCameraPosition(venuePosition));
		mMap.addMarker(new MarkerOptions().position(
				new LatLng(latitude, longitude)).title("Marker"));
	}

	/**
	 * When the map is not ready the CameraUpdateFactory cannot be used. This
	 * should be called on all entry points that call methods on the Google Maps
	 * API.
	 */
	private boolean checkReady() {
		if (mMap == null) {

			return false;
		}
		return true;
	}

	/**
	 * Called when the zoom in button (the one with the +) is clicked.
	 */
	public void onZoomIn(View view) {
		if (!checkReady()) {
			return;
		}

		changeCamera(CameraUpdateFactory.zoomIn());
	}

	/**
	 * Called when the zoom out button (the one with the -) is clicked.
	 */
	public void onZoomOut(View view) {
		if (!checkReady()) {
			return;
		}

		changeCamera(CameraUpdateFactory.zoomOut());
	}

	private void changeCamera(CameraUpdate update) {
		changeCamera(update, null);
	}

	/**
	 * Change the camera position by moving or animating the camera depending on
	 * the state of the animate toggle button.
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {

		mMap.animateCamera(update, callback);
	}
}
