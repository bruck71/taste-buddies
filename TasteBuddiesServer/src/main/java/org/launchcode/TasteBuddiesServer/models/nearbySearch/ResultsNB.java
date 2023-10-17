package org.launchcode.TasteBuddiesServer.models.nearbySearch;

import org.launchcode.TasteBuddiesServer.models.geocode.Geometry;

import java.util.List;
public class ResultsNB {
    private String place_id;
    private Geometry geometry;

    public ResultsNB(String place_id, Geometry geometry) {
        this.place_id = place_id;
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
