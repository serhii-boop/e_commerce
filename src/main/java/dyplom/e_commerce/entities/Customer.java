package dyplom.e_commerce.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Column(nullable = false, length = 64)
    private String addressLine1;
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    @Column(nullable = false, length = 45)
    private String country;
    @Column(nullable = false, length = 45)
    private String city;
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;
    @Column(name = "verification_code")
    private String verificationCode;
    private boolean enabled;
    @Column(name = "created_time")
    private Date createdTime;

    public Customer() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Transient
    public String getAddress() {
        String address = firstName;
        if (lastName != null && !lastName.isEmpty()) {
            address += " " + lastName;
        }
        if (!addressLine1.isEmpty()) address += ", " + addressLine1;
        if (addressLine2 != null && !addressLine2.isEmpty()) address += " " + addressLine2;
        if (!city.isEmpty()) address += ", " + city;
        address += ", " + country;
        if (!postalCode.isEmpty()) address += ". Postal code: " + postalCode;
        if (!phoneNumber.isEmpty()) address += ". Phone number: " + phoneNumber;
        return address;
    }
}
