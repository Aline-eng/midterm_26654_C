package com.farmco.farmco_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmco.farmco_connect.model.ELocationType;
import com.farmco.farmco_connect.model.Location;
import com.farmco.farmco_connect.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLocation(@RequestBody Location location, @RequestParam(required = false) String parentId) {
        String response = locationService.saveLocation(location, parentId);
        if (response.equals("Location saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();

        if (locations.isEmpty()) {
            return new ResponseEntity<>("No locations found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping(value = "/provinces", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProvinces() {
        List<Location> provinces = locationService.getProvinces();
        if (provinces.isEmpty()) {
            return new ResponseEntity<>("No provinces found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }

    @GetMapping(value = "/children", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getChildren(@RequestParam String parentId, @RequestParam ELocationType type) {
        List<Location> children = locationService.getChildren(parentId, type);
        if (children.isEmpty()) {
            return new ResponseEntity<>("No child locations found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    @GetMapping(value = "/districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDistrictsByProvince(@RequestParam String provinceId) {
        List<Location> districts = locationService.getChildren(provinceId, ELocationType.DISTRICT);
        if (districts.isEmpty()) {
            return new ResponseEntity<>("No districts found for this province", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(districts, HttpStatus.OK);
    }

    @GetMapping(value = "/sectors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSectorsByDistrict(@RequestParam String districtId) {
        List<Location> sectors = locationService.getChildren(districtId, ELocationType.SECTOR);
        if (sectors.isEmpty()) {
            return new ResponseEntity<>("No sectors found for this district", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    @GetMapping(value = "/cells", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCellsBySector(@RequestParam String sectorId) {
        List<Location> cells = locationService.getChildren(sectorId, ELocationType.CELL);
        if (cells.isEmpty()) {
            return new ResponseEntity<>("No cells found for this sector", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cells, HttpStatus.OK);
    }

    @GetMapping(value = "/villages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVillagesByCell(@RequestParam String cellId) {
        List<Location> villages = locationService.getChildren(cellId, ELocationType.VILLAGE);
        if (villages.isEmpty()) {
            return new ResponseEntity<>("No villages found for this cell", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(villages, HttpStatus.OK);
    }
}
