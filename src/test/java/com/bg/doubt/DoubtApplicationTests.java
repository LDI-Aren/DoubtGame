package com.bg.doubt;

import com.bg.doubt.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@SpringBootTest
class DoubtApplicationTests {


    @Test
    void contextLoads() {
        UserDto userDto = new UserDto();
        userDto.setUserId("idid");
        userDto.setUserPassword("papa");
        userDto.setUsername("nana");


    }

}
