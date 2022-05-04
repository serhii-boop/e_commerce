package dyplom.e_commerce.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "user_address")
public class Address {
    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "address_line1", length = 55)
    private String addressLine1;
    @Column(name = "address_line2", length = 55)
    private String addressLine2;
    @Column(name = "city", length = 55)
    private String city;
    @Column(name = "street", length = 55)
    private String street;
    @Column(name = "zip", length = 12)
    private String zipCode;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
}
