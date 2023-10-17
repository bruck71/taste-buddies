package org.launchcode.TasteBuddiesServer.services;

import com.google.gson.Gson;
import org.launchcode.TasteBuddiesServer.models.geocode.Location;
import org.launchcode.TasteBuddiesServer.models.nearbySearch.TranscriptNB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextSearchService {

    @Value("${apiKey}")
    private String APIKey;

    private final NearbyService nearbyService;

//    Text Search URL
    String URLTS = "https://maps.googleapis.com/maps/api/place/textsearch/json?type=restaurant";

    public TextSearchService(NearbyService nearbyService) {
        this.nearbyService = nearbyService;
    }

    public List<String> getNearbyIDsFromLocationAndSearchRadius (
            String location,
            String searchRadius
    ) throws URISyntaxException, IOException, InterruptedException
    {
        TranscriptNB transcriptNB;  // NB stands for Nearby Search - Might need to change this later for Text Search

        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getRequestTS = HttpRequest.newBuilder()
                .uri(new URI(
                        URLTS +
                                "&query=" + location +
                                "&radius=" + searchRadius +
                                "&key=" + APIKey))
                .build();
        System.out.println("Request URI" + getRequestTS.uri());
        HttpResponse<String> getResponseTS = httpClient.send(getRequestTS, HttpResponse.BodyHandlers.ofString());
        transcriptNB = gson.fromJson(getResponseTS.body(), TranscriptNB.class);

        List<String> placeIDs = new ArrayList<>();
        String pageToken = transcriptNB.getNext_page_token();

//        Loop through Json Response body and add the place_id's into the placeIDs array
        for (int i=0; i < transcriptNB.getResults().size(); i++){
            String place_id = transcriptNB.getResults().get(i).getPlace_id();
            placeIDs.add(place_id);
        }
//        If placeIDs size is = 1 then try searching from the lat and long from the response body and use the nearbyService to find more IDs
        int numberRestaurants = placeIDs.size();

        if (numberRestaurants == 1) {
//           Extract Lat and Lng from the JSON Response
            String latitude = transcriptNB.getResults().get(0).getGeometry().getLocation().getLat();
            String longitude = transcriptNB.getResults().get(0).getGeometry().getLocation().getLng();
            Location nearbyLocation = new Location(latitude, longitude);
//           Call NearbyService
            List<String> nearbyPlaceIDs = nearbyService.getNearbyIDsFromLocationAndSearchRadius(nearbyLocation, searchRadius);

            return nearbyPlaceIDs;
        }

//        TODO: Add logic to load next page of restaurants if available once users reach end of list and hit load more restaurants button


        return placeIDs;
    }


}
