# jacob-dan

simple springboot+jpa

* 要求实体类继承jacob.dan.base.beanBaseEntity，根据《阿里巴巴Java开发手册》，

>三、MySQL规约
【强制】表必备三字段：id, gmt\_create, gmt\_modified。 说明：其中id必为主键，类型为unsigned bigint、单表时自增、步长为1。gmt\_create, gmt\_modified的类型均为date\_time类型。

BaseEntity中id为String类型，由SnowflakeIdFactory产生，除了上述字段，还增加了is\_deleted字段作为删除标识

创建好实体类后，可执行jacob.dan.generator.code.Generator.main(String[])方法生成相应的service/repository代码


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

2. http://localhost:8080/test/generatorUser
3. http://localhost:8080/test/generatorRole
4. http://localhost:8080/test/generatorUserRole
---
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
