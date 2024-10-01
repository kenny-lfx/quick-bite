package com.laioffer.onlineorder.controller;


import com.laioffer.onlineorder.model.RegisterBody;
import com.laioffer.onlineorder.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomerController {


    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/signup") // 有signup， login和logout都自动生成了
    @ResponseStatus(value = HttpStatus.CREATED) // 200 OK, 一般默认会带合适的response，这里改了一下，201 CREATED
    public void signUp(@RequestBody RegisterBody body) {
        customerService.signUp(body.email(), body.password(), body.firstName(), body.lastName());
    }
}
