package tp.msk.reactivemongo.web_fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tp.msk.reactivemongo.model.BeerDTO;
import tp.msk.reactivemongo.services.BeerService;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO){
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
        validator.validate(beerDTO, errors);

        if (errors.hasErrors()){
            throw  new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
        return beerService.getBeerById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerDTO -> beerService
                        .patchBeer(beerDTO, request.pathVariable("beerId")))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerDTO -> beerService
                        .updateBeer(beerDTO, request.pathVariable("beerId")))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {
        return beerService.saveBeer(request.bodyToMono(BeerDTO.class).doOnNext(this::validate))
                .flatMap(beerDto -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDto.getId()))
                        .build());
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse.ok()
                .body(beerService.getBeerById(request.pathVariable("beerId"))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        BeerDTO.class);
    }

    public Mono<ServerResponse> listBeers(ServerRequest response) {
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
