package dyplom.e_commerce.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "order_status", length = 55)
    private String orderStatus;
    @Column(name = "order_price")
    private Double orderPrice;
    @Column(name = "order_address_line1", length = 55)
    private String orderAddressLine1;
    @Column(name = "order_address_line2", length = 55)
    private String orderAddressLine2;
    @Column(name = "order_city", length = 55)
    private String city;
    @Column(name = "order_street", length = 100)
    private String orderStreet;
    @Column(name = "zip", length = 12)
    private String orderZip;
}
