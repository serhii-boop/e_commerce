package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.repositories.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    public final static int CUSTOMERS_PER_PAGE = 10;
    private final CustomerRepository customerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPas = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPas);
    }

    public boolean verify(String verifyCode) {
        Customer customer = customerRepository.findByVerificationCode(verifyCode);
        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepository.enable(customer.getId());
            return true;
        }
    }

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);
        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public void save(Customer customerInForm) {
        if (!customerInForm.getPassword().isEmpty()) {
            String encodePassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodePassword);
        } else {
            Customer customerInDb = customerRepository.findById(customerInForm.getId()).get();
            customerInForm.setPassword(customerInDb.getPassword());
        }
        customerRepository.save(customerInForm);
    }


    public void delete(Integer id) {
        Long count = customerRepository.countById(id);
        if (count == null || count == 0) {
            throw new RuntimeException("exception");
        }
        customerRepository.deleteById(id);
    }
}
