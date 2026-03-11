package com.farmco.farmco_connect.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.farmco.farmco_connect.model.ELocationType;
import com.farmco.farmco_connect.model.Farmer;
import com.farmco.farmco_connect.model.Location;
import com.farmco.farmco_connect.model.User;
import com.farmco.farmco_connect.repository.FarmerRepository;
import com.farmco.farmco_connect.repository.LocationRepository;
import com.farmco.farmco_connect.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public String saveUser(User user, String locationId) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already exists";
        }

        Location village = resolveVillage(locationId);
        if (village == null) {
            return "Village not found (use village id, code, or name)";
        }

        user.setLocation(village);
        userRepository.save(user);
        return "User saved successfully";
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            return "Invalid username or password";
        }

        return "Login successful";
    }

    public List<User> getUsersByProvince(String provinceValue) {
        return userRepository.findByAnyLocationLevelCodeOrName(provinceValue);
    }

    public Page<User> getUsersPaginatedAndSorted(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return userRepository.findAll(pageable);
    }

    public String assignFarmerToUser(String userId, String farmerId) {
        UUID parsedUserId;
        UUID parsedFarmerId;
        try {
            parsedUserId = UUID.fromString(userId);
            parsedFarmerId = UUID.fromString(farmerId);
        } catch (IllegalArgumentException e) {
            return "Invalid user id or farmer id";
        }

        User user = userRepository.findById(parsedUserId).orElse(null);
        if (user == null) {
            return "User not found";
        }

        Farmer farmer = farmerRepository.findById(parsedFarmerId).orElse(null);
        if (farmer == null) {
            return "Farmer not found";
        }

        user.getFarmers().add(farmer);
        userRepository.save(user);
        return "Farmer assigned to user successfully";
    }

    private Location resolveVillage(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        Location byCode = locationRepository.findByTypeAndCodeIgnoreCase(ELocationType.VILLAGE, input);
        if (byCode != null) {
            return byCode;
        }

        Location byName = locationRepository.findByTypeAndNameIgnoreCase(ELocationType.VILLAGE, input);
        if (byName != null) {
            return byName;
        }

        try {
            UUID id = UUID.fromString(input);
            Location byId = locationRepository.findById(id).orElse(null);
            if (byId != null && byId.getType() == ELocationType.VILLAGE) {
                return byId;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        return null;
    }
}
