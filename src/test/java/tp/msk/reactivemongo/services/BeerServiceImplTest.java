package tp.msk.reactivemongo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.domain.Beer;
import tp.msk.reactivemongo.mappers.BeerMapper;
import tp.msk.reactivemongo.mappers.BeerMapperImpl;
import tp.msk.reactivemongo.model.BeerDTO;
import tp.msk.reactivemongo.repositories.BeerRepository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class BeerServiceImplTest {
    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerRepository beerRepository;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(getTestBeer());
    }

    @Test
    void findFirstByBeerNameTest() {
        BeerDTO beerDTO = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> foundDto = beerService.findFirstByBeerName(beerDTO.getBeerName());

        foundDto.subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void findByBeerStyleTest() {
        BeerDTO beerDTO1 = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerService.findByBeerStyle(beerDTO1.getBeerStyle())
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Save Test Beer Using Subscriber")
    void saveBeerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        await().untilTrue(atomicBoolean);

        BeerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
        BeerDTO savedBeer = beerService.saveBeer(Mono.just(getTestBeerDto())).block();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";  //use final so cannot mutate
        BeerDTO savedBeerDto = getSavedBeerDto();
        savedBeerDto.setBeerName(newName);

        BeerDTO updatedDto = beerService.saveBeer(Mono.just(savedBeerDto)).block();

        //verify exists in db
        BeerDTO fetchedDto = beerService.getBeerById(updatedDto.getId()).block();
        assertThat(fetchedDto.getBeerName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Beer Using Reactive Streams")
    void testUpdateStreaming() {
        final  String newName = "New Beer Name";
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        beerService.saveBeer(Mono.just(getTestBeerDto()))
                .map(savedBeerDto -> {
                    savedBeerDto.setBeerName(newName);
                    return savedBeerDto;
                })
                .flatMap(beerService::saveBeer)
                .flatMap(savedUpdatedDto -> beerService.getBeerById(savedUpdatedDto.getId()))
                .subscribe(atomicDto::set);

        await().until(() -> atomicDto.get() != null);
        assertThat(atomicDto.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getTestBeerDto();
        beerService.deleteBeerById(beerToDelete.getId()).block();
        BeerDTO emptyBeer = beerService.getBeerById(beerToDelete.getId()).block();
        assertThat(emptyBeer).isNull();
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

    public BeerDTO getSavedBeerDto() {
        return beerService.saveBeer(Mono.just(getTestBeerDto())).block();
    }

    public static BeerDTO getTestBeerDto() {
        return new BeerMapperImpl().beerToBeerDTO(getTestBeer());
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .id("555")
                .beerName("Space Dust")
                .beerStyle("IPA")
                .upc("123123")
                .price(BigDecimal.TEN)
                .quantityOnHand(23)
                .build();
    }
}