package tp.msk.reactivemongo.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tp.msk.reactivemongo.domain.Beer;
import tp.msk.reactivemongo.repositories.BeerRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll()
                .doOnSuccess(success -> {
                    loadBeerData();
                })
                .subscribe();

    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("Pale Ale")
                        .upc("12244")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(133)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();
                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("Pale Ale")
                        .upc("14562")
                        .price(new BigDecimal("9.99"))
                        .quantityOnHand(220)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();
                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("124567")
                        .price(new BigDecimal("14.99"))
                        .quantityOnHand(120)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
