package com.laioffer.airbnb.service;

import com.laioffer.airbnb.entity.Stay;
import com.laioffer.airbnb.repository.LocationRepository;
import com.laioffer.airbnb.repository.StayAvailabilityRepository;
import com.laioffer.airbnb.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private LocationRepository locationRepository;
    private StayAvailabilityRepository stayAvailabilityRepository;
    private StayRepository stayRepository;

    @Autowired
    public SearchService(LocationRepository locationRepository, StayAvailabilityRepository stayAvailabilityRepository, StayRepository stayRepository) {
        this.locationRepository = locationRepository;
        this.stayAvailabilityRepository = stayAvailabilityRepository;
        this.stayRepository = stayRepository;
    }

    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {
        //parameters change to just String category
        //Location location = geoEncodingService.getLatLong(1), NGO.getAddress()); -->gets NGO lat lon
        //      we could then GeoPoint curAddress = location.getGeoPoint save this to GeoPoint
        //      THEN we can use curAddress.getLat(), curAddress.getLong() to get our current lat lon
        //User service to get NGO and then NGO.getRadius --> this is distance
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        //wont need the code
        long duration = Duration.between(checkinDate.atStartOfDay(), checkoutDate.atStartOfDay()).toDays();
        List<Long> filteredStayIds = stayAvailabilityRepository.findByDateBetweenAndStateIsAvailable(stayIds, checkinDate, checkoutDate.minusDays(1), duration);
        //then we use similar and follow Spring word convention findByIdInAndCategoryEqual(itemIds, category);
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);

    }
}
