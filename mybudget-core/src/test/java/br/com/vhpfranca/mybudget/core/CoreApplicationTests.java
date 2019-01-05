package br.com.vhpfranca.mybudget.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.victorpfranca.mybudget.core.category.CategoryControllerTest;
import br.com.victorpfranca.mybudget.core.category.CategoryServiceImplTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CategoryServiceImplTest.class, CategoryControllerTest.class })
public class CoreApplicationTests {

	@Test
	public void contextLoads() {
	}

}
