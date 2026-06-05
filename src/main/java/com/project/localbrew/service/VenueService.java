package com.project.localbrew.service;

import com.project.localbrew.dto.request.VenueRequest;
import com.project.localbrew.dto.response.VenueResponse;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;

import java.util.List;
import java.util.UUID;

public interface VenueService {

    List<VenueResponse> findAllVenues();

    List<VenueResponse> findAllActiveVenues();

    List<VenueResponse> findAllPendingVenues();

    List<VenueResponse> findAllSuspendedVenues();

    List<VenueResponse> findAllActiveVenuesByCity(String city);

    List<VenueResponse> findAllActiveVenuesByType(List<VenueType> types);

    List<VenueResponse> findAllActiveVenuesByName(String name);

    VenueResponse findVenueById(UUID id);

    VenueResponse findActiveVenueById(UUID id);

    List<VenueResponse> findAllVenuesByCurrentOwner();
  
    VenueResponse saveVenue(VenueRequest request);

    VenueResponse updateVenueById(VenueRequest request, UUID id);

    VenueResponse updateVenueStatus(UUID id, VenueStatus status);

    VenueResponse activateVenue(UUID id);

    VenueResponse suspendVenue(UUID id);

    void deleteVenueById(UUID id);
}

