package edu.oregonstate.cass.iot.iottestbench;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.AboutObjectDescription;
import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.Variant;

import java.util.Map;

public class Main extends AppCompatActivity {
    private static final String TAG = "testapp"; //Tag for messages logged by this class

    private ListView aboutList;
    public ArrayAdapter<AboutInfo> aboutListAdapter;

    public ListView detailList;
    public ArrayAdapter<String> detailListAdapter;

    public static Activity activity;
    public static String PACKAGE_NAME; //Unique bus name



    //--------------//Builtins//-------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.fragment) != null){

            if (savedInstanceState != null){
                return;
            }
            //create new fragment
            AboutListFragment announcements = new AboutListFragment();

            //pass potential extras from intent to the fragment
            announcements.setArguments(getIntent().getExtras());

            //add fragment to the fragment container
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, announcements).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //--------------//Bus Object Instances and Constants//-------------------//
    /**
     * The bus attachment is the object that provides AllJoyn services to Java
     * clients.  Pretty much all communiation with AllJoyn is going to go through
     * this obejct.
     */
    private BusAttachment mBus  = new BusAttachment(PACKAGE_NAME, BusAttachment.RemoteMessage.Receive);
    /**
     * Instance of the custom About Listener class
     */
    private TestAboutListener mTestListener = new TestAboutListener();



    //--------------//Custom Bus Objects//-------------------//
    /**
     * AboutListener that adds the announced 'busName' to the global list of About announcement
     * names via the MainActivity addFoundAbout() method. It also creates a new AboutProxy
     * object that references the found About interface and adds it to a list of AboutProxy
     * objects so we can get the contents of the About Announcement later when the user
     * selects that particular announcement.
     */
    public class TestAboutListener implements AboutListener {
        public void announced(final String busName, int version, short sessionPort, AboutObjectDescription[] aboutObjectDescriptions, final Map<String, Variant> aboutData) {
            Log.i(TAG, "AboutListener(): Found About Interface: "+busName);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addFoundAbout(busName, aboutData);
                }
            });
        }
    }



    //--------------//Data Handling//-------------------//
    /**
     * Call to update the UI element that contains the names of all About
     * objects discovered so far.
     */
    public void addFoundAbout(final String busName, final Map<String,Variant> aboutData){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AboutInfo tempInterface = new AboutInfo(busName, aboutData);
                aboutListAdapter.add(tempInterface);
            }
        });
    }
    /**
     * Call to remove names from the UI element that contains all About
     * object names discovered so far.
     */
    public void removeFoundAbout(final String name, final Map<String,Variant> aboutData, final String busName){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * Todo: iterate through each item in the aboutListAdapter to find the one with "busName"
                 * and remove the item with a matching busName.
                 */
            }
        });
    }
    /**
     * Class to contain the information for displaying about interface info.
     * Contains name to be displayed in UI, map of about info, and name reported by
     * listener (so listener can remove that entry once interface disappears
     * from the network).
     */
    class AboutInfo {
        public final Map<String,Variant> aboutData;
        public final String busName;

        private AboutInfo(String busName, Map<String,Variant> aboutData){
            this.aboutData = aboutData;
            this.busName = busName;
        }

        @Override
        public String toString(){  //This is the method that is called to get the string to show in the listview
            return this.busName;
        }
    }



    //--------------//Misc.//-------------------//
    /**
     * Manually load in the NDK library so we can use the methods defined inside.
     */
    static {
        Log.i(TAG, "System.loadLibrary(\"alljoyn_java\")");
        System.loadLibrary("alljoyn_java");
    }
}
