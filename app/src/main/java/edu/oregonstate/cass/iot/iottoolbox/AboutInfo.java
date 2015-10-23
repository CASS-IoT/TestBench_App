package edu.oregonstate.cass.iot.iottoolbox;

import org.alljoyn.bus.Variant;

import java.util.Map;

/**
 * Created by iot on 10/23/15.
 */

/**
 * Class to contain the information for displaying about interface info.
 * Contains name to be displayed in UI, map of about info, and name reported by
 * listener (so listener can remove that entry once interface disappears
 * from the network).
 */
public class AboutInfo {
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
