package uz.pdp.config.sercurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.annotation.SessionScope;
import uz.pdp.domain.User;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(register -> register
                        .requestMatchers("/",
                                "/home/**",
                                "/auth/**",
                                "/static/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .formLogin(config -> config
                        .loginPage("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(authenticationFailureHandler))
                .rememberMe(config -> config
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-me")
                        .tokenValiditySeconds(3600 * 24)
                        .key("secret_key:CMAOcnaomxoaMXOAMdad12d2XxaAxaXAxxasaMOAMDaSMXOAxoMxoamx120mx")
                        .alwaysRemember(false))
                .logout(config -> config
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST")));
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SessionScope
    public UserContext userContext() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            if (principal instanceof User user) {
                return UserContext.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .build();
            }
        }
        return null;
    }

}
