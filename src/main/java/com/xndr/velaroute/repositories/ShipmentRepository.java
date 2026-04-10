package com.xndr.velaroute.repositories;

import com.xndr.velaroute.models.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {
    // Custom query: Find a shipment by its tracking number
        Optional<Shipment> findByTrackingNumber(String trackingNumber);
    }

