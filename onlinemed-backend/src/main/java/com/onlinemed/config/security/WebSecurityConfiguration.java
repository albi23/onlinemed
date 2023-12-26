package com.onlinemed.config.security;

import com.onlinemed.servises.impl.login.PasetoAuthProvider;
import com.onlinemed.servises.impl.login.JwtTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private String corsAllowedHerokuUrl;

    @Autowired
    private PasetoAuthProvider pasetoAuthProvider;

    @Autowired
    private PasetoAuthenticationEntryPoint pasetoAuthenticationEntryPoint;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${security.cors}")
    public void setSharedSecurityKey(final String corsUrl) {
        this.corsAllowedHerokuUrl = corsUrl;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(pasetoAuthProvider);
    }

    /**
     * All routing paths have permitAll() by default, the are narrowed down
     * by @Secured() annotation over the class
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().csrf().disable()
                /* Add custom authorisation filter */
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/language/**").permitAll()
                .antMatchers("/api/static-translation/**").permitAll()
                .antMatchers("/api/person/**").permitAll()
                .antMatchers("/api/roles/**").permitAll()
                .antMatchers("/api/drug-equivalent/**").permitAll()
                .antMatchers("/api/community/**").permitAll()
                .antMatchers("/api/doctor-info/**").permitAll()
                .antMatchers("/api/email/**").permitAll()
                .antMatchers("/api/forum-category/**").permitAll()
                .antMatchers("/api/forum-topic/**").permitAll()
                .antMatchers("/api/forum-post/**").permitAll()
                .antMatchers("/api/registration-link/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(pasetoAuthenticationEntryPoint).and()
                .sessionManagement().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(this.corsAllowedHerokuUrl));
        configuration.setAllowedMethods(Arrays.stream(RequestMethod.values()).map(Enum::toString).collect(Collectors.toList()));
        return new UrlBasedCorsConfigurationSource() {{
            registerCorsConfiguration("/**", configuration);
        }};
    }


}
