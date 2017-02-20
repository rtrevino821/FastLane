package com.example.hertzfastlane;

/**
 * Created by Steven J on 2/17/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.cloud.model.Color;
import com.example.hertzfastlane.estimote.BeaconID;
import com.example.hertzfastlane.estimote.EstimoteCloudBeaconDetails;
import com.example.hertzfastlane.estimote.EstimoteCloudBeaconDetailsFactory;
import com.example.hertzfastlane.estimote.ProximityContentManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.hertzfastlane.MyReservationActivity.convertStreamToString;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class beacons extends AppCompatActivity {

    private static final String TAG = "beacons";

    private static final Map<Color, Integer> BACKGROUND_COLORS = new HashMap<>();

    static {
        BACKGROUND_COLORS.put(Color.ICY_MARSHMALLOW, android.graphics.Color.rgb(109, 170, 199));
        BACKGROUND_COLORS.put(Color.BLUEBERRY_PIE, android.graphics.Color.rgb(98, 84, 158));
        BACKGROUND_COLORS.put(Color.MINT_COCKTAIL, android.graphics.Color.rgb(155, 186, 160));
    }

    private static final int BACKGROUND_COLOR_NEUTRAL = android.graphics.Color.rgb(160, 169, 172);

    private ProximityContentManager proximityContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EstimoteSDK.initialize(getApplicationContext(), "stevenjoy99-yahoo-com-s-yo-lyx", "0f4d0fa349ea5d6604f52b776a9653c8");

        Log.d("Tag", "Beacons");
        setContentView(R.layout.activity_main);
        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        // TODO: replace with UUIDs, majors and minors of your own beacons
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 32725,55822),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 20930, 14720),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 226788, 12168)), //

                new EstimoteCloudBeaconDetailsFactory());
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;
                Integer backgroundColor;

                if (content != null ) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
                    text = "You're in " + beaconDetails.getBeaconName() + "'s range!";
                    backgroundColor = BACKGROUND_COLORS.get(beaconDetails.getBeaconColor());

                    if (beaconDetails.getBeaconName().equals("ice")) {

                        Intent mapActivityIntent = new Intent(beacons.this, MapActivity.class);
                        beacons.this.startActivity(mapActivityIntent);

                        Runnable runnable = new Runnable(){
                            @Override
                            public void run(){
                                HttpClient httpClient = new DefaultHttpClient();

                                HttpGet request = new HttpGet("https://a83ypd2j44.execute-api.us-east-1.amazonaws.com/prod/testBeacons");


                                HttpResponse response;

                                try{
                                    response = httpClient.execute(request);
                                    HttpEntity entity = response.getEntity();
                                    InputStream instream = entity.getContent();
                                    String result = convertStreamToString(instream);


                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                    }
                    if (beaconDetails.getBeaconName().equals("blueberry")) {
                        Intent helpActivityIntent = new Intent(beacons.this, HelpActivity.class);
                        beacons.this.startActivity(helpActivityIntent);
                    }
                    if (beaconDetails.getBeaconName().equals("mint")) {
                        Intent mappyActivityIntent = new Intent(beacons.this, MapActivity.class);
                        beacons.this.startActivity(mappyActivityIntent);
                    }

                }


                     else {
                        text = "No beacons in range.";
                        backgroundColor = null;
                    }
                 ((TextView) findViewById(R.id.textView)).setText(text);
                findViewById(R.id.relativeLayout).setBackgroundColor(
                        backgroundColor != null ? backgroundColor : BACKGROUND_COLOR_NEUTRAL);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }
}