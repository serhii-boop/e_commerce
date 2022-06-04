package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.*;
import dyplom.e_commerce.entities.checkout.CheckoutInfo;
import dyplom.e_commerce.repositories.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE = 10;

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            return orderRepository.findAll(keyword, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public Order getById(Integer id) {
        return orderRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void deleteById(Integer id)  {

        orderRepository.deleteById(id);
    }

    public void save(Order orderInForm) {
        Order orderInDB = orderRepository.findById(orderInForm.getId()).get();
        orderInForm.setOrderTime(orderInDB.getOrderTime());
        orderInForm.setCustomer(orderInDB.getCustomer());

        orderRepository.save(orderInForm);
    }

    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    public Order createOrder(Customer customer, Address  address, List<CartItem> cartItems, CheckoutInfo checkoutInfo) {
        Order newOrder = new Order();
        newOrder.setOrderTime(new Date());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(customer);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setTotal(checkoutInfo.getProductTotal());

        if (address == null) {
            newOrder.copyAddressFromCustomer();
        } else {
            newOrder.copyShippingAddress(address);
        }
        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem cartItem: cartItems) {
            Product product = cartItem.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setProductCost(product.getCost() + cartItem.getQuantity());
            orderDetail.setSubtotal(cartItem.getSubTotal());

            orderDetails.add(orderDetail);
        }
        return orderRepository.save(newOrder);
    }

    public Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            return orderRepository.findAllByCustomerId(keyword, customer.getId(), pageable);
        }
        return orderRepository.findAllByCustomerId(customer.getId(), pageable);
    }

//    public void updateStatus(Integer orderId, String status) {
//        Order orderInDB = orderRepository.findById(orderId).get();
//        OrderStatus statusToUpdate = OrderStatus.valueOf(status);
//
//        if (!orderInDB.hasStatus(statusToUpdate)) {
//            List<OrderTrack> orderTracks = orderInDB.getOrderTracks();
//
//            OrderTrack track = new OrderTrack();
//            track.setOrder(orderInDB);
//            track.setStatus(statusToUpdate);
//            track.setUpdatedTime(new Date());
//            track.setNotes(statusToUpdate.defaultDescription());
//
//            orderTracks.add(track);
//
//            orderInDB.setStatus(statusToUpdate);
//
//            orderRepo.save(orderInDB);
//        }
//
//    }
}
