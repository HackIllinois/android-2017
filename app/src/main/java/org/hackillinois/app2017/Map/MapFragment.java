package org.hackillinois.app2017.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.annimon.stream.Stream;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.UI.CustomBottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapFragment extends Fragment implements DirectionCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LatLng userLocation;
    private LatLng endLocation;
    private static final int REQUEST_CODE = 12;
    private static final LatLng ECEB = new LatLng(40.114918, -88.228253);
    private static final LatLng SIEBEL = new LatLng(40.114026, -88.224807);
    private static final LatLng UNION = new LatLng(40.109387, -88.227246);
    private static final LatLng DCL = new LatLng(40.1131069, -88.228756);
    private GoogleApiClient mGoogleApiClient;
    private Unbinder unbinder;
    private IndoorMapViewer.Buildings toOpen;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            directions.clear();
            if (hasPermissions()) {
                LatLng currentLocation = (userLocation != null ? userLocation : findCurrentLocation());
                if (currentLocation != null) {
                    switch (v.getId()) {
                        case R.id.map_DCL:
                            endLocation = DCL;
                            setHeaderName("Digital Computer Laboratory");
                            break;
                        case R.id.map_Siebel:
                            endLocation = SIEBEL;
                            setHeaderName("Thomas Siebel Center for Computer Science");
                            break;
                        case R.id.map_ECEB:
                            endLocation = ECEB;
                            setHeaderName("Electrical Computer Engineering Building");
                            break;
                        case R.id.map_Union:
                            endLocation = UNION;
                            setHeaderName("Illini Union");
                            break;
                    }

                    if (endLocation == DCL) {
                        toOpen = IndoorMapViewer.Buildings.DCL;
                    } else if (endLocation == SIEBEL) {
                        toOpen = IndoorMapViewer.Buildings.SIEBEL;
                    } else if (endLocation == ECEB) {
                        toOpen = IndoorMapViewer.Buildings.ECEB;
                    } else if (endLocation == UNION) {
                        toOpen = IndoorMapViewer.Buildings.UNION;
                    }

                    indoorMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IndoorMapViewer.showMap(getContext(), toOpen);
                        }
                    });

                    toggle(v, currentLocation, endLocation);
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    };

    private BottomSheetBehavior mBottomSheetBehavior;
    private ArrayList<Object> directions;
    private DirectionsAdapter mAdapter;
    @BindView(R.id.map_bottomsheet_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.map_bottomsheet)
    LinearLayout bottomSheet;
    @BindView(R.id.map_bottomsheet_distance)
    TextView distance;
    @BindView(R.id.map_bottomsheet_name)
    TextView name;
    @BindView(R.id.map_bottomsheet_time)
    TextView time;
    @BindView(R.id.map_fab_indoormap)
    FloatingActionButton indoorMap;
    @BindView(R.id.map_fab_location)
    FloatingActionButton fab;
    @BindView(R.id.map_bottomsheet_header) LinearLayout header;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directions = new ArrayList<>();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_map, parent, false);
        unbinder = ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DirectionsAdapter(directions);
        mRecyclerView.setAdapter(mAdapter);

        ((MainActivity) getActivity()).setMapDCLOnClickListener(clickListener);
        ((MainActivity) getActivity()).setMapECEBOnClickListener(clickListener);
        ((MainActivity) getActivity()).setMapSiebelOnClickListener(clickListener);
        ((MainActivity) getActivity()).setMapUnionOnClickListener(clickListener);

        fab.setOnClickListener(v -> {
			if (hasPermissions()) {
				getMyLocation();
			}
		});

        mBottomSheetBehavior = CustomBottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        header.setOnClickListener(v -> {
			if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
				mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
			} else {
				mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
			}
		});


        return view;
    }

    @SuppressLint("Annotator")
    private void setDirections(Direction direction) {

        Leg leg = direction.getRouteList().get(0).getLegList().get(0);
        directions.add(null);

        Stream.of(leg.getStepList())
                .map(s -> new DirectionObject(parseHTMLStepText(s), s.getDistance().getText()))
                .forEach(directions::add);

        directions.add(null);
        mAdapter.notifyDataSetChanged();
    }

    private static String parseHTMLStepText(Step s) {
        return s.getHtmlInstruction()
				.replace("><", "> <")
				.replaceAll("&nbsp;", " ")
				.replaceAll("\\<.*?>","");
    }

    private void toggle(View v, LatLng currentLocation, LatLng endLocation) {
        TextView union = ((MainActivity) getActivity()).mapUnionText;
        TextView dcl = ((MainActivity) getActivity()).mapDCLText;
        TextView eceb = ((MainActivity) getActivity()).mapECEBText;
        TextView siebel = ((MainActivity) getActivity()).mapSiebelText;

        mMap.clear();
        if (((TextView) v).getCurrentTextColor() == ContextCompat.getColor(v.getContext(), R.color.seafoam_blue)) {
            ((TextView) v).setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            indoorMap.setVisibility(View.INVISIBLE);
        } else {
            union.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            dcl.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            eceb.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            siebel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            ((TextView) v).setTextColor(ContextCompat.getColor(v.getContext(), R.color.seafoam_blue));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            indoorMap.setVisibility(View.VISIBLE);
            requestDirection(currentLocation, endLocation);
        }
    }

    private void setupMap() {
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFrame);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapFrame, mSupportMapFragment).commit();
        } else {
            mSupportMapFragment.getMapAsync(googleMap -> {
				if (googleMap != null) {
					mMap = googleMap;

					if (userLocation != null) {
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(userLocation)      // Sets the center of the map to location user
								.zoom(19)                   // Sets the zoom
								.build();                   // Creates a CameraPosition from the builder
						//userLocation = new LatLng(location.getLatitude(), location.getLongitude());
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18.0f));
					}

					mMap.getUiSettings().setAllGesturesEnabled(true);
					mMap.setIndoorEnabled(false);
					mMap.getUiSettings().setMyLocationButtonEnabled(false);

					if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
							== PackageManager.PERMISSION_GRANTED) {
						googleMap.setMyLocationEnabled(true);
					}
				}
			});
        }
    }

    @SuppressLint("MissingPermission")
    // Permissions are assumed to be available when this is called
    private LatLng findCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void requestDirection(LatLng current, LatLng destination) {
        if (hasPermissions()) {
            GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_server_key))
                    .from(current)
                    .to(destination)
                    .transportMode(TransportMode.WALKING)
                    .execute(this);
        }
    }

    private void getMyLocation() {
        if (hasPermissions()) {
            LatLng latLng = findCurrentLocation();
            if (latLng != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                if (mMap != null) {
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            setHeader(direction.getRouteList().get(0).getLegList().get(0));
            setDirections(direction);

            int directionColor = ContextCompat.getColor(getContext(), R.color.sea);

            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(userLocation));
                mMap.addMarker(new MarkerOptions().position(endLocation));

                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                mMap.addPolyline(DirectionConverter.createPolyline(this.getContext(), directionPositionList, 6, directionColor));
            }

        } else {
            Toast.makeText(getContext(), "You're not in a valid location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!hasPermissions()) {
            return;
        }

        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // for some reason the linter doesn't see the checks inside of hasPermissions;

        if (location == null) {
            Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
            return;
        }
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        setupMap();
        //Toast.makeText(getContext(), location.toString(), Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CODE);
                }
            }
            Toast.makeText(getContext(), "Please enable location permissions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getContext(), "Please grant Location permissions.", Toast.LENGTH_SHORT).show();
                    ;
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Could not connect to Google Play Services. Requesting alternative location.", Toast.LENGTH_LONG).show();
        userLocation = findCurrentLocation();
        setupMap();
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setHeader(Leg leg) {
        this.distance.setText(leg.getDistance().getText());
        this.time.setText(leg.getDuration().getText());
    }

    private void setHeaderName(String name) {
        this.name.setText(name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
