package com.project.localbrew.service;

import com.project.localbrew.entity.*;
import com.project.localbrew.repository.VenueDrinkRepository;
import com.project.localbrew.repository.VenueRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final CurrentUserService currentUserService;
    private final VenueDrinkRepository venueDrinkRepository;

    public VenueServiceImpl(VenueRepository venueRepository, CurrentUserService currentUserService, VenueDrinkRepository venueDrinkRepository) {
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
        this.venueDrinkRepository = venueDrinkRepository;
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<Venue> findAllVenues() {

        return venueRepository.findAll();
    }

    // =========================
    // GET ACTIVE
    // =========================

    public List<Venue> findActiveVenues() {

        return venueRepository.findByStatus(VenueStatus.ACTIVE);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public Venue findVenueById(UUID id) {

        validateId(id);

        return venueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));
    }

    @Override
    public List<Drink> findAllDrinksByVenueId(UUID venueId) {
        validateId(venueId);

        findVenueById(venueId);

        return venueDrinkRepository.findByVenueId(venueId).stream().map(VenueDrink::getDrink).toList();
    }

    // =========================
    // CREATE
    // =========================

    @Override
    public Venue saveVenue(Venue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("Venue nullo");
        }
        //non deve avere ID
        if (venue.getId() != null) {
            throw new IllegalArgumentException("Un nuovo venue non deve avere ID");
        }
        //prende user dal JWT
        User currentUser = currentUserService.getCurrentUser();

        if (currentUser.getRole() != Role.OWNER && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Solo OWNER o ADMIN possono creare venue");
        }
        //status automatico
        venue.setOwner(currentUser);
        venue.setStatus(VenueStatus.PENDING);

        return venueRepository.save(venue);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public Venue updateVenueById(Venue venue, UUID id) {

        validateId(id);

        if (venue == null) {

            throw new IllegalArgumentException("Venue nullo");
        }

        Venue existingVenue = findVenueById(id);

        User currentUser = currentUserService.getCurrentUser();

        // SOLO owner del locale o ADMIN
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        boolean isOwnerOfVenue = existingVenue.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfVenue) {

            throw new AccessDeniedException("Non puoi modificare questo locale");
        }

        // UPDATE FIELDS

        if (venue.getName() != null && !venue.getName().isBlank()) {

            existingVenue.setName(venue.getName());
        }

        if (venue.getDescription() != null) {

            existingVenue.setDescription(venue.getDescription());
        }

        if (venue.getCity() != null && !venue.getCity().isBlank()) {
            existingVenue.setCity(venue.getCity());
        }

        if (venue.getAddress() != null && !venue.getAddress().isBlank()) {

            existingVenue.setAddress(venue.getAddress());
        }


        if (venue.getLatitude() != null) {

            validateLatitude(venue.getLatitude());

            existingVenue.setLatitude(venue.getLatitude());
        }

        if (venue.getLongitude() != null) {

            validateLongitude(venue.getLongitude());

            existingVenue.setLongitude(venue.getLongitude());
        }

        if (venue.getType() != null) {

            existingVenue.setType(venue.getType());
        }

        return venueRepository.save(existingVenue);
    }

    // =========================
    // STATUS
    // =========================

    public Venue updateVenueStatus(UUID id, VenueStatus status) {

        validateId(id);

        if (status == null) {

            throw new IllegalArgumentException("Status venue obbligatorio");
        }

        User currentUser = currentUserService.getCurrentUser();

        // SOLO ADMIN
        if (!currentUser.getRole().name().equals("ADMIN")) {

            throw new AccessDeniedException("Solo ADMIN può modificare lo status");
        }

        Venue venue = findVenueById(id);

        venue.setStatus(status);

        return venueRepository.save(venue);
    }

    public Venue activateVenue(UUID id) {

        return updateVenueStatus(id, VenueStatus.ACTIVE);
    }

    public Venue suspendVenue(UUID id) {

        return updateVenueStatus(id, VenueStatus.SUSPENDED);
    }

    // =========================
    // DELETE
    // =========================

    @Override
    public void deleteVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        Venue venue = findVenueById(id);

        User currentUser = currentUserService.getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Solo ADMIN può eliminare un locale");
        }

        venueRepository.delete(venue);
    }

    // =========================
    // VALIDATIONS
    // =========================

    @Override
    public Venue findActiveVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        Venue venue = findVenueById(id);

        if (venue.getStatus() != VenueStatus.ACTIVE) {
            throw new EntityNotFoundException("Venue non trovato con ID: " + id);
        }

        return venue;
    }

    private void validateId(UUID id) {

        if (id == null) {

            throw new IllegalArgumentException("ID nullo");
        }
    }

    private void validateLatitude(Double latitude) {

        if (latitude < -90 || latitude > 90) {

            throw new IllegalArgumentException("Latitudine deve essere tra -90 e 90");
        }
    }

    private void validateLongitude(Double longitude) {

        if (longitude < -180 || longitude > 180) {

            throw new IllegalArgumentException("Longitudine deve essere tra -180 e 180");
        }
    }
}