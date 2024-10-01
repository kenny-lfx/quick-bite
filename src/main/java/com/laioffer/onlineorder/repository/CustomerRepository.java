package com.laioffer.onlineorder.repository;



import com.laioffer.onlineorder.entity.CustomerEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Long> { // long就是id的类型，要保持一致

    List<CustomerEntity> findByFirstName(String firstName); //函数名字不能乱写，会根据这个自动实现

    List<CustomerEntity> findByLastName(String lastName);

    CustomerEntity findByEmail(String email);


    @Modifying
    @Query("UPDATE customers SET first_name = :firstName, last_name = :lastName WHERE email = :email")
    void updateNameByEmail(String email, String firstName, String lastName); // 这里就不重要了，因为操作已经给出
}

