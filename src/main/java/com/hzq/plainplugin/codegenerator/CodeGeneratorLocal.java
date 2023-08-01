package com.hzq.plainplugin.codegenerator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Unit test for simple App.
 */
public class CodeGeneratorLocal
{
    static String projectPath = System.getProperty("user.dir");
    /**
     * <p>
     * 读取控制台内容
     * </p>
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

    public static GlobalConfig getGc(){
        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(projectPath + "/src/main/java");

        gc.setFileOverride(true);
        // 开启 activeRecord 模式
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        //格式化自定义类名
        gc.setServiceName("%sService");

        gc.setAuthor(scanner("输入作者名字"));
        gc.setOpen(false);
        gc.setSwagger2(true);
        return gc;
    }

    public static DataSourceConfig getDsc(){
        // todo 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://ip:port/mall_moduledb?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("username");
        dsc.setPassword("password");
        return dsc;
    }

    public static PackageConfig getPkg(){
        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));
        // 第二级包路径
        pc.setModuleName(scanner("输入子包路径（如：user）"));
        // 第一级包路径
        pc.setParent(scanner("输入主包路径（如：com.hzq）"));
        return pc;
    }

    public static InjectionConfig getCfg(){
        // 自定义配置
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
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    public static TemplateConfig getTmp(){
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

    public static StrategyConfig getStr(String name){
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setRestControllerStyle(true);
//        strategy.setSuperEntityClass("ModuleBaseDomain");
        strategy.setEntityLombokModel(true);
//        strategy.setSuperMapperClass("mapper.BaseMapper");
//        strategy.setSuperServiceClass("service.BaseService");
//        strategy.setSuperServiceImplClass("service.BaseServiceImpl");
//        strategy.setSuperControllerClass("controller.BaseController");
//        strategy.setInclude(scanner("表名"));

        //指定表名
        strategy.setInclude(scanner("输入表名（如：table_name）"));
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
    /*public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        GlobalConfig gc = getGc();
        mpg.setGlobalConfig(gc);

        //获取数据源
        DataSourceConfig dsc = getDsc();
        mpg.setDataSource(dsc);

        //获取包配置
        PackageConfig pkg = getPkg();
        mpg.setPackageInfo(pkg);

        //自定义（特殊输出路径等）
        InjectionConfig cfg = getCfg();
        mpg.setCfg(cfg);


        //模板配置
        TemplateConfig templateConfig = getTmp();
        mpg.setTemplate(templateConfig);

        //策略配置（父包配置等）
        StrategyConfig str = getStr(pkg.getModuleName());
        mpg.setStrategy(str);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //执行
        mpg.execute();
    }*/

    /*public static void main(String[] args) throws IOException {

        MpIdeaConfig mp = new MpIdeaConfig();
        mp.setIp("1");
        mp.setMain("sultan");
        mp.setModule("3");
        mp.setName("按劳动法");
        mp.setPass("5");
        mp.setSec("6");
        mp.setTable("7");
        mp.setUser("8");
        //本地文件记忆
        File file = new File("C:\\\\mp-idea.txt");
        if(!file.exists()){
            try {
                file.createNewFile(); //如果文件不存在则创建文件
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(JSONObject.toJSONString(mp).getBytes());


        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[2];
        ByteArrayOutputStream byteBufferOutputStream = new ByteArrayOutputStream();
        int len = 0;
        while((len = inputStream.read(bytes))!=-1){
            byteBufferOutputStream.write(bytes,0,len);
        }
        String s = new String(byteBufferOutputStream.toByteArray());

        MpIdeaConfig mpa = JSONObject.parseObject(s,MpIdeaConfig.class);

        outputStream.close();
        inputStream.close();
        byteBufferOutputStream.close();

        System.out.println(mpa);
    }*/

}
