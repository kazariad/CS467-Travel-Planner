package edu.oregonstate.cs467.travelplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Clock;

@SpringBootTest
public abstract class AbstractBaseTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @MockitoSpyBean(reset = MockReset.AFTER)
    protected Clock clock;
}
