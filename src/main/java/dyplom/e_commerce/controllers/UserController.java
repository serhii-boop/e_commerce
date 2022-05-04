package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.OrderProducts;
import dyplom.e_commerce.entityService.OrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final OrderProductService orderProductService;

    public UserController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping
    public ResponseEntity<List<OrderProducts>> getAll(){
        return ResponseEntity.ok(orderProductService.getAllByUserId(1));
    }
}
