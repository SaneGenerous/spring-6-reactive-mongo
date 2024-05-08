package tp.msk.reactivemongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import tp.msk.reactivemongo.domain.Beer;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
