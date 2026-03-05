package com.farmco.farmco_connect.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmco.farmco_connect.model.ELocationType;
import com.farmco.farmco_connect.model.Location;
import com.farmco.farmco_connect.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public String saveLocation(Location location, String parentId) {
        if (location.getType() == null) {
            return "Location type is required";
        }

        if (locationRepository.existsByCodeIgnoreCase(location.getCode())) {
            return "Location code already exists";
        }

        if (location.getType() == ELocationType.PROVINCE) {
            if (parentId != null && !parentId.isBlank()) {
                return "Province cannot have a parent";
            }
            location.setParent(null);
            locationRepository.save(location);
            return "Location saved successfully";
        }

        if (parentId == null || parentId.isBlank()) {
            return "Parent location is required for this type";
        }

        UUID parsedParentId;
        try {
            parsedParentId = UUID.fromString(parentId);
        } catch (IllegalArgumentException e) {
            return "Invalid parent id";
        }

        Location parent = locationRepository.findById(parsedParentId).orElse(null);
        if (parent == null) {
            return "Parent location not found";
        }

        if (!isValidParentChild(parent.getType(), location.getType())) {
            return "Invalid hierarchy: " + location.getType() + " cannot be under " + parent.getType();
        }

        location.setParent(parent);
        locationRepository.save(location);
        return "Location saved successfully";
    }

    public List<Location> getAllLocations() {
        List<Location> locations = locationRepository.findAll();

        return locations;
    }

    public List<Location> getProvinces() {
        return locationRepository.findByType(ELocationType.PROVINCE);
    }

    public List<Location> getChildren(String parentId, ELocationType childType) {
        UUID parsedParentId;
        try {
            parsedParentId = UUID.fromString(parentId);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        return locationRepository.findByParentIdAndType(parsedParentId, childType);
    }

    private boolean isValidParentChild(ELocationType parentType, ELocationType childType) {
        return (parentType == ELocationType.PROVINCE && childType == ELocationType.DISTRICT)
                || (parentType == ELocationType.DISTRICT && childType == ELocationType.SECTOR)
                || (parentType == ELocationType.SECTOR && childType == ELocationType.CELL)
                || (parentType == ELocationType.CELL && childType == ELocationType.VILLAGE);
    }
}
