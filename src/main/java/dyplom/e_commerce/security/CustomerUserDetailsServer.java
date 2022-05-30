package dyplom.e_commerce.security;

import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailsServer implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username);
        if (customer != null){
            return new CustomerUserDetails(customer);
        }

        throw new RuntimeException("user not found: " + username);
    }
}
