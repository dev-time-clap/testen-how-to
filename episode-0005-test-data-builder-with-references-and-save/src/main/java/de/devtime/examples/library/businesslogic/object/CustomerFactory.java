package de.devtime.examples.library.businesslogic.object;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.persistence.entity.CustomerEntity;
import de.devtime.examples.library.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomerFactory {

  private final ApplicationContext appContext;
  private final CustomerRepository customerRepo;

  public Optional<Customer> findByCustomerId(final UUID customerId) {
    Optional<CustomerEntity> optEntity = this.customerRepo
        .findById(customerId);
    if (optEntity.isPresent()) {
      Customer customer = this.appContext.getBean(Customer.class);
      customer.init(optEntity.get());
      return Optional.of(customer);
    }
    return Optional.empty();
  }
}
