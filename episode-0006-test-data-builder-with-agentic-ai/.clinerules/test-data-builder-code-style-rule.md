# TestDataBuilder Code Style Rules

## Common rules
- A `TestDataBuilder` belongs to exactly one class to build (e.g. entity, dto, etc.)
- Use the following naming pattern for the class name:
  `<entity/dto name>TestDataBuilder`
  Example: BookEntityTestDataBuilder
- Use two dedicated sections in the class.

  First section:
  
  `// --------------------< Add referenced builder here >--------------------`
  
  Second section:
  
  `// ----------------------< Internal builder logic >-----------------------`

## Builder class header conventions
- Add a generic type <B> to the builder class that extends from the TestDataBuilder interface that uses the class type to build.
- Extend the builder class from the builder of the class to build.
- The builder class must implement either `TestDataBuilder` interface if a not saveable object is build in this builder class or it must implement the `TestDataBuilderWithSaveSupport` interface if the object to build is an entity class.

## Referenced provider section conventions
- Add a referenced test data provider for each referenced entity in the first section
- Add a list of referenced test data provider for each collection of referenced entities in the first section
- Use the following naming pattern for the referenced test data provider fields:
  `<class name to build without 'Entity/Dto/etc.' postfix>TestDataProvider`
  Example: bookTestDataProvider
- Add a `with` method that has a `Consumer<*TestDataProvider>` parameter to be able to configure directly the values of the referenced provider instance. Use a `RecursionGuard` if the referenced provider is a single instance (not a list declaration).
  Example:
  
```
  public B withCustomer(final Consumer<CustomerEntityTestDataProvider> consumer) {
    RecursionGuard.guard(AdditionalBookDataEntityTestDataProvider.class, () -> {
      this.customerTestDataProvider = this.customerTestDataProvider == null
          ? CustomerEntityTestDataProvider.create()
          : this.customerTestDataProvider;
      consumer.accept(this.customerTestDataProvider);
    });
    return and();
  }
```

- 
- 