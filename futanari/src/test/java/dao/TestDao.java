package dao;

import com.futanari.dao.UserTypeMapper;
import com.futanari.model.UserType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Totooria Hyperion on 2017/5/5.
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDao {

	@Autowired
	private static UserTypeMapper userTypeMapper;

	@BeforeClass
	public static void init() {//junit之前init spring
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
		userTypeMapper = (UserTypeMapper)context.getBean("userTypeMapper");
	}

	@Test
	public void testDao() {

		System.out.println(userTypeMapper);

		UserType userType = new UserType();
		userType.setUserTypeName("类型");
		userType.setEnabled(true);
		Integer count = userTypeMapper.insertOne(userType);
		System.out.println(count);
		UserType retUserType = userTypeMapper.selectByPrimaryKey(userType.getId());
		System.out.println(retUserType.getId() + ":" + retUserType.getUserTypeName());


	}

}
