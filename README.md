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

	代码地址：[https://github.com/PaulusChang/jacob-dan/blob/master/src/test/java/jacob/dan/test/GenerateDataTest.java](https://github.com/PaulusChang/jacob-dan/blob/master/src/test/java/jacob/dan/test/GenerateDataTest.java)

2. 测试封装好的查询方法
	
	测试示例中注入的 UserService 是自动生成的，继承了 BaseService。完事测试代码参见 [https://github.com/PaulusChang/jacob-dan/blob/master/src/test/java/jacob/dan/test/QueryTest.java](https://github.com/PaulusChang/jacob-dan/blob/master/src/test/java/jacob/dan/test/QueryTest.java)， 简单的调用示例如下

	
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
欢迎讨论分享

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
