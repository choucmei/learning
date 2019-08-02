package chouc.java.spring.userdefinedannotation.test;

import chouc.java.spring.userdefinedannotation.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring2.xml")
@Component
public class MyServer3 {
	@Autowired
	HelloService helloService;

	@Test
	public void helloTest1() {
		System.out.println("开始junit测试……");
		String hello = helloService.hello("ooooooo");
		System.out.println(hello);
	}

}
