package dyplom.e_commerce.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "order_products")
public class OrderProducts {
    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    private int quantity;
}
