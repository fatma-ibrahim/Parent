package org.code.parentsplashscreen.ui;

import com.google.gson.Gson;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;


import org.code.parentsplashscreen.models.LocationRealTime;
import org.code.parentsplashscreen.responses.ShowTripResponse;
import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.viewmodels.DriverViewModel;
import org.code.parentsplashscreen.viewmodels.TripViewModel;
import org.codeforiraq.parentsplashscreen.R;
import org.codeforiraq.parentsplashscreen.databinding.FragmentTrackingBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import stream.customalert.CustomAlertDialogue;

public class TrackingFragment extends Fragment implements OnMapReadyCallback {
    private PermissionsManager permissionsManager;
    private MapboxMap map;
    private MapView mapView;
    private Marker markeri;
    private Point destination;
    private NavigationMapRoute navigationMapRoute;
    private LatLng parent;
    //private LocationManager manager;
    private double latitude;
    private double longitude;
    Pusher pusher;
    private TripViewModel tripViewModel;
    private int tripId;
    private Point school;
    private int trip_status;
    private String school_name;
    private FragmentTrackingBinding trackingBinding;
    private boolean arrived = false;

    public TrackingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trackingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracking, container, false);
        //View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        //mapView = (MapView) view.findViewById(R.id.mapView);
        mapView = trackingBinding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        tripViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);

        return trackingBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = new LatLng(Double.parseDouble(SharedPreferencesManager.getInstance(getActivity()).getUser().getLit()), Double.parseDouble(SharedPreferencesManager.getInstance(getActivity()).getUser().getLng()));
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        pusher = new Pusher("a7171d09fe99b7303a96", options);
        showTrip();
    }

    private void showTrip() {
        trackingBinding.progress.setVisibility(View.VISIBLE);
        tripViewModel.showTrip("Bearer " + SharedPreferencesManager.getInstance(getActivity()).getToken()).observe(getActivity(), new Observer<ShowTripResponse>() {
            @Override
            public void onChanged(ShowTripResponse showTripResponse) {
                trackingBinding.progress.setVisibility(View.INVISIBLE);
                if (showTripResponse != null && showTripResponse.isSuccess()) {
                    showDialog("Trip Information", showTripResponse.getData());
                    Log.d("trip", "trip." + showTripResponse.getMessage().getTrip().getStatus() + "" + showTripResponse.getMessage().getTrip().getId());
                    school_name = showTripResponse.getMessage().getSchool().getName();
                    tripId = showTripResponse.getMessage().getTrip().getId();
                    trip_status = showTripResponse.getMessage().getTrip().getStatus();
                    school = Point.fromLngLat(Double.parseDouble(showTripResponse.getMessage().getSchool().getLng()),
                            Double.parseDouble(showTripResponse.getMessage().getSchool().getLit()));
                    pusherListener();
                } else if (showTripResponse != null && !showTripResponse.isSuccess()) {
                    showDialog("Trip Information", showTripResponse.getData());
                }
            }
        });
    }

    private void pusherListener() {
        if (tripId != -1) {
            pusher.connect(new ConnectionEventListener() {
                @Override
                public void onConnectionStateChange(ConnectionStateChange change) {
                    Log.i("Pusher", "State changed from " + change.getPreviousState() +
                            " to " + change.getCurrentState());
                }

                @Override
                public void onError(String message, String code, Exception e) {
                    Log.i("Pusher", "There was a problem connecting! " +
                            "\ncode: " + code +
                            "\nmessage: " + message +
                            "\nException: " + e
                    );
                }
            }, ConnectionState.ALL);

            Channel channel = pusher.subscribe("trip." + tripId);

            channel.bind("triplocation", new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    Log.i("Pusher", "Received event with data: " + event.toString());
                    Gson gson = new Gson();
                    LocationRealTime location = gson.fromJson(event.getData(), LocationRealTime.class);
                    if (location.getParent_id() != null) {
                        String[] split = location.getParent_id().split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].contains(String.valueOf(SharedPreferencesManager.getInstance(getActivity()).getUser().getId()))) {
                                Log.d("pppppp", "yes");
                                arrived = true;
                            }
                        }
                    }
                    latitude = Double.parseDouble(location.getLit());
                    longitude = Double.parseDouble(location.getLng());
                    markeri.setPosition(new LatLng(latitude, longitude));
                    cameraPosition(new LatLng(latitude, longitude));
                    if (trip_status == 1 && arrived == false) {
                        getRoute(markeri, parent, school);
                    } else if (trip_status == 1 && arrived == true) {
                        getRouteToSchool(markeri, school);
                    }
                    if (trip_status == 3) {
                        getRouteToParent(markeri, parent);
                    }
                    //Toast.makeText(getActivity(), event+"", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        /** Initialized The Marker Of the driver **/
        markeri = map.addMarker(new MarkerOptions()
                .setTitle("Driver")
                .position(new LatLng(0, 0))
                .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.bus_marker))
        );

    }



    private void getRoute(Marker driver, LatLng parent, Point dest) {
        NavigationRoute.Builder builder = NavigationRoute.builder()  // Initializing the route builder
                .accessToken(Mapbox.getAccessToken())
                .origin(Point.fromLngLat(driver.getPosition().getLongitude(), driver.getPosition().getLatitude()))
                .addWaypoint(Point.fromLngLat(parent.getLongitude(), parent.getLatitude()))
                .destination(dest);

        /** Initialized The Marker Of the School **/
        map.addMarker(new MarkerOptions()
                .setTitle(school_name)
                .position(new LatLng(dest.latitude(), dest.longitude()))
                .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.school_marker))
        );
        /** Initialized The Marker Of the Parent Home **/
        map.addMarker(new MarkerOptions()
                .setTitle(SharedPreferencesManager.getInstance(getActivity()).getUser().getName())
                .position(new LatLng(parent))
                .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.home_marker))
        );
        builder.build() // Building the route
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e("hossam", "NO VALID RESPONSE");
                            return;
                        } else {
                            if (response.body().routes().size() == 0) {
                                Log.e("hossam", "NO ROUTES FOUND");
                                return;
                            }
                            DirectionsRoute currentRoute = response.body().routes().get(0);
                            try {
                                currentRoute = response.body().routes().get(0);
                            } catch (NullPointerException e) {
                                Log.e("hossam", "BAD ROUTE, NULL POINTER EXCEPTION: " + e.getMessage());
                            }
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                                Log.d("hossam", "Old route removed");
                            } else {
                                try {
                                    navigationMapRoute = new NavigationMapRoute(mapView, map);
                                    Log.d("hossam", "New route created");
                                } catch (Exception e) {
                                    Log.d("hossam", "null object reference " + e.getMessage().toString());
                                }

                            }
                            try {
                                navigationMapRoute.addRoute(currentRoute);
                                Log.d("hossam", "New route added");
                            } catch (Exception e) {
                                Log.d("hossam", "FAILED TO ADD NEW ROUTE" + e.getMessage());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e("hossam", t.getMessage() + "--error--");
                    }
                });
    }

    private void getRouteToParent(Marker driver, LatLng parent) {
        NavigationRoute.Builder builder = NavigationRoute.builder()  // Initializing the route builder
                .accessToken(Mapbox.getAccessToken())
                .origin(Point.fromLngLat(driver.getPosition().getLongitude(), driver.getPosition().getLatitude()))
                .destination(Point.fromLngLat(parent.getLongitude(), parent.getLatitude()));

        /** Initialized The Marker Of the Parent Home **/
        map.addMarker(new MarkerOptions()
                .setTitle(SharedPreferencesManager.getInstance(getActivity()).getUser().getName())
                .position(new LatLng(parent))
                .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.home_marker))
        );
        builder.build() // Building the route
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e("hossam", "NO VALID RESPONSE");
                            return;
                        } else {
                            if (response.body().routes().size() == 0) {
                                Log.e("hossam", "NO ROUTES FOUND");
                                return;
                            }
                            DirectionsRoute currentRoute = response.body().routes().get(0);
                            try {
                                currentRoute = response.body().routes().get(0);
                            } catch (NullPointerException e) {
                                Log.e("hossam", "BAD ROUTE, NULL POINTER EXCEPTION: " + e.getMessage());
                            }
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                                Log.d("hossam", "Old route removed");
                            } else {
                                try {
                                    navigationMapRoute = new NavigationMapRoute(mapView, map);
                                    Log.d("hossam", "New route created");
                                } catch (Exception e) {
                                    Log.d("hossam", "null object reference " + e.getMessage().toString());
                                }

                            }
                            try {
                                navigationMapRoute.addRoute(currentRoute);
                                Log.d("hossam", "New route added");
                            } catch (Exception e) {
                                Log.d("hossam", "FAILED TO ADD NEW ROUTE" + e.getMessage());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e("hossam", t.getMessage() + "--error--");
                    }
                });
    }

    private void getRouteToSchool(Marker driver, Point school) {
        NavigationRoute.Builder builder = NavigationRoute.builder()  // Initializing the route builder
                .accessToken(Mapbox.getAccessToken())
                .origin(Point.fromLngLat(driver.getPosition().getLongitude(), driver.getPosition().getLatitude()))
                .destination(school);

        /** Initialized The Marker Of the Parent Home **/
        map.addMarker(new MarkerOptions()
                .setTitle(school_name)
                .position(new LatLng(school.latitude(), school.longitude()))
                .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.school_marker))
        );
        builder.build() // Building the route
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e("hossam", "NO VALID RESPONSE");
                            return;
                        } else {
                            if (response.body().routes().size() == 0) {
                                Log.e("hossam", "NO ROUTES FOUND");
                                return;
                            }
                            DirectionsRoute currentRoute = response.body().routes().get(0);
                            try {
                                currentRoute = response.body().routes().get(0);
                            } catch (NullPointerException e) {
                                Log.e("hossam", "BAD ROUTE, NULL POINTER EXCEPTION: " + e.getMessage());
                            }
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                                Log.d("hossam", "Old route removed");
                            } else {
                                try {
                                    navigationMapRoute = new NavigationMapRoute(mapView, map);
                                    Log.d("hossam", "New route created");
                                } catch (Exception e) {
                                    Log.d("hossam", "null object reference " + e.getMessage().toString());
                                }

                            }
                            try {
                                navigationMapRoute.addRoute(currentRoute);
                                Log.d("hossam", "New route added");
                            } catch (Exception e) {
                                Log.d("hossam", "FAILED TO ADD NEW ROUTE" + e.getMessage());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e("hossam", t.getMessage() + "--error--");
                    }
                });
    }


    private void cameraPosition(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0), 10000);
    }

    private void showDialog(String title, String message) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(title)
                .setMessage(message)
                .setNegativeText("OK")
                .setNegativeColor(R.color.purple_200)
                .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        if (navigationMapRoute != null) {
            navigationMapRoute.removeRoute();
        }
    }

}