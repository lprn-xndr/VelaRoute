package com.xndr.velaroute.specifications;

import com.xndr.velaroute.models.Shipment;
import org.springframework.data.jpa.domain.Specification;

public class ShipmentSpecifications {

    public static Specification<Shipment> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Shipment> hasDestination(String destination) {
        return (root, query, criteriaBuilder) ->
                destination == null ? null : criteriaBuilder.like(root.get("destination"),
                        "%" + destination + "%");
    }
}
