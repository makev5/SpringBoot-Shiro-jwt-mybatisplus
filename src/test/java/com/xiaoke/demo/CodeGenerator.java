package com.xiaoke.demo;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CodeGenerator {


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

}