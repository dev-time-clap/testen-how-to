# JPA Entity Code Style Rule

## @MappedSuperclass
Use an abstract mapped super class when multiple entities exists that uses the same super fields.
An abstract mapped super class has the following lombok annotaions:
- @NoArgsConstructor
- @AllArgsConstructor
- @EqualsAndHashCode(onlyExplicitlyIncluded = true)
- @ToString
- @Getter
- @Setter
Annotate the class with @MappedSuperclass.
All fields in a mapped super class are protected.

Example of a mapped super class: AbstractEntity

## @Entity

### Entity class header conventions
Every JPA entity has the following lombok annotations:
- @Slf4j
- @NoArgsConstructor
- @EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
- @ToString(callSuper = true)
- @Getter
- @Setter

Every JPA entity has the following jakarta.persistence annotations:
- @Entity
- @Table(name = "<Name of the entity>")

The name of the table follows the convention: Name of the Entity class without the `Entity` postfix.
Every entity extends the AbstractEntity class.

### Entity column conventions
Organize the columns into two groups:
- First group: simple @Column fields
- Second group: referenced entities
- Sort the fields within each group alphabetically in descending order.

Use the following naming conventions for columns:
- Use UPPER_CASE notation with underscore for real table column name
- Use camelCase notation for the Java fields

### Conventions for referenced entities
- Add an inline comment on top of the annoations ('Primary link:' | 'Inverse link: ' <description>) The description explains the relation in a busines way.
- Always remove the generated lombok setter with @Setter(AccessLevel.NONE) annotation
- If the referenced entity is a collection, then remove the generated lombok getter with @Getter(AccessLevel.NONE) annotation.
- Always exclude collections from toString method by adding @ToString.Exclude annotation on the field
- If the referenced entity is a collection, then create a getter method that returns an inmutable instance of the collection. Use Collections.unmodifiableSet().

### Handling of bi-directional links
Add for each referened field a `set` method that sets the referenced entity in a bi-directional way. Take the following rules into account:
- Prevent an endless loop by comparing the current and the given object
- Remove the linking on the other side first if possible
- Apply the new reference
- Apply the reverse linking if possible

Add for each collection that holds reference to other entities a `add` method that adds a single referenced entity in a bi-directional way.  Take the following rules into account:
- Assert with `Objects.requireNonNull` that the given parameter is not null
- Prevent an endless loop by using `contains` on the collection and the given object
- Apply the new reference
- Apply the reverse linking if possible

Add for each collection that holds reference to other entities a `remove` method that removes a single referenced entity in a bi-directional way. Take the following rules into account:
- Assert with `Objects.requireNonNull` that the given parameter is not null
- Prevent an endless loop by using `contains` on the collection and the given object
- Remove the primary linking if possible
- Apply the reverse linking if possible

### Add Builder Support
Add a builder support to every JPA entity. Use Lombok's @Builder annotation for this purpose. Use the `@Builder(setterPrefix = "with", toBuilder = true)` annotation on constructor level for all fields of this class and all fields of all super classes.

Override the generated *Builder class from lombok and add:
- a generic type <B> on the Builder class
- implements the GenericBuilder interface
- a protected default constructor

Example of an entity class: BookEntity