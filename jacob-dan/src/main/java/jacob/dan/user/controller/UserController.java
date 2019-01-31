package jacob.dan.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jacob.dan.base.bean.JsonResult;
import jacob.dan.user.entity.User;
import jacob.dan.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@PutMapping
	@ResponseBody
	public JsonResult<User> save(User user) {
		userService.save(user);
		return new JsonResult<User>(user);
	}
	
	@GetMapping("/list")
	@ResponseBody
	public JsonResult<List<User>> list(User user) {
		List<User> users = userService.findAll(user);
		return new JsonResult<List<User>>(users);
	}
}
