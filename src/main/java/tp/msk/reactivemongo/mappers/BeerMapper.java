package tp.msk.reactivemongo.mappers;

import org.mapstruct.Mapper;
import tp.msk.reactivemongo.domain.Beer;
import tp.msk.reactivemongo.model.BeerDTO;

@Mapper
public interface BeerMapper {
    Beer beerDTOToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDTO(Beer beer);
}
