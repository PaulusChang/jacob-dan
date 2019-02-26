package jacob.dan.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jacob.dan.SpringbootApplication;
import jacob.dan.base.bean.util.DateUtils;
import jacob.dan.base.bean.util.MathUtils;
import jacob.dan.base.bean.util.DateUtils.Field;
import jacob.dan.user.entity.Role;
import jacob.dan.user.entity.User;
import jacob.dan.user.entity.UserRole;
import jacob.dan.user.service.RoleService;
import jacob.dan.user.service.UserRoleService;
import jacob.dan.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenerateDataTest {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;

	/**
	 * 执行此方法，生成测试数据 generateUserRole() 要最后调用
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateTestData() {
		generateUser();
		generateRole();
		generateUserRole();
	}
	
	/**
	 * 生成测试的user数据
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateUser() {
		User user;
		String name;
		Integer age;
		Float weight;
		Date birthday;
		String hobby;
		String[] bobbys = new String[] {"sport", "music", "painting", "reading", null};
		for (int i = 0; i < 100; i++) {
			name = "user_name_" + i;
			age = MathUtils.rand(20, 65);
			weight = (float) (MathUtils.rand(400, 800) / 10.0);
			birthday = DateUtils.add(DateUtils.getDate("2018-08-08"), Field.DAY, -1 * MathUtils.rand(1000, 10000));
			hobby = bobbys[MathUtils.rand(0, bobbys.length)];
			user = new User(name, age, weight, birthday, hobby);
			userService.save(user);
		}
	}

	/**
	 * 生成测试的role数据
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateRole() {
		Role role;
		String name;
		String value;
		String description;
		for (int i = 0; i < 5; i++) {
			name = "name" + i;
			value = "value" + i;
			description = "description" + i;
			role = new Role(name, value, description);
			roleService.save(role);
		}
	}
	
	/**
	 * 生成userRole关系数据
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateUserRole() {
		List<User> users = userService.findAll();
		List<Role> roles = roleService.findAll();
		UserRole userRole;
		for (User user : users) {
			userRole = new UserRole(user, roles.get(MathUtils.rand(0, roles.size() - 1)));
			userRoleService.save(userRole);
		}
	}

}
