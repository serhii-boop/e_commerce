package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Order;
import dyplom.e_commerce.entities.OrderStatus;
import dyplom.e_commerce.repositories.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
