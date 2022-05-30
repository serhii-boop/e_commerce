package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Address;
import dyplom.e_commerce.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByCustomer(Customer customer);

    @Query("select a from Address a where a.id = ?1 and a.customer.id = ?2")
    Address findByIdAndCustomerId(Integer addressId, Integer customerId);
    @Query("delete from Address a where a.id = ?1 and a.customer.id = ?2")
    @Modifying
    void deleteByIdAndCustomerId(Integer addressId, Integer customerId);

    @Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
    @Modifying
    void setDefaultAddress(Integer id);

    @Query("UPDATE Address a SET a.defaultForShipping = false "
            + "WHERE a.id <> ?1 AND a.customer.id = ?2")
    @Modifying
    void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
    Address findDefaultByCustomer(Integer customerId);
}
