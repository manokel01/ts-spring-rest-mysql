package tinysensormanager.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class provides the configuration for Spring Security.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider authProvider;

    /**
     * Constructor for SecurityConfig class.
     * Injects the custom authentication provider to the SecurityConfig
     * @param customAuthenticationProvider the custom authentication provider for the application
     */
    @Autowired
    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.authProvider = customAuthenticationProvider;
    }

    /**
     * Configures the authentication manager to use the custom authentication provider.
     * @param auth the authentication manager builder
     * @throws Exception if there is an error configuring the authentication manager
     */
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    /**
     * Configures the security filter chain for the application.
     * @param http the HttpSecurity object
     * @return the security filter chain
     * @throws Exception if there is an error configuring the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                // Sets the authorization rules for the URL endpoints
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("api/users/**").authenticated()
                // .authorizeRequests().antMatchers("api/users/**").permitAll()
                // Configures form login settings, including the login page and default success URL
                .anyRequest().authenticated().and().formLogin()
                .loginPage("/login").defaultSuccessUrl("/api/users?lastname=").permitAll()
                // Configures HTTP basic authentication
                .and().httpBasic()
                .and()
                // Configures logout settings, including the logout URL and success URL
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

        return http.build();
                // .authorizeRequests().antMatchers(HttpMethod.POST, "/api/users").permitAll()
                // .and()
                // .authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").permitAll();
    }

    /**
     * Configures the WebSecurityCustomizer to ignore certain paths.
     * @return the WebSecurityCustomizer object
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/styles/**", "/img/**", "/js/**");
    }

    /**
     * Configures the authentication manager bean.
     * @param authenticationConfiguration the authentication configuration
     * @return the authentication manager
     * @throws Exception if there is an error configuring the authentication manager
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
