package de.devtime.examples.library.persistence.entity;

import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.fasterxml.uuid.Generators;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Setter

@MappedSuperclass
public abstract class AbstractEntity<E extends AbstractEntity<?>> implements Persistable<UUID> {

  @Id
  @Column(name = "ID", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  @Setter(value = AccessLevel.PACKAGE)
  private UUID id;

  @Version
  private int version;

  @Transient
  private boolean persisted;

  @PostPersist
  @PostLoad
  private void setPersisted() {
    this.persisted = true;
  }

  @Override
  public boolean isNew() {
    return !this.persisted;
  }

  @SuppressWarnings("unchecked")
  public E generateId() {
    setId(Generators.timeBasedEpochRandomGenerator().generate());
    return (E) this;
  }
}
