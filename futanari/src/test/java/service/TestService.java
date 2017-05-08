package service;

import com.futanari.service.UsersServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by TotooriaHyperion on 2017/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:applicationContext.xml"
})
public class TestService {

	private static final Logger logger = LoggerFactory.getLogger(TestService.class);

	@Autowired
	private static UsersServiceImpl usersService;

	@BeforeClass
	public static void init() {//junit之前init spring
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
		usersService = (UsersServiceImpl)context.getBean("usersServiceImpl");
	}

	@Test
	public void testService() {

		logger.info(usersService.toString());
		usersService.testAutowired();

	}

}
