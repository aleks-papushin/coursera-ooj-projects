package sandbox;

import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksey on 13.05.2017.
 */
public class LifeExpectancySbx extends PApplet {
    UnfoldingMap map;
    Map<String, Float> lifeExpByCountry;
    List<Feature> countries;
    List<Marker> countryMarkers;

    public void setup() {
        size(800, 600, OPENGL);
        map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);

        lifeExpByCountry = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");

        countries = GeoJSONReader.loadData(this, "countries.geo.json");
        countryMarkers = MapUtils.createSimpleMarkers(countries);

        map.addMarkers(countryMarkers);
        shadeCountries();
    }

    public void draw() {
        map.draw();
    }

    private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
        Map<String, Float> lifeExpMap = new HashMap<String, Float>();
        String[] rows = loadStrings(fileName);

        for (String row : rows) {
            String[] columns = row.split(",");
            if (columns.length == 6 && !columns[5].equals("..")) {
                lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
           }
        }
        return lifeExpMap;
    }

    private void shadeCountries() {
        for (Marker marker : countryMarkers) {
            String countryId = marker.getId();

            if (lifeExpByCountry.containsKey(countryId)) {
                float lifeExp = lifeExpByCountry.get(countryId);
                int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
                marker.setColor(color(255-colorLevel, 100, colorLevel));
            }
            else {
                marker.setColor(color(150));
            }
        }
    }
}