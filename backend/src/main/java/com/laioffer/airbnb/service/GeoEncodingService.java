package com.laioffer.airbnb.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.laioffer.airbnb.entity.Location;
import com.laioffer.airbnb.exception.GeoEncodingException;
import com.laioffer.airbnb.exception.InvalidStayAddressException;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoEncodingService {
    @Value("${geocoding.apikey}")
    private String apikey;

    public Location getLatLong(Long id, String stayAddress) { //0
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apikey).build();
        try {
            GeocodingResult result = GeocodingApi.geocode(context, stayAddress).await()[0];
            if (result.partialMatch) {
                throw new InvalidStayAddressException("Failed to find stay address");
            }
            return new Location(id, new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
            throw new GeoEncodingException("Failed to encode stay address");
        }
    }
}

