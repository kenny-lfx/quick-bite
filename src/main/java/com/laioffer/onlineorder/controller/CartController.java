package com.laioffer.onlineorder.controller;


import com.laioffer.onlineorder.entity.CustomerEntity;
import com.laioffer.onlineorder.model.AddToCartBody;
import com.laioffer.onlineorder.model.CartDto;
import com.laioffer.onlineorder.service.CartService;
import com.laioffer.onlineorder.service.CustomerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CartController {


    private final CartService cartService;
    private final CustomerService customerService;


    public CartController(
            CartService cartService,
            CustomerService customerService
    ) {
        this.cartService = cartService;
        this.customerService = customerService;
    }


    @GetMapping("/cart")
    public CartDto getCart(@AuthenticationPrincipal User user) {
        CustomerEntity customer = customerService.getCustomerByEmail(user.getUsername());


        return cartService.getCart(customer.id());
    }

    @PostMapping("/cart")
    public void addToCart(@AuthenticationPrincipal User user, @RequestBody AddToCartBody body) {
        CustomerEntity customer = customerService.getCustomerByEmail(user.getUsername());
        cartService.addMenuItemToCart(customer.id(), body.menuId());
    }


    @PostMapping("/cart/checkout")
    public void checkout(@AuthenticationPrincipal User user) {
        CustomerEntity customer = customerService.getCustomerByEmail(user.getUsername());
        cartService.clearCart(customer.id());
    }

    //为什么能识别？因为成功登陆后spring返回一个，sessionid，浏览器存在cookie里，然后浏览器再访问的时候会带上这个id，
}   // session base authentication，server会存这个信息，还有token base，server不知道现在多少人登陆了，穿的东西是数字签名，区别就是server不存session，所以没有logout方法。
