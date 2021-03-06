package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import com.sun.org.apache.xpath.internal.SourceTree;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.providers.MapQuestProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;
import processing.core.PShape;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
	// Colors constants for coloring the markers
	public final int RED = color(255, 0, 0);
	public final int YELLOW = color(255, 255, 0);
	public final int BLUE = color(0, 0, 255);
	// Size constants for markers
	public static final int HUGE = 15;
	public static final int MODERATE = 10;
	public static final int LIGHT = 5;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.HybridProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
        if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    
	    //TODO: Add code here as appropriate
		// Create markers for every single earthquake in List of earthquakes
		// and add them to the appropriate array
		for (PointFeature earthquake : earthquakes) {
			markers.add(createMarker(earthquake));
		}

		// Add all markers from the List to the map
		map.addMarkers(markers);
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// Create marker based on earthquake location
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		// Color and set marker size based on earthquake magnitude
		float magnitude = (float) feature.getProperty("magnitude");
		if (magnitude > THRESHOLD_MODERATE) {
			marker.setColor(RED);
			marker.setRadius(HUGE);
		} else if (magnitude <= THRESHOLD_MODERATE && magnitude > THRESHOLD_LIGHT) {
			marker.setColor(YELLOW);
			marker.setRadius(MODERATE);
		} else {
			marker.setColor(BLUE);
			marker.setRadius(LIGHT);
		}
		return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}

	// helper method to drawMarker key in GUI
	// TODO: Implement this method to drawMarker the key
	private void addKey() {
		pushMatrix();
		translate(20, 100);

		fill(200);
		rect(0, 0, 140, 300); // legend background
		fill(0);
		textAlign(TOP, CENTER);
		text("Earthquake Key", 20, 20);  // legend title

		fill(RED);
		ellipse(20, 100, HUGE, HUGE);
		fill(YELLOW);
		ellipse(20, 150, MODERATE, MODERATE);
		fill(BLUE);
		ellipse(20, 200, LIGHT, LIGHT);
		fill(0);
		text("Magnitude >5", 30, 100);
		text("Magnitude >4", 30, 150);
		text("Magnitude <4", 30, 200);

		popMatrix();
	}
}
