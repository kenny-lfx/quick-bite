package com.laioffer.onlineorder;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.sql.DataSource;


@Configuration
public class AppConfig {


    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setCreateUserSql("INSERT INTO customers (email, password, enabled) VALUES (?,?,?)");
        userDetailsManager.setCreateAuthoritySql("INSERT INTO authorities (email, authority) values (?,?)");
        userDetailsManager.setUsersByUsernameQuery("SELECT email, password, enabled FROM customers WHERE email = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT email, authorities FROM authorities WHERE email = ?");
        return userDetailsManager;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // disable，因为要不然端口不规定就不接受
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 一些内设的常见路径，一般是前段resource
                                .requestMatchers(HttpMethod.GET, "/", "/index.html", "/*.json", "/*.png", "/static/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/logout", "/signup").permitAll()
                                .requestMatchers(HttpMethod.GET, "/restaurants/**", "/restaurant/**").permitAll()
                                .anyRequest().authenticated() // 除了上面的都要authorize才能访问
                )
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .formLogin()
                // 能够登陆，formLogin
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value())) // 成功了怎么办，可以跳转
                .failureHandler(new SimpleUrlAuthenticationFailureHandler()) // 失败了怎么办，可以跳转
                .and()
                .logout()
                // 能够退出，
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
        return http.build();// 前面都是builder类型的用法
    }
}

