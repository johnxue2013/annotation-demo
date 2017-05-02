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
