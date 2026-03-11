package com.farmco.farmco_connect.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmco.farmco_connect.model.ELocationType;
import com.farmco.farmco_connect.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByCodeIgnoreCase(String code);

    Location findByTypeAndCodeIgnoreCase(ELocationType type, String code);

    Location findByTypeAndNameIgnoreCase(ELocationType type, String name);

    List<Location> findByType(ELocationType type);

    List<Location> findByParentIdAndType(UUID parentId, ELocationType type);
}
