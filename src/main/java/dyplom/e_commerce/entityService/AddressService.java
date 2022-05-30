package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Address;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.repositories.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> listAddressBook(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public Address getByAddressIdAndCustomerId(Integer addressId, Integer customerId) {
        return addressRepository.findByIdAndCustomerId(addressId, customerId);
    }

    public void delete(Integer addressId, Integer customerId) {
        addressRepository.deleteByIdAndCustomerId(addressId, customerId);
    }

    public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
        if (defaultAddressId > 0) {
            addressRepository.setDefaultAddress(defaultAddressId);
        }

        addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
    }

    public Address getDefaultAddress(Customer customer) {
        return addressRepository.findDefaultByCustomer(customer.getId());
    }
}
