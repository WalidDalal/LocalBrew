package com.project.localbrew.service;

import com.project.localbrew.entity.*;
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

    public VenueServiceImpl(VenueRepository venueRepository, CurrentUserService currentUserService) {
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public List<Venue> findAllActiveVenuesByCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City nullo");
        }

        return venueRepository.findAllByCityContainingIgnoreCaseAndStatus(city, VenueStatus.ACTIVE);
    }

    @Override
    public List<Venue> findAllActiveVenues() {
        return venueRepository.findAllByStatus(VenueStatus.ACTIVE);
    }

    @Override
    public List<Venue> findAllPendingVenues() {
        return venueRepository.findAllByStatus(VenueStatus.PENDING);
    }

    @Override
    public List<Venue> findAllSuspendedVenues() {
        return venueRepository.findAllByStatus(VenueStatus.SUSPENDED);
    }

    @Override
    public Venue findVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        return venueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));
    }

    @Override
    public List<Venue> findAllVenuesByCurrentOwner() {
        User owner = currentUserService.getCurrentUser();

        return venueRepository.findAllByOwnerId(owner.getId());
    }

    @Override
    public List<Venue> findAllActiveVenuesByType(List<VenueType> types) {
        if (types == null || types.isEmpty()) {
            throw new IllegalArgumentException("Types nullo");
        }
        return venueRepository.findAllByTypeInAndStatus(types, VenueStatus.ACTIVE);
    }

    @Override
    public List<Venue> findAllActiveVenuesByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name nullo");
        }

        return venueRepository.findAllByNameContainingIgnoreCaseAndStatus(name, VenueStatus.ACTIVE);
    }

    @Override
    public Venue saveVenue(Venue venue) {
        // NON deve avere ID nullo
        if (venue.getId() != null) {
            throw new IllegalArgumentException("Un nuovo venue non deve avere ID");
        }
        //prende user dal JWT
        User currentUser = currentUserService.getCurrentUser();

        // Solo OWNER o ADMIN può creare venue
        if (currentUser.getRole() != Role.OWNER) {
            throw new AccessDeniedException("Non puoi creare questo locale");
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
            throw new AccessDeniedException("Non puoi modificare questo locale");
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
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Non puoi modificare questo locale");
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
            throw new AccessDeniedException("Non puoi eliminare questo locale");
        }

        venueRepository.delete(venue);
    }
}