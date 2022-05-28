package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Country;
import dyplom.e_commerce.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findByCountryOrderByNameAsc(Country country);
}
