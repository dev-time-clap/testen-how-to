package de.devtime.examples.library.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtime.examples.library.persistence.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {

  Optional<BookEntity> findByIsbn(String isbn);

}
