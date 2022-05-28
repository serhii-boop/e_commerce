package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByEmail(String email);

    Customer findByVerificationCode(String code);

    @Query(value = "select c from Customer c where c.email LIKE %?1% " +
            "or c.firstName LIKE %?1% or c.lastName LIKE %?1% " +
            "or c.city LIKE %?1% or c.country LIKE %?1% or c.postalCode LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);
    @Query(value = "update Customer c SET c.enabled = ?2 where c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    @Query("update  Customer c set c.verificationCode = null, c.enabled = true where c.id = ?1")
    @Modifying
    void enable(Integer id);
}
