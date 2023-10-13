package tr.com.htr.intern.userservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
@SpringBootTest
class UserServiceApplicationTests {

    @BeforeEach
    void setup(){
        System.out.println(" inside before each ");
    }
    @Test
    public void test(){
        System.out.println(" inside test ");
    }

    @AfterEach
    void afterEach(){
        System.out.println(" inside after each ");
    }

}
