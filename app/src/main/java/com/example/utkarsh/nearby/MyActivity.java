package com.example.utkarsh.nearby;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Movie;
import android.location.Location;
import android.os.Bundle;

/**
 * Created by utkarsh on 9/7/16.
 */
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends FragmentActivity
        implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public static List<Place> placelist = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private String LOG_TAG;
    public static List<Integer> list1 = new ArrayList<>();
    private JSONArray reader;
    private Location mLastLocation;
    private TabLayout.Tab mLatitudeText;
    private TabLayout.Tab mLongitudeText;
    private int i = 0;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private JSONObject reader1;
    private String name;
    private String Addressname;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Toolbar toolbar;
private ArrayList<String> notificationshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        //  setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        notificationshow =new ArrayList<>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        gps = new GPSTracker(MyActivity.this);
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

     //   startingcode();
        gpsenablecheck();
        navigationdrawersetup(savedInstanceState);
        ImageButton ib = (ImageButton)findViewById(R.id.navigationbutton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                Log.i("onDrawerClosed","working");
                for (int i = 0; i < navDrawerItems.size(); i++) {
                    NavDrawerItem country = navDrawerItems.get(i);
                    if (country.getifcheckboxselected()) {
                        responseText.append("\n" + country.getTitle());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();


            }
        });
    }

    private void gpsenablecheck() {
        // Check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    private void navigationdrawersetup(Bundle savedInstanceState) {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);


        navDrawerItems = new ArrayList<NavDrawerItem>();
boolean ischeckboxchecked =false;
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1),ischeckboxchecked));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1),ischeckboxchecked));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1),ischeckboxchecked));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1),ischeckboxchecked));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1),ischeckboxchecked));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1),ischeckboxchecked));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        ImageButton open_button = (ImageButton) mDrawerLayout.findViewById(R.id.button);
        open_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerList);
                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        mDrawerToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


    }

    public void startingcode() {

        Log.i("Starting code", "Starting code begining");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new PlacesAdapter(placelist);


        // final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //  layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //  recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        MyActivity.ClickListener x = new MyActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Place place = placelist.get(position);
                Toast.makeText(getApplicationContext(), place.getname() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };

        recyclerView.addOnItemTouchListener(new MyActivity.RecyclerTouchListener(getApplicationContext(), recyclerView, x));
       String  url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=hospital&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
        prepareMovieData(url);

        Log.i("placelistt size", String.valueOf(placelist.size()));
        for (int i = 0; i < placelist.size(); i++) {
            System.out.println("list***********" + placelist.get(i).getname());
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {


        //   Log.i("latitude", String.valueOf(mLastLocation.getLatitude()));
        //   Log.i("longitude", String.valueOf(mLastLocation.getLongitude()));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("connection failed", result.getErrorMessage());
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MyActivity) getActivity()).onDialogDismissed();
        }
    }


    // TODO: Please implement GoogleApiClient.OnConnectionFailedListener to
    // handle connection failures.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        startingcode();
    }


    private void prepareMovieData(String url) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        Log.i("njnvfjvnvjnvvjn", "dvnvjnvnvn");
        // url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=restaurant&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                    //    Log.i("Response is: ", response);
                        try {
                            JSONObject m = new JSONObject(response);
                            reader = m.getJSONArray("results");
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject name = (JSONObject) reader.get(i);
                                String name1 = name.getString("name");
                                Log.i("NAME", name1);
                                String id = name.getString("place_id");
                             //   System.out.println(id);
                                callplcaedetails(id);
                                //  https:
//maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY


                            }
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Not worked", "That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callplcaedetails(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("id" + id);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                      //  Log.i("Response is: ", response);

                        JSONObject m = null;

                        try {
                            m = new JSONObject(response);


                            reader1 = m.getJSONObject("result");
                            String nameofplace = reader1.getString("name");

                            Addressname = reader1.getString("formatted_address");
                            placelist.add(new Place(nameofplace, Addressname, (float) 0.004));
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Not worked", "That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MyActivity.ClickListener clickListener;

        RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final MyActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    System.out.println("ddddddddddddddddddddd");
                    Intent i = new Intent(context, MapPane.class);
                    startActivity(i);
                    finish();
                    return super.onDoubleTap(e);

                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item


            displayView(position);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
       /* switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true;
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //   menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=restaurant&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url);
                break;
            case 1:
                String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=atm&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url1);
                break;
            case 2:
                String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=restaurant&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url2);
                break;
            case 3:
                String url3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=atm&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url3);
                break;
            case 4:
                String url4 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=atm&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url4);
                break;
            case 5:
                String url5 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=5000&type=atm&key=AIzaSyDi9vrZ0F3TXlGM0wwkUWBxGER8NkY3GlQ";
                prepareMovieData(url5);
                break;

            default:
                break;
        }

   /*     if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);

            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }*/
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}

