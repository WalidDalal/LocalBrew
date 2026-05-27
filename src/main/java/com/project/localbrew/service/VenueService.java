package com.project.localbrew.service;

import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;

import java.util.List;
import java.util.UUID;

public interface VenueService {
    List<Venue> findAllVenues();

    List<Venue> findAllActiveVenues();

    List<Venue> findAllPendingVenues();

    List<Venue> findAllSuspendedVenues();

    List<Venue> findAllActiveVenuesByCity(String city);

    List<Venue> findAllActiveVenuesByType(List<VenueType> types);

    List<Venue> findAllActiveVenuesByName(String name);

    Venue findVenueById(UUID id);

    List<Venue> findAllVenuesByCurrentOwner();

    Venue saveVenue(Venue venue);

    Venue updateVenueById(Venue venue, UUID id);

    void deleteVenueById(UUID id);

    Venue updateVenueStatus(UUID id, VenueStatus status);

    Venue activateVenue(UUID id);

    Venue suspendVenue(UUID id);
}
