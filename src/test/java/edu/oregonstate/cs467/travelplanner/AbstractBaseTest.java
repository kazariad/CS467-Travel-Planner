package edu.oregonstate.cs467.travelplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractBaseTest {
    @Autowired
    protected ObjectMapper objectMapper;
}
