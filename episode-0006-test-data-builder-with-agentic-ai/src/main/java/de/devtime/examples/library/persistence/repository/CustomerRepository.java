package de.devtime.examples.library.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtime.examples.library.persistence.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

}
