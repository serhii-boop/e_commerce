package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, Integer> {

    @Query(value = "select op.id, op.order_id, op.product_id, op.quantity from order_products op " +
            "inner join orders o on o.id = op.order_id where o.user_id = ?1", nativeQuery = true)
    List<OrderProducts> findByUserId(int id);
}
