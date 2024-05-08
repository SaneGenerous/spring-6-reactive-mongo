package tp.msk.reactivemongo.services;

import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.model.BeerDTO;

public class BeerServiceImpl implements BeerService {
    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return null;
    }

    @Override
    public Mono<BeerDTO> getBeerById(String beerId) {
        return null;
    }
}
