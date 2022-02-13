package com.biraj.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EShopApplicationTests {

    @Test
    void contextLoads() {
    // no impl needed for this class
    }

    // Test Method added ONLY to cover main() invocation not covered by application tests.
    @Test
    public void main() {
        EShopApplication.main(new String[]{});
    }


}
