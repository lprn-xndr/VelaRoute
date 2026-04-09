package com.xndr.velaroute.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;

@Entity // This tells Java: "Create a table in Postgres for this class"
@Table(name = "shipments")
@Data // Lombok: Auto creates Getters/Setters
@NoArgsConstructor // Lombok: Creates a blank constructor
@AllArgsConstructor // Lombok: Creates a constructor with all fields
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Tracking number is required")
    @Size(min = 5, max = 20, message = "Tracking number must be between 5 and 20 chracters")
    @Column(nullable = false, unique = true)
    private String trackingNumber;

    private String origin;
    private String destination;
    private String status; // PENDING, IN TRANSIT, DELIVERED
}
