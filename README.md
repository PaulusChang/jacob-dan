# jacob-dan

simple springboot+jpa

* 要求实体类继承jacob.dan.base.beanBaseEntity，根据《阿里巴巴Java开发手册》，

>三、MySQL规约
【强制】表必备三字段：id, gmt\_create, gmt\_modified。 说明：其中id必为主键，类型为unsigned bigint、单表时自增、步长为1。gmt\_create, gmt\_modified的类型均为date\_time类型。

BaseEntity中id为String类型，由SnowflakeIdFactory产生，除了上述字段，还增加了is\_deleted字段作为删除标识

创建好实体类后，可执行jacob.dan.generator.code.Generator.main(String[])方法生成相应的service/repository代码

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
