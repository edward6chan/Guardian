package com.edward6chan.www.guardian;

/**
 * Created by Apoorva on 08/11/14.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Service that receives ActivityRecognition updates. It receives
 * updates in the background, even if the main Activity is not visible.
 */
public class ActivityRecognitionIntentService extends IntentService {

    private final String TAG = "ActivityRecognitionIntentService";


    public ActivityRecognitionIntentService() {
        super("My Activity Recognition Service");


    }

    /**
     * Called when a new activity detection update is available.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent() hit.");

        // If the incoming intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {

            // Get the update
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);


            List<DetectedActivity> allActivities = result.getProbableActivities();

            for(DetectedActivity activity: allActivities){
                String name = getNameFromType(activity.getType());
                Log.i(TAG, "name:" + name + ", confidence: " + activity.getConfidence());
            }
            // Get the most probable activity
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = mostProbableActivity.getConfidence();
            /*
             * Get an integer describing the type of activity
             */
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);
            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */
        
            //Log.i(TAG, "Activity:" + activityName);

            //We create the intent to pass on to the receiver here
            //I think this is a default name hehe
            Intent i = new Intent("com.edward6chan.www.guardian.ACTIVITY_RECOGNITION_DATA");

            //Here is where we set the names for the keys to access corresponding values
            i.putExtra("Activity", activityName);

            i.putExtra("Confidence", confidence);

            sendBroadcast(i);

        } else {
            Log.i(TAG, "else");

             /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
        }
    }

    /**
     * Map detected activity types to strings
     *
     * @param activityType The detected activity type
     * @return A user-readable name for the type
     */
    private String getNameFromType(int activityType) {
        Log.i(TAG, "getNameFromType() hit. Activity Type: " + activityType);
        switch (activityType) {
//            case DetectedActivity.IN_VEHICLE:
//                return "in_vehicle";
//            case DetectedActivity.ON_BICYCLE:
//                return "on_bicycle";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.WALKING:
                return "walking";

        }
        return "unknown";
    }
}