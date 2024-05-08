package tp.msk.reactivemongo.services;

import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.model.BeerDTO;

public interface BeerService {
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
    Mono<BeerDTO> getBeerById(String beerId);
}
