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

    @Override
    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public List<Venue> findAllVenuesByCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City nullo");
        }

        return venueRepository.findAllByCity(city);
    }

    public List<Venue> findAllActiveVenues() {
        return venueRepository.findAllByStatus(VenueStatus.ACTIVE);
    }

    @Override
    public Venue findVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        return venueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));
    }

    @Override
    public List<Drink> findAllDrinksByVenueId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        return venueDrinkRepository.findByVenueId(id).stream().map(VenueDrink::getDrink).toList();
    }

    @Override
    public List<Venue> findAllVenuesByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name nullo");
        }

        return venueRepository.findAllByName(name);
    }

    @Override
    public List<Venue> findAllVenuesByType(VenueType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type nullo");
        }
        return venueRepository.findAllByType(type);
    }

    @Override
    public Venue saveVenue(Venue venue) {
        // NON deve avere ID nullo
        if (venue.getId() != null) {
            throw new IllegalArgumentException("Un nuovo venue non deve avere ID");
        }
        //prende user dal JWT
        User currentUser = currentUserService.getCurrentUser();

        // Solo OWNER può creare venue
        if (currentUser.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Solo gli OWNER possono creare venue");
        }
        //status automatico
        venue.setOwner(currentUser);
        venue.setStatus(VenueStatus.PENDING);

        return venueRepository.save(venue);
    }

    @Override
    public Venue updateVenueById(Venue venue, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        if (venue == null) {
            throw new IllegalArgumentException("Venue nullo");
        }

        Venue existingVenue = findVenueById(id);
        User currentUser = currentUserService.getCurrentUser();

        // SOLO owner del locale o ADMIN
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwnerOfVenue = existingVenue.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfVenue) {
            throw new IllegalArgumentException("Non puoi modificare questo locale");
        }

        // UPDATE FIELDS

        if (venue.getName() != null && !venue.getName().isBlank()) {
            existingVenue.setName(venue.getName());
        }

        if (venue.getDescription() != null) {
            existingVenue.setDescription(venue.getDescription());
        }

        if (venue.getCity() != null) {
            existingVenue.setCity(venue.getCity());
        }

        if (venue.getAddress() != null && !venue.getAddress().isBlank()) {
            existingVenue.setAddress(venue.getAddress());
        }


        if (venue.getLatitude() != null) {
            existingVenue.setLatitude(venue.getLatitude());
        }

        if (venue.getLongitude() != null) {

            existingVenue.setLongitude(venue.getLongitude());
        }

        if (venue.getType() != null) {
            existingVenue.setType(venue.getType());
        }

        return venueRepository.save(existingVenue);
    }

    public Venue updateVenueStatus(UUID id, VenueStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status venue obbligatorio");
        }

        User currentUser = currentUserService.getCurrentUser();

        // SOLO ADMIN
        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("Solo ADMIN può modificare lo status");
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

    @Override
    public void deleteVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        Venue venue = findVenueById(id);

        User currentUser = currentUserService.getCurrentUser();

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        boolean isOwnerOfVenue = venue.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfVenue) {
            throw new IllegalArgumentException("Non puoi eliminare questo locale");
        }

        venueRepository.delete(venue);
    }
}