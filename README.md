# SpringBoot+Shiro+jwt+mybatisplus权限认证

**特性**

------

- 完全使用了Shiro的注解配置，保持高度的灵活性。
- 放弃Cookie,Session,使用JWT进行鉴权，完全实现无状态鉴权。
- JWT密钥支持过期时间。
- 对跨域提供支持

**程序逻辑**

------

1. 我们POST用户名与密码到`/login`进行登入，如果成功返回一个加密token，失败的话直接返回401错误。
2. 之后用户访问每一个需要权限的网址请求必须在`header`中添加`Authorization`字段，例如`Authorization: token`，`token`为密钥。
3. 后台会进行`token`的校验，如果有误会直接返回401。

**Token加密说明**

***

* 携带了`username`信息在token中。
* 设定了过期时间。
* 使用用户登入密码对`token`进行加密

**Token校验流程**

***

1. 获得`token`中携带的`username`信息。
2. 进入数据库搜索这个用户，得到他的密码。
3. 使用用户的密码来检验`token`是否正确。

用户表tb_user

|  ID  | USERNAME | PASSWORD | ROLE  | PERMISSION |
| :--: | :------: | :------: | :---: | :--------: |
|  1   |  admin   |  123456  | admin | edit,view  |
|  2   |   user   |  123456  | user  |    view    |

准备Maven文件

***

新建一个SpringBoot项目，添加相关的dependencies

```xml
	<dependencies>
		<!-- 引入spring boot开始 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<!-- spring-boot-starter-test -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!-- spring-boot-starter-web -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<!-- springfox-swagger2 -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.9.2</version>
		</dependency>

		<!-- springfox-swagger-ui -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.9.2</version>
		</dependency>


		<!-- shiro与spring整合 -->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-spring</artifactId>
			<version>1.4.0</version>
		</dependency>

		<!-- jwt -->
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.2.0</version>
		</dependency>


		<!-- MYSQL -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.47</version>
		</dependency>

		<!-- lombok -->
		<dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

		<!-- Mybatis-plus -->
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-boot-starter</artifactId>
			<version>3.1.0</version>
		</dependency>

		<!-- 代码生成器 -->
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-generator</artifactId>
			<version>3.3.1</version>
		</dependency>
	
		<!-- Apache velocity -->
		<dependency>
			<groupId>org.apache.velocity</groupId>
			<artifactId>velocity-engine-core</artifactId>
			<version>2.0</version>
		</dependency>

		<!-- slf4j -->
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-api</artifactId>
			<version>1.7.7</version>
		</dependency>
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.7</version>
		</dependency>


		<!-- junit -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
		</dependency>
 
	</dependencies>
```

**代码生成器**

***

```java
public static void main(String[] args) {

    //1. 全局配置
    GlobalConfig globalConfig = new GlobalConfig();
    globalConfig.setActiveRecord(true)	// AR模式
        .setAuthor("Ma Ke") // 作者
        .setOutputDir(System.getProperty("user.dir") + "/src/main/java")  // 生成路径
        .setFileOverride(true) // 文件覆盖
        .setIdType(IdType.AUTO) // 主键策略
        .setServiceName("%sService") // 设置生成service接口的名字的首字母是否为I
        .setBaseResultMap(true)
        .setBaseColumnList(true);


    //2. 数据源配置
    DataSourceConfig dataSource = new DataSourceConfig();
    dataSource.setDbType(DbType.MYSQL)
        .setDriverName("com.mysql.jdbc.Driver") 
        .setUrl("jdbc:mysql://localhost:3306/xiaoke")
        .setUsername("root")
        .setPassword("root");


    //3. 包名策略配置
    PackageConfig packageConfig = new PackageConfig();
    packageConfig.setParent("com.xiaoke.demo")
        //  .setModuleName("blog")

        .setMapper("mapper")
        .setService("service")
        .setController("controller")
        .setEntity("beans")
        .setXml("mapper");

    //4. 策略配置
    StrategyConfig strategyConfig = new StrategyConfig();
    strategyConfig.setCapitalMode(true)	// 全局大写命名
        .setNaming(NamingStrategy.underline_to_camel)
        .setTablePrefix("tb_")
        .setInclude("tb_user");   // 生成的表


    //5.整合配置
    AutoGenerator autoGenerator = new AutoGenerator();
    autoGenerator.setGlobalConfig(globalConfig)
        .setDataSource(dataSource)
        .setPackageInfo(packageConfig)
        .setStrategy(strategyConfig);

    //6.执行
    autoGenerator.execute();

}
```

**URL结构**

***

| URL                 | 作用                                           |
| ------------------- | ---------------------------------------------- |
| /login              | 登入                                           |
| /article            | 所有人都可以访问，但是用户与游客看到的内容不同 |
| /require_auth       | 登入的用户才可以进行访问                       |
| /require_role       | admin的角色用户才可以登入                      |
| /require_permission | 拥有view和edit权限的用户才可以访问             |

### 总结

***

我就说下代码还有哪些可以进步的地方吧

- 没有实现Shiro的`Cache`功能。
- Shiro中鉴权失败时不能够直接返回401信息，而是通过跳转到`/401`地址实现。
- jwt默认是不加密的（其中的加密算法只是用来再jwt的第三部分signature做加密，防止数据篡改），需要加密的话，生成原始 Token 以后，可以用密钥再加密一次（可以用AES算法）。文章的github-demo中没有再次加密。

该图展示的是原始token，可以解析出数据
