package de.devtime.examples.library.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtime.examples.library.persistence.entity.BookAuthorEntity;

public interface BookAuthorRepository extends JpaRepository<BookAuthorEntity, UUID> {

}
