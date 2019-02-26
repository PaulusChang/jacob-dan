# jacob-dan

simple springboot+jpa

* 要求实体类继承jacob.dan.base.beanBaseEntity，根据《阿里巴巴Java开发手册》，

>三、MySQL规约
【强制】表必备三字段：id, gmt\_create, gmt\_modified。 说明：其中id必为主键，类型为unsigned bigint、单表时自增、步长为1。gmt\_create, gmt\_modified的类型均为date\_time类型。

BaseEntity中id为String类型，由SnowflakeIdFactory产生，除了上述字段，还增加了is\_deleted字段作为删除标识


1. 创建实体类，并调用方法生成相应的 service/repository 代码，以 jacob.dan.user.entity.UserRole 为例

	在 jacob.dan.user.entity 包下添加 UserRole.java

		package jacob.dan.user.entity;
		
		import javax.persistence.Entity;
		import javax.persistence.ManyToOne;
		import javax.persistence.Table;
		
		import jacob.dan.base.bean.BaseEntity;
		
		@Entity
		@Table(name = "dan_user_row")
		public class UserRole extends BaseEntity {
		
			@ManyToOne
			private User user;
			@ManyToOne
			private Role role;
			
			public User getUser() {
				return user;
			}
			public void setUser(User user) {
				this.user = user;
			}
			public Role getRole() {
				return role;
			}
			public void setRole(Role role) {
				this.role = role;
			}
			
		}

	代码生成工具 jacob.dan.generator.code.Generator.main(String[]) 可以生成指定包、类、目标类型的代码文件，参见对象中的方法

		generate(Category, Class<?>)
		generate(CategoryList, Class<?>)
		generate(Category, Package)
		generate(CategoryList, Package)

