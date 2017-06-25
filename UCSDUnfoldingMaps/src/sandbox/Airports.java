package sandbox;

import demos.Airport;

/**
 * Created by Aleksey on 19.06.2017.
 */
public class Airports {
    public static void main(String[] args) {

    }

    public static String findAirportCode(String toFind, Airport[] airports) {
        for (Airport airport : airports) {
            if (airport.getCity().equals(toFind)) {
                return airport.getCode3();
            }
        }
        return null;
    }

    public static String airportCodeBinarySearch(String toFind, Airport[] sortedAirports) {
        int low = 0;
        int high = sortedAirports.length - 1;
        int mid = high / 2;

        while (low <= high) {
            int comparingResult = toFind.compareTo(sortedAirports[mid].getCity());
            if (comparingResult > 0) {
                low = mid + 1;
                mid = (low + high) / 2;
            }
            else if (comparingResult < 0) {
                high = mid - 1;
                mid = (low + high) / 2;
            }
            else {
                return sortedAirports[mid].getCode3();
            }
        }
        return null;
    }
}
