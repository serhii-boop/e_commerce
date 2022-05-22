package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.OrderProducts;
import dyplom.e_commerce.repositories.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCartService {

    private final OrderProductRepository orderProductRepository;

    public ShopCartService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public List<OrderProducts> listCartItem(int id) {
        return orderProductRepository.findByUserId(id);
    }
}
