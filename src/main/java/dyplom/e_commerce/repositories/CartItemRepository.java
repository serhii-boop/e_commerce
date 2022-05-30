package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.CartItem;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCustomer(Customer customer);

    CartItem findByCustomerAndProduct(Customer customer, Product product);
    @Modifying
    @Query("update CartItem c set c.quantity = ?1 where c.customer.id = ?2 and c.product.id = ?3")
    void updateQuantity(Integer quantity, Integer customerId, Integer productId);

    @Modifying
    @Query("delete from CartItem c where c.customer.id = ?1 and c.product.id = ?2")
    void deleteByCustomerIdAndProductId(Integer customerId, Integer productId);
}
