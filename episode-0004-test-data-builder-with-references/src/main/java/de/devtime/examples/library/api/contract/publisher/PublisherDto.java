package de.devtime.examples.library.api.contract.publisher;

import java.util.UUID;

import de.devtime.examples.library.api.contract.AbstractPersistableDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PublisherDto extends AbstractPersistableDto {

  private String name;

  @Builder(setterPrefix = "with")
  private PublisherDto(
      final UUID id,
      final String name) {
    super(id);
    this.name = name;
  }
}
