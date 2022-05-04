package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.OrderProducts;
import dyplom.e_commerce.repositories.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public List<OrderProducts> getAllByUserId(int id) {
        return orderProductRepository.findByUserId(id);
    }
}
