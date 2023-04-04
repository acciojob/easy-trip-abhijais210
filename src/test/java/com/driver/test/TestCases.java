package com.driver.test;

import com.driver.EaseMyTrip;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = EaseMyTrip.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {


}

