package account.config;

import account.exHandle.RestAuthenticationEntryPoint;
import account.user.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

// Extending the adapter and adding the annotation
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;
    private final UserService userService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public WebSecurityConfig(PasswordEncoder encoder,
                             UserService userService,
                             RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.encoder = encoder;
        this.userService = userService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    // To configure what authentication should do in Spring Security,
    // we can use a special builder — AuthenticationManagerBuilder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(encoder);

        // storing users in memory
//        auth.inMemoryAuthentication()
//                .withUser("Lacquered")
//                .password(encoder.encode("1245Lacq"))
//                .roles("ADMIN")
//                .passwordEncoder(encoder);
    }

    // To specify which authentication methods are allowed (form-based, HTTP basic)
    // and how they are configured
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/signup", "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
