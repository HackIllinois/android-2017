package org.hackillinois.app2017.Map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapFragment extends Fragment implements DirectionCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LatLng userLocation;
    private LatLng endLocation;
    private static final LatLng ECEB = new LatLng(40.114918, -88.228253);
    private static final LatLng SIEBEL = new LatLng(40.114026, -88.224807);
    private static final LatLng UNION = new LatLng(40.109387, -88.227246);
    private HashSet<LatLng> visited;
    private GoogleApiClient mGoogleApiClient;
    private Unbinder unbinder;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            directions.clear();
            if(hasPermissions())  {
                Header header;
                String location = "";
                LatLng currentLocation = (userLocation != null ? userLocation : findCurrentLocation());
                if( currentLocation != null) {
                    switch (v.getId()) {
                        case R.id.map_DCL:
                            toggle(v);
                            setHeader("Digital Computer Laboratory", "0.4 mi", "9 min");
                            location = "DCL";
                            break;
                        case R.id.map_Siebel:
                            toggle(v);
                            setHeader("Thomas Siebel Center for Computer Science", "0.4 mi", "9 min");
                            location = "Siebel Center";
                            endLocation = SIEBEL;
                            break;
                        case R.id.map_ECEB:
                            toggle(v);
                            setHeader("Electrical Computer Engineering Building", "0.4 mi", "9 min");
                            location = "Electrical and Computer Engineering Building";
                            endLocation = ECEB;
                            break;
                        case R.id.map_Union:
                            toggle(v);
                            setHeader("Illini Union", "0.4 mi", "9 min");
                            location = "Illini Union";
                            endLocation = UNION;
                            break;
                    }

                    if (!visited.contains(endLocation)) {
                        // Toast.makeText(getContext(), "Getting directions to " + location, Toast.LENGTH_SHORT).show();
                        requestDirection(currentLocation, endLocation);
                        visited.add(endLocation);
                    } else {
                        Toast.makeText(getContext(), "You already requested this location", Toast.LENGTH_SHORT).show();
                    }
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
    @BindView(R.id.map_bottomsheet_indoormap)
    TextView indoorMap;
    @BindView(R.id.map_fab_location)
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        visited = new HashSet<>();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPermissions()) {
                    getMyLocation();
                }
            }
        });

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        return view;
    }

    private void setDirections(Direction direction) {
        List<Route> list = direction.getRouteList();

        for (Route r : list) {
            directions.add(new DirectionObject(r.getSummary(), "wat"));
        }

        mAdapter.notifyDataSetChanged();
    }

    private void toggle(View v) {
        TextView union = ((MainActivity) getActivity()).mapUnionText;
        TextView dcl = ((MainActivity) getActivity()).mapDCLText;
        TextView eceb = ((MainActivity) getActivity()).mapECEBText;
        TextView siebel = ((MainActivity) getActivity()).mapSiebelText;

        if (((TextView) v).getCurrentTextColor() == ContextCompat.getColor(v.getContext(), R.color.seafoam_blue)) {
            ((TextView) v).setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            union.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            dcl.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            eceb.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            siebel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.faded_blue));
            ((TextView) v).setTextColor(ContextCompat.getColor(v.getContext(), R.color.seafoam_blue));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
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
                }
            });
        }
    }

    private LatLng findCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void requestDirection(LatLng current, LatLng destination) {
        if(hasPermissions()) {
            GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_server_key))
                    .from(current)
                    .to(destination)
                    .transportMode(TransportMode.WALKING)
                    .execute(this);
        }
    }

    private void getMyLocation() {
        if(hasPermissions()) {
            LatLng latLng = findCurrentLocation();
            if( latLng != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                if(mMap != null) {
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            setDirections(direction);

            int directionColor;

            if (endLocation == SIEBEL) {
                directionColor = Color.RED;
            } else if (endLocation == ECEB) {
                directionColor = Color.BLACK;
            } else {
                directionColor = Color.GREEN;
            }

            if(mMap != null) {
                mMap.addMarker(new MarkerOptions().position(userLocation));
                mMap.addMarker(new MarkerOptions().position(endLocation));

                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                mMap.addPolyline(DirectionConverter.createPolyline(this.getContext(), directionPositionList, 5, directionColor));
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
        if(!hasPermissions()) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location == null) {
            Toast.makeText(getContext(),"Please enable location services", Toast.LENGTH_SHORT).show();
            return;
        }
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        setupMap();
        //Toast.makeText(getContext(), location.toString(), Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getContext(),"Please enable location permissions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private void setHeader(String name, String distance, String time) {
        this.name.setText(name);
        this.distance.setText(distance);
        this.time.setText(time);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
