package tp.msk.reactivemongo.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.model.BeerDTO;

public interface BeerService {
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
    Mono<BeerDTO> getBeerById(String beerId);
    Mono<BeerDTO> updateBeer(BeerDTO beerDTO, String beerId);
    Mono<BeerDTO> patchBeer(BeerDTO beerDTO, String beerId);
    Flux<BeerDTO> listBeers();
    Mono<Void> deleteBeerById(String beerId);
}
