package com.example.Amigoscode.Spring.Security.Security;

import com.example.Amigoscode.Spring.Security.student.Student;
import com.sun.source.tree.ReturnTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.print.DocFlavor;

import java.util.concurrent.TimeUnit;

import static com.example.Amigoscode.Spring.Security.Security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.Amigoscode.Spring.Security.Security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//allow us to us method authorization
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
 //               .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
 //               .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*")
                    .permitAll()
                .antMatchers("/api/**")
                    .hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses",true)
                    .passwordParameter("password") //same as default
                    .usernameParameter("username") //same as default
                .and()
                .rememberMe() //defaults to 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingverysecured")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //if u enable crf remove this line. see logout doc
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaUser = User.builder()
                .username("Anna")
                .password(passwordEncoder.encode("password"))
                //.roles(STUDENT.name()) //ROLE_STUDENT
                .authorities(STUDENT.grantedAuthorities())
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                //.roles(ADMIN.name())
                .authorities(ADMIN.grantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                //.roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.grantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                annaUser,
                lindaUser,
                tomUser
        );
    }
}
