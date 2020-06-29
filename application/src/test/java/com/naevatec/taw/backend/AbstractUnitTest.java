package com.naevatec.taw.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;


//@RunWith(JUnitPlatform.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
public abstract class AbstractUnitTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
