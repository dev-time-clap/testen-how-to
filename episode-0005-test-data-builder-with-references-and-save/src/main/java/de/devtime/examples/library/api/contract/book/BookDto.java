package de.devtime.examples.library.api.contract.book;

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
public class BookDto extends AbstractPersistableDto {

  private String isbn;
  private String title;
  private UUID authorId;
  private UUID publisherId;

  @Builder(setterPrefix = "with")
  private BookDto(
      final UUID id,
      final String isbn,
      final String title,
      final UUID authorId,
      final UUID publisherId) {
    super(id);
    this.isbn = isbn;
    this.title = title;
    this.authorId = authorId;
    this.publisherId = publisherId;
  }
}
