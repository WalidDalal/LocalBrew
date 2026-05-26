package com.project.localbrew.service;

import java.util.List;
import java.util.UUID;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;

public interface VenueService {
	// Crud
	Venue findActiveVenueById(UUID id);
	List<Venue> findAllVenues();
	Venue findVenueById(UUID id);
	Venue saveVenue(Venue venue);
	Venue updateVenueById(Venue venue, UUID id);
	void deleteVenueById(UUID id);
	List<Venue> findActiveVenues();
	Venue updateVenueStatus(UUID id, VenueStatus status);
	Venue activateVenue(UUID id);
	Venue suspendVenue(UUID id);
	List<Drink> findAllDrinksByVenueId(UUID venueId);
}
