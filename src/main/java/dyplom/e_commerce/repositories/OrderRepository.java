package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
            + " CONCAT(o.firstName, ' ', o.lastName) LIKE %?1% OR"
            + " o.firstName LIKE %?1% OR"
            + " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
            + " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
            + " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
            + " o.customer.firstName LIKE %?1% OR"
            + " o.customer.lastName LIKE %?1%")
    Page<Order> findAll(String keyword, Pageable pageable);

    @Query("select o from Order o join o.orderDetails od join od.product p " +
            "where o.customer.id = ?2 " +
            "and (p.name LIKE %?1% or o.status like %?1%)")
    Page<Order> findAllByCustomerId(String keyword, Integer customerId, Pageable pageable);
    @Query("select o from Order o where o.customer.id = ?1")
    Page<Order> findAllByCustomerId(Integer customerId, Pageable pageable);

    Long countById(Integer id);

    @Query("select new dyplom.e_commerce.entities.Order(o.id, o.orderTime, o.productCost, o.subtotal, o.total)" +
            " from Order o where o.orderTime between ?1 and ?2 order by o.orderTime asc ")
    List<Order> findByOrderTimeBetween(Date startTime, Date endTime);

}
