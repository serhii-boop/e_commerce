package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewOrderWithSingleProduct() {
        Customer customer = entityManager.find(Customer.class, 7);
        Product product = entityManager.find(Product.class, 13);

        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        mainOrder.setProductCost(product.getCost());
        mainOrder.setSubtotal(product.getPrice());
        mainOrder.setTotal(product.getPrice() + 10);

        mainOrder.setStatus(OrderStatus.NEW);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(mainOrder);
        orderDetail.setProductCost(product.getCost());
        orderDetail.setQuantity(1);
        orderDetail.setSubtotal(product.getPrice());
        orderDetail.setUnitPrice(product.getPrice());

        mainOrder.getOrderDetails().add(orderDetail);

        Order savedOrder = repo.save(mainOrder);

        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewOrderWithMultipleProducts() {
        Customer customer = entityManager.find(Customer.class, 7);
        Product product1 = entityManager.find(Product.class, 20);
        Product product2 = entityManager.find(Product.class, 30);

        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProduct(product1);
        orderDetail1.setOrder(mainOrder);
        orderDetail1.setProductCost(product1.getCost());
        orderDetail1.setQuantity(1);
        orderDetail1.setSubtotal(product1.getPrice());
        orderDetail1.setUnitPrice(product1.getPrice());

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProduct(product2);
        orderDetail2.setOrder(mainOrder);
        orderDetail2.setProductCost(product2.getCost());
        orderDetail2.setQuantity(2);
        orderDetail2.setSubtotal(product2.getPrice() * 2);
        orderDetail2.setUnitPrice(product2.getPrice());

        mainOrder.getOrderDetails().add(orderDetail1);
        mainOrder.getOrderDetails().add(orderDetail2);

        mainOrder.setProductCost(product1.getCost() + product2.getCost());
        float subtotal = product1.getPrice() + product2.getPrice() * 2;
        mainOrder.setSubtotal(subtotal);
        mainOrder.setTotal(subtotal + 30);

        mainOrder.setStatus(OrderStatus.PAID);

        Order savedOrder = repo.save(mainOrder);
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void testListOrders() {
        Iterable<Order> orders = repo.findAll();

        orders.forEach(System.out::println);
    }

    @Test
    public void testUpdateOrder() {
        Integer orderId = 2;
        Order order = repo.findById(orderId).get();

        order.setStatus(OrderStatus.SHIPPING);
        order.setOrderTime(new Date());

        Order updatedOrder = repo.save(order);

        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
    }

    @Test
    public void testGetOrder() {
        Integer orderId = 3;
        Order order = repo.findById(orderId).get();

        assertThat(order).isNotNull();
        System.out.println(order);
    }

    @Test
    public void testDeleteOrder() {
        Integer orderId = 3;
        repo.deleteById(orderId);

        Optional<Order> result = repo.findById(orderId);
        assertThat(result).isNotPresent();
    }


//    @Test
//    public void testFindByOrderTimeBetween() throws ParseException {
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date startTime = dateFormatter.parse("2021-08-01");
//        Date endTime = dateFormatter.parse("2021-08-31");
//
//        List<Order> listOrders = repo.findByOrderTimeBetween(startTime, endTime);
//
//        assertThat(listOrders.size()).isGreaterThan(0);
//
//        for (Order order : listOrders) {
//            System.out.printf("%s | %s | %.2f | %.2f | %.2f \n",
//                    order.getId(), order.getOrderTime(), order.getProductCost(),
//                    order.getSubtotal(), order.getTotal());
//        }
//    }
}