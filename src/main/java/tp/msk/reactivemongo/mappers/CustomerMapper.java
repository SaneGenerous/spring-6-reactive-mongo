package tp.msk.reactivemongo.mappers;

import org.mapstruct.Mapper;
import tp.msk.reactivemongo.domain.Customer;
import tp.msk.reactivemongo.model.CustomerDTO;

@Mapper
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
