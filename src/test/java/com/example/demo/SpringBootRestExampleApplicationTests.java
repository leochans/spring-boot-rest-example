package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.controller.DemoController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRestExampleApplicationTests {

    @Autowired
    private DemoController demoController;

    @Test
    public void contextLoads() {
        assertThat(demoController).isNotNull();
    }

}
