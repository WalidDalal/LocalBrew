package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;

import java.util.List;
import java.util.UUID;

public interface VenueService {
    List<Venue> findAllVenues();

    List<Venue> findAllVenuesByCity(String city);

    List<Venue> findAllVenuesByName(String name);

    List<Venue> findAllVenuesByType(VenueType type);

    List<Venue> findAllActiveVenuesByName(String name);

    List<Drink> findAllDrinksByVenueId(UUID venueId);

    List<Venue> findAllActiveVenues();

    Venue findVenueById(UUID id);

    Venue saveVenue(Venue venue);

    Venue updateVenueById(Venue venue, UUID id);

    void deleteVenueById(UUID id);

    Venue updateVenueStatus(UUID id, VenueStatus status);

    Venue activateVenue(UUID id);

    Venue suspendVenue(UUID id);
}
