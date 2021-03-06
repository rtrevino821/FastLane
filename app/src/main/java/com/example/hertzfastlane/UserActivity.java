package com.example.hertzfastlane;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class UserActivity extends AppCompatActivity {
    //public Button button;

    /* hamburger menu variables */
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    /* hamburger toggle bar variables */
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ProgressBar spinner;

    ArrayList<Integer> carsList = new ArrayList<Integer>();
    String carClassLog;

    Animation animSlideDown, animSlideUp;

    private ImageView backgroundView;
    String TAG = "/UserActivity";


    final Context context = this;
    Member member;

    int arrayListInt;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        backgroundView = (ImageView) findViewById(R.id.home_bg);
        displayBackgroundImages();

        spinner =(ProgressBar)findViewById(R.id.progress_loader);
        spinner.setVisibility(View.GONE);

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        //ReplaceFont.replaceDefaultFont(this, "DEFAULT", "segoeuib.ttf" );

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        /** Hamburger display menu*/
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        /** Hamburger toggle action bar */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        /** toggle hamburger layout   **strings xml added in res folder*/
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** used when drawer is open   ** not fully working should display Gold Star! */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Gold Star!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** used when a drawer is closed */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        setupDrawer();


        setupDrawer();  /* function needs to be fixed  currently not in use */


        /** on click drawer options */
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserActivity.this, "Thank You For Using Hertz!", Toast.LENGTH_SHORT).show();
            }
        });
        /** hamburger layout ended ^^ */

        final EditText etName = (EditText) findViewById(R.id.etName);
        final TextView tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        final Button bMyReservation = (Button) findViewById(R.id.bMyReservation);

        member = LoginActivity.getMember();

 //       String name = member.getFirst_NM();
     //   tvWelcome.setText("Welcome " + name + ",\nLet us know how we can help.");
        tvWelcome.startAnimation(animSlideDown);
        //tvWelcome.startAnimation(animSlideUp);


        bMyReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Intent reservationIntent = new Intent(UserActivity.this, MyReservationActivity.class);
                UserActivity.this.startActivity(reservationIntent);
                //spinner.setVisibility(View.GONE);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("User Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (mDrawerToggle.onOptionsItemSelected(item)) { // added for hamburger toggle
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_maps:
                startActivity(new Intent(this, MapActivity.class));
                return true;
            case R.id.action_help:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
            case R.id.Beacon:
                startActivity(new Intent(this, beacons.class));  // estimote beacons action bar, created string and extra action button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDrawerItems() {  // hamburger layout display method
        String[] osArray = { "Profile", "Home", "Rental", "Gold Plus Rewards", "Goodbye" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
    /** Not currently used  needs to be fixed */
    private void setupDrawer() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void addAdrenalineCarList()
    {
        carsList.add(R.drawable.g_wagon);
        carsList.add(R.drawable.camaro_front);
        carsList.add(R.drawable.camaro_side);
        carsList.add(R.drawable.e_class);
        carsList.add(R.drawable.mustang_closeup);
        //Prestige
        carsList.add(R.drawable.a8);
        carsList.add(R.drawable.benz);
        carsList.add(R.drawable.maserati);
    }

    private void addPrestigeCarList()
    {
        carsList.add(R.drawable.a8);
        carsList.add(R.drawable.benz);
        carsList.add(R.drawable.maserati);

    }
    private void loadImages(int index)
    {
        //Uri uri = Uri.parse("android.resource://com.example.hertzfastlane/drawable/benz");


        Uri uri = Uri.fromFile(new File("android.resource://com.example.hertzfastlane/drawable/" + "benz.jpg"));
        Picasso.with(context).load(carsList.get(arrayListInt)).into(backgroundView);


    }


    private void displayBackgroundImages()
    {

        int classInt = (int) ( Math.random() * 2 + 1); // will return either 1 or 2
        Random rand = new Random();
        int adrenalineInt = rand.nextInt(8);
        int prestigeInt = rand.nextInt(3)+0;
        int randomInt = (int) ( Math.random() * 2 + 1); // will return either 1 or 2

        if(true)
        {
            addAdrenalineCarList();
            arrayListInt = adrenalineInt;
            loadImages(adrenalineInt);
            Log.d(TAG,"Adrenaline: " + adrenalineInt);
        }
//        else
//        {
//            addPrestigeCarList();
//            arrayListInt = prestigeInt;
//            loadImages(prestigeInt);
//            Log.d(TAG,"Prestige: " + prestigeInt);
//        }
        carsList.clear();
    }


}