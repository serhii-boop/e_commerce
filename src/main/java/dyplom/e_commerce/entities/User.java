package dyplom.e_commerce.entities;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "password",  nullable = false, length = 64)
    private String password;
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Column(name = "email_verified")
    private boolean emailVerified;
    @Column(name = "registration_date", length = 50)
    private LocalDateTime registrationDate;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

}
