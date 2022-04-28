package com.wh.demo;

import com.wh.demo.entity.Admin;
import com.wh.demo.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class U4Day07PhonebookApplicationTests {

    @Autowired
    AdminService adminService;

    @Test
    public void t1(){
        List<Admin> list = adminService.list();
        list.forEach(System.out::println);
    }

    @Test
    void contextLoads() {
    }

}
