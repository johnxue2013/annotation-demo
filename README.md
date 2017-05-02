![MIT Licence](https://travis-ci.org/johnxue2013/annotation-demo.svg?branch=master)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
# 自定义注解在spring中的使用demo
有些时候spring提供的注解不能满足需求时，需要自定义注解。如提取项目中所有action，并标明其意义。此demo演示在spring中如何自定义注解，并如何解析自定义的注解。

## demo
1. clone此项目
```bash
# git clone https://github.com/johnxue2013/annotation-demo.git

```

2. 修改配置  
  修改路径annotation-spring-demo/src/main/resources/spring/applicationContext.xml文件中的  
```xml
<bean class="com.johnxue.common.config.AuthorityConfig">
    <property name="basePackages">
        <list>
            <value>com.izhiqu.controller</value>
        </list>
    </property>
    <property name="destination">
        <!--修改成本地实际存在路径，文件可以不存在，只要根路径存在就行-->
        <value>D:\\api.txt</value>
    </property>
</bean>

<bean class="com.johnxue.common.authority.DefaultAuthorityAdapter"/>

```  
3. 生成war包
进入项目的根目录运行命令  
```Bash
mvn clean install
```
等待命令行输出BUILD SUCCESS字样，失败请检查是否maven版本过低,或者[提issue][1]

4. 部署运行  
找到上一步骤输出的war包，放入tomcat的webapps目录下，运行tomcat，启动完毕后。
访问http://localhost:8080/annotation-spring-demo/indexController/demo ,打开步骤2中配置的输出文件即可看到数据结果
``` 
[description=这是一个测试的http接口,url=/indexController/test,method=GET/POST]
[description=微信后台调用使用,url=/wx,method=GET]

```  
   
## 将此注解集成进其他spring项目?  
1. clone此项目
```bash
# git clone https://github.com/johnxue2013/annotation-demo.git

```
2. 基本配置

首先，如果项目是使用maven进行管理的，则修改项目的pom.xml文件。
在`<dependencies>`标签下添加如下内容
```xml
<dependency>
    <groupId>com.johnxue.annotation</groupId>
    <artifactId>common-util</artifactId>
    <version>1.0-SNAPSHOT</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
        </exclusion>

        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </exclusion>

        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
如果不是则找到上一步输出的common-util jar包，扔到项目的lib目录下


配置好依赖后,修改spring的配置文件，如本demo中的配置文件是applicationContext.xml。添加
如下配置
```xml
<bean class="com.johnxue.common.config.AuthorityConfig">
    <property name="basePackages">
        <list>
            <value>com.izhiqu.controller</value>
        </list>
    </property>
    <property name="destination">
        <!--修改成本地实际存在路径，文件可以不存在，只要根路径存在就行-->
        <value>D:\\api.txt</value>
    </property>
</bean>
```

做完上述操作后，在任意想要导出的Controller中的action上添加`@Authrory(description=<说明>)`,启动项目，
调用暴露出的接口，程序将自动解析此注解，调用相应接口就可以导出到文件。

> `@Authrory(description=<说明>)` 中的<说明>请替换成有实际意义的值。


> 示例如下: 修改或新增任意一个controller，在controller中调用暴露出的接口。
for example
```java
package com.izhiqu.controller;

import com.johnxue.common.annotation.Authority;
import com.johnxue.common.authority.IAuthority;
import com.johnxue.common.bean.AuthorityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例controller
 *
 * @author han.xue
 * @since 2017-04-30 19:49:40
 */
@Controller
@RequestMapping("indexController")
public class IndexController {

    /**
     * 注入提供的类
     */
    @Autowired
    private IAuthority authority;

    @Authority(description = "这是一个测试的http接口")
    @RequestMapping("test")
    @ResponseBody
    public List<String> test() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }


    @RequestMapping("demo")
    @ResponseBody
    public void demo() {


        //获取所有controller中使用@Authority注解的接口
        List<AuthorityInfo> allAuthority = authority.getAllAuthority();
        System.out.println("---------------------------------------------------");
        //打印到控制台
        System.out.println(allAuthority);
        //调用提供的服务，导出到文件,导出的位置取决于具体的配置,具体可参考applicationContext.xml
        //中的配置
        boolean export = authority.export();
        System.out.println(export);
        System.out.println("---------------------------------------------------");
    }

}

```  
启动tomcat，访问`http://<host>:<port>/<project-name>/indexController/demo` 即可。

最终可查看导出的文件得到内容如下
``` 
[description=这是一个测试的http接口,url=/indexController/test,method=GET/POST]
[description=微信后台调用使用,url=/wx,method=GET]

```  
> 说明：结果文件中的第二条是另一个WeChatController中的  

## 不满足于导出结果到文件?
导出到文件只是一个简单的demo，你可以只需要编写少量的代码就可定制结果导出的位置。
最简单的就是继承自`com.johnxue.common.authority.DefaultAuthorityAdapter`并重写
`protected boolean doExport(List<AuthorityInfo> authorityInfos)`，实现你自己的逻辑。如导出到数据库或导出
到excel。  

最后别忘记修改对应的spring配置文件。  
```xml
<bean class="com.johnxue.common.config.AuthorityConfig">
    <property name="basePackages">
        <list>
            <value>com.izhiqu.controller</value>
        </list>
    </property>
    <property name="destination">
        <!--修改成本地实际存在路径，文件可以不存在，只要根路径存在就行-->
        <value>D:\\api.txt</value>
    </property>
</bean>  

<!--此处替换成你自己的实现类-->
<bean class="com.XXX.XXX.XXX.YourAuthorityAdapter"/>

```


## 写在最后  
水平有限，如发现问题，及时[提bug][1]  





[1]:https://github.com/johnxue2013/annotation-demo/issues "提bug的超链接"








