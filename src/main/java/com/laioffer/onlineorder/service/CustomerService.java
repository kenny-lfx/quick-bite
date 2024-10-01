package com.laioffer.onlineorder.service;


import com.laioffer.onlineorder.entity.CartEntity;
import com.laioffer.onlineorder.entity.CustomerEntity;
import com.laioffer.onlineorder.repository.CartRepository;
import com.laioffer.onlineorder.repository.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {


    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;


    public CustomerService(
            CartRepository cartRepository,
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            UserDetailsManager userDetailsManager) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }


    @Transactional // 能回滚，并且保证全部都成功执行才会实行更改，不然就回滚改动
    public void signUp(String email, String password, String firstName, String lastName) {
        email = email.toLowerCase();
        UserDetails user = User.builder() // 方便构建的一个内置方法，替代了new，因为new的话需要了解顺序代表的位置，比不一定熟悉，builder能随便选位置，参数多适合
                .username(email)          // 不是所有class都有build pattern，需要单独写才能用
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        customerRepository.updateNameByEmail(email, firstName, lastName); // creatUser不管的这里再更新


        CustomerEntity savedCustomer = customerRepository.findByEmail(email);
        CartEntity cart = new CartEntity(null, savedCustomer.id(), 0.0);
        cartRepository.save(cart);
    }


    public CustomerEntity getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
