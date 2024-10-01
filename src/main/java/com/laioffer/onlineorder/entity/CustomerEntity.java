package com.laioffer.onlineorder.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("customers") // 这是对应的哪一个table
public record CustomerEntity(
        @Id Long id, // 标记primary Key，不能缺失
        String email,
        String password,
        boolean enabled,
        String firstName,
        String lastName
) {
}
