package tp.msk.reactivemongo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.domain.Beer;
import tp.msk.reactivemongo.mappers.BeerMapper;
import tp.msk.reactivemongo.model.BeerDTO;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {
    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(getTestBeer());
    }

    @Test
    void testSaveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDto ->{
                System.out.println(savedDto.getId());
                atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .upc("123123")
                .price(BigDecimal.TEN)
                .quantityOnHand(23)
                .build();
    }
}