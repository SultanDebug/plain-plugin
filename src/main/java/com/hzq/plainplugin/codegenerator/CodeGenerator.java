package com.hzq.plainplugin.codegenerator;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Unit test for simple App.
 */
public class CodeGenerator {
//    static String projectPath = System.getProperty("user.dir");

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static GlobalConfig getGc(String path, String author, String module) {
        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(path + module + "/src/main/java");

        //是否覆盖 是否覆盖原来代码
        gc.setFileOverride(true);
        // 开启 activeRecord 模式
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setAuthor(author);
        //生成代码后，是否打开文件夹
        gc.setOpen(false);
        //格式化定义类名
        gc.setServiceName("%sService");
        gc.setEntityName("%sDomain");
        // 实体属性 Swagger2 注解,实体类上会增加注释
        gc.setSwagger2(true);
        return gc;
    }

    public static DataSourceConfig getDsc(String db, String ip, String user, String pass) {
        // todo 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://ip:port/db?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true");
        // dsc.setSchemaName("public");

        if (db.equals("mysql")) {
            dsc.setDriverName("com.mysql.cj.jdbc.Driver");
            dsc.setUrl("jdbc:mysql://" + ip + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true");
        } else {
            dsc.setDriverName("org.postgresql.Driver");
            dsc.setUrl("jdbc:postgresql://" + ip);
        }
        dsc.setUsername(user);
        dsc.setPassword(pass);
//        dsc.setUsername("mall_moduledb");
//        dsc.setPassword("H639yv8XBRUKnh1f");
        return dsc;
    }

    public static PackageConfig getPkg(String main, String sec) {
        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));
        // 第二级包路径
        pc.setModuleName(sec);
        // 第一级包路径
        pc.setParent(main);
        return pc;
    }

    public static InjectionConfig getCfg(String path, String module) {
        // 自定义配置mapper.xml输出路径
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // 设置ftl自定义参数
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件路径及名称
                return path + module + "/src/main/resources/mapper/" + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    public static TemplateConfig getTmp() {
        //模板定义
        TemplateConfig templateConfig = new TemplateConfig();

        //全局设为null表示不生成
        templateConfig
                // entity模板采用自定义模板
                .setEntity("templates/entity.java")
                // mapper模板采用自定义模板
                .setMapper("templates/mapper.java")
                // 不生成xml文件
                .setXml(null)
                // service模板采用自定义模板
                .setService("templates/service.java")
                // serviceImpl模板采用自定义模板
                .setServiceImpl("templates/serviceImpl.java")
                // controller模板采用自定义模板
                .setController("templates/controller.java");
        return templateConfig;
    }

    public static StrategyConfig getStr(String name, String tabName) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setRestControllerStyle(true);
//        strategy.setSuperEntityClass("ModuleBaseDomain");
        strategy.setEntityLombokModel(true);
        strategy.setSuperMapperClass("mapper.BaseMapper");
        strategy.setSuperServiceClass("service.BaseService");
        strategy.setSuperServiceImplClass("service.BaseServiceImpl");
        strategy.setSuperControllerClass("controller.BaseController");
//        strategy.setInclude(scanner("表名"));

        //指定表名
//        strategy.setInclude(scanner("输入表名（如：table_name）"));
        strategy.setInclude(tabName);
        //id继承
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(name + "_");
        return strategy;
    }

    /**
     * 入口
     * 修改todo数据源部分
     */
    public static void init(String db, String path, String ip, String user, String pass, String author, String main, String sec, String table, String module) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        GlobalConfig gc = getGc(path, author, module);
        mpg.setGlobalConfig(gc);

        //获取数据源
        DataSourceConfig dsc = getDsc(db, ip, user, pass);
        mpg.setDataSource(dsc);

        //获取包配置
        PackageConfig pkg = getPkg(main, sec);
        mpg.setPackageInfo(pkg);

        //自定义（特殊输出路径等）
        InjectionConfig cfg = getCfg(path, module);
        mpg.setCfg(cfg);


        //模板配置
        TemplateConfig templateConfig = getTmp();
        mpg.setTemplate(templateConfig);

        //策略配置（父包配置等）
        StrategyConfig str = getStr(pkg.getModuleName(), table);
        mpg.setStrategy(str);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //执行
        mpg.execute();
    }

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\\\mp-idea.txt");
        MpIdeaConfig mpa = null;
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream byteBufferOutputStream = new ByteArrayOutputStream();
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {
                    byteBufferOutputStream.write(bytes, 0, len);
                }
                String s = new String(byteBufferOutputStream.toByteArray(), StandardCharsets.UTF_8);
                mpa = JSONObject.parseObject(s, MpIdeaConfig.class);
                inputStream.close();
                byteBufferOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mpa = new MpIdeaConfig();
            mpa.setDb("mysql");
            mpa.setName("author");
            mpa.setIp("ip:port/db");
            mpa.setUser("username");
            mpa.setPass("pass");
            mpa.setModule("/sub-module");
            mpa.setMain("com.hzq");
            mpa.setSec("subpackage");
            mpa.setTable("tablename");
        }

        init(mpa.getDb(), System.getProperty("user.dir"), mpa.getIp(), mpa.getUser(), mpa.getPass(), mpa.getName(), mpa.getMain(), mpa.getSec()
                , mpa.getTable(), mpa.getModule());
    }


}
