package dyplom.e_commerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomerWebSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder1() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/app", "/app/**", "/search", "/search/**").permitAll()
                .antMatchers("/app/account-details", "/app/cart", "/app/address", "/app/address/**").authenticated()
                .antMatchers("/admin-page").hasAnyAuthority("Admin", "Editor")
                .antMatchers("/admin-page/**").hasAnyAuthority("Admin", "Editor")
                .antMatchers("/app/customer").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/app/login")
                .permitAll()
                .defaultSuccessUrl("/app")
                .and()
                .logout().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/richtext/**", "/font/**", "/images/**", "./category/**", "./product/**", "/webjars/**");
    }

    @Bean
    public UserDetailsService customerDetailsService() {
        return new CustomerUserDetailsServer();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider1());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider1() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder1());
        return authProvider;
    }
}
