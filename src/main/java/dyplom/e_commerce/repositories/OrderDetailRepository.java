package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query("select new dyplom.e_commerce.entities.OrderDetail(od.product.category.name, od.quantity, od.productCost, od.subtotal) " +
            "from OrderDetail od where od.order.orderTime between ?1 and ?2")
    List<OrderDetail> findByCategoryAndTimeBetween(Date startTime, Date endTime);

    @Query("select new dyplom.e_commerce.entities.OrderDetail(od.quantity, od.product.name,  od.productCost, od.subtotal) " +
            "from OrderDetail od where od.order.orderTime between ?1 and ?2")
    List<OrderDetail> findByProductAndTimeBetween(Date startTime, Date endTime);
}
