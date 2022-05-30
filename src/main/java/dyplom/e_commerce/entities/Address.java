package dyplom.e_commerce.entities;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "phone_name", nullable = false, length = 15)
    private String phoneNumber;
    @Column(name = "address_line_1", nullable = false, length = 64)
    private String addressLine1;
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    @Column(name = "country", nullable = false, length = 45)
    private String country;
    @Column(name = "city", nullable = false, length = 45)
    private String city;
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "default_address")
    private boolean defaultForShipping;

    public Address() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isDefaultForShipping() {
        return defaultForShipping;
    }

    public void setDefaultForShipping(boolean defaultForShipping) {
        this.defaultForShipping = defaultForShipping;
    }

    @Override
    public String toString() {
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