2. 测试封装好的查询方法
	
	测试示例中注入的 UserService 是自动生成的，继承了 BaseService。
		
		
		import static org.hamcrest.CoreMatchers.equalTo;
		import static org.junit.Assert.assertThat;
		
		import java.util.Arrays;
		import java.util.List;
		
		import org.hamcrest.Matchers;
		import org.junit.Assert;
		import org.junit.Test;
		import org.junit.runner.RunWith;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.data.domain.Page;
		import org.springframework.data.domain.Sort.Direction;
		import org.springframework.test.context.junit4.SpringRunner;
		
		import jacob.dan.SpringbootApplication;
		import jacob.dan.base.bean.Constraint.Null;
		import jacob.dan.base.bean.OrderBean;
		import jacob.dan.base.bean.PageRequestBean;
		import jacob.dan.base.bean.util.DateUtils;
		import jacob.dan.base.bean.util.DateUtils.Format;
		import jacob.dan.user.entity.User;
		import jacob.dan.user.entity.UserRole;
		import jacob.dan.user.service.UserRoleService;
		import jacob.dan.user.service.UserService;
		
		@RunWith(SpringRunner.class)
		@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
		public class QueryTest {
			
			@Autowired
			private UserService userService;
			@Autowired
			private UserRoleService userRoleService;
		
			/**
			 * 测试排序
			 * @author ChangJian
			 * @date 2019年2月26日
			 */
			@Test
			public void testSort() {
				User user = new User();
				//birthday desc
				user.setOrderBean(new OrderBean(Direction.DESC, "birthday"));
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (int i = 1; i < users.size(); i++) {
					assertThat(users.get(i - 1).getBirthday(), Matchers.greaterThanOrEqualTo(users.get(i).getBirthday()));
				}
			}
		
			/**
			 * 测试普通的约束
			 * @author ChangJian
			 * @date 2019年2月25日
			 */
			@Test
			public void testNormal() {
				User user = new User();
				int age = 22;
				user.setAge(age);
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					assertThat(user2.getAge(), equalTo(age));
				}
			}
			
			@Test
			public void testArray() {
				User user = new User();
				Integer[] ageArray = new Integer[] {22, 23, 25};
				List<Integer> ageList = Arrays.asList(ageArray);
				
				user.setAgeArray(ageArray);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(ageList.contains(user2.getAge()));
				}
			}
		
			@Test
			public void testList() {
				User user = new User();
				List<Integer> ageList = Arrays.asList(22, 23, 25);
				
				user.setAgeList(ageList);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(ageList.contains(user2.getAge()));
				}
			}
			
			@Test
			public void testMinOpen() {
				User user = new User();
				
				user.setAgeMinOpen(30);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(30 < user2.getAge());
				}
			}
			
			@Test
			public void testMaxClose() {
				User user = new User();
				
				user.setAgeMaxClose(30);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(30 >= user2.getAge());
				}
			}
			
			/**
			 * 结果集为minOpen，即为左开区间
			 * @author ChangJian
			 * @date 2019年2月26日
			 */
			@Test
			public void testDateMinOpen() {
				User user = new User();
				String minOpen = "2001-12";
				user.setBirthdayMinOpen(minOpen);
				//birthday desc
				user.setOrderBean(new OrderBean(Direction.ASC, "birthday"));
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				//结果集升序排列，第一个结果birthday大于给定的值，即测试通过
				assertThat(users.get(0).getBirthday(), Matchers.greaterThanOrEqualTo(DateUtils.getDate("2002-01")));
				
			}
			
			@Test
			public void testDateMaxClose() {
				User user = new User();
				
				user.setBirthdayMaxClose("2001-12-10");
				user.setOrderBean(new OrderBean(Direction.DESC, "birthday"));
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				//结果集降弃排列，第一个结果birthday小于等于给定的值，即测试通过
				assertThat(DateUtils.getString(users.get(0).getBirthday(), Format.yyyy_MM_dd), Matchers.equalTo("2001-12-10"));
				
			}
			
			@Test
			public void testLike() {
				User user = new User();
				String contains = "por";
				user.setHobbyLike(contains);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(user2.getHobby().contains(contains));
				}
			}
			
			@Test
			public void testStartWith() {
				User user = new User();
				String startWith = "spo"; //sport
				user.setHobbyStartWith(startWith);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(user2.getHobby().startsWith(startWith));
				}
			}
			
			@Test
			public void testEndWith() {
				User user = new User();
				String endWith = "sic"; //music
				user.setHobbyEndWith(endWith);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(user2.getHobby().endsWith(endWith));
				}
			}
			
			@Test
			public void testNullIs() {
				User user = new User();
				user.setBobbyNull(Null.IS);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(null == user2.getHobby());
				}
			}
			
			@Test
			public void testNullIsNot() {
				User user = new User();
				user.setBobbyNull(Null.NOT);
				
				List<User> users = userService.findAll(user);
				assertThat(users.size(), Matchers.greaterThan(0));
				for (User user2 : users) {
					Assert.assertTrue(null != user2.getHobby());
				}
			}
			
			/**
			 * 子对象中的约束，此时需要重写BaseServiceImpl.specification(T)，自定义Specification，继承BaseSpecification
			 * @author ChangJian
			 * @date 2019年2月26日
			 */
			@Test
			public void testSubObj() {
				User user = new User();
				String userName = "user_name_0";
				user.setName(userName);
				UserRole userRole = new UserRole(user, null);
				List<UserRole> userRoles = userRoleService.findAll(userRole);
				assertThat(userRoles.size(), Matchers.greaterThan(0));
				for (UserRole ur : userRoles) {
					assertThat(ur.getUser().getName(), equalTo(userName));
				}
			}
			
			@Test
			public void testPage() {
				int pageSize = 10;
				PageRequestBean pageRequestBean = new PageRequestBean(pageSize, 0);
				User user = new User();
				int maxAge = 35;
				user.setAgeMaxClose(maxAge);
				user.setOrderBean(new OrderBean(Direction.DESC, "age"));
				Page<User> page1 = userService.findAll(user, pageRequestBean);
				List<User> user1s = page1.getContent();
				assertThat(user1s.size(), equalTo(pageSize)); //因为不是最后一页，所以集合长度应该等于pageSize
				assertThat(user1s.get(0).getAge(), Matchers.lessThanOrEqualTo(maxAge)); 
				pageRequestBean.setPageNo(1);
				Page<User> page2 = userService.findAll(user, pageRequestBean);
				List<User> user2s = page2.getContent();
				assertThat(user1s.get(pageSize - 1).getAge(), Matchers.greaterThanOrEqualTo(user2s.get(0).getAge())); 
			}
		}

取名：圣祖雅各伯第五子，丹

* 勒乌本
* 西默盎
* 肋未
* 犹大
* 丹
* 纳裴塔里
* 加得
* 阿协尔
* 依撒加尔
* 则步隆
* 若瑟
* 本雅明
