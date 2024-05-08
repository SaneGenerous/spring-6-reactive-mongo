package tp.msk.reactivemongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import tp.msk.reactivemongo.domain.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
