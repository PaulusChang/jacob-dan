package jacob.dan.user.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jacob.dan.base.bean.JsonResult;
import jacob.dan.base.bean.util.DateUtils;
import jacob.dan.base.bean.util.MathUtils;
import jacob.dan.base.bean.util.DateUtils.Field;
import jacob.dan.base.constant.YesNo;
import jacob.dan.user.entity.Role;
import jacob.dan.user.entity.User;
import jacob.dan.user.entity.UserRole;
import jacob.dan.user.service.RoleService;
import jacob.dan.user.service.UserRoleService;
import jacob.dan.user.service.UserService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRoleService userRoleService;
	
	@GetMapping("/generatorUser")
	@ResponseBody
	public JsonResult<User> generatorUser() {
		User user;
		String name;
		Integer age;
		Float weight;
		Date birthday;
		String hobby;
		for (int i = 0; i < 100; i++) {
			name = "user_name_" + i;
			age = MathUtils.rand(20, 65);
			weight = (float) (MathUtils.rand(400, 800) / 10.0);
			birthday = DateUtils.add(DateUtils.getDate("2018-08-08"), Field.DAY, -1 * MathUtils.rand(1000, 10000));
			hobby = "hobby_" + MathUtils.rand(0, 10);
			user = new User(name, age, weight, birthday, hobby);
			userService.save(user);
		}
		return new JsonResult<>(YesNo.YES, "测试user插入完成");
	}
	
	@GetMapping("/generatorRole")
	@ResponseBody
	public JsonResult<Role> generatorRole() {
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
		return new JsonResult<>(YesNo.YES, "测试role插入完成");
	}
	
	
	@GetMapping("/generatorUserRole")
	@ResponseBody
	public JsonResult<Role> generatorUserRole() {
		List<User> users = userService.findAll();
		List<Role> roles = roleService.findAll();
		UserRole userRole;
		for (User user : users) {
			userRole = new UserRole(user, roles.get(MathUtils.rand(0, roles.size() - 1)));
			userRoleService.save(userRole);
		}
		return new JsonResult<>(YesNo.YES, "测试userRole插入完成");
	}

}
