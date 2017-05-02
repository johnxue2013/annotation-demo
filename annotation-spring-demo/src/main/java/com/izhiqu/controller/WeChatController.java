package com.izhiqu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.johnxue.common.annotation.Authority;

/**
 * 提供给微信的接口
 * Created by johnxue on 2017/1/24.
 */
@Controller
@RequestMapping
public class WeChatController {

    private static Logger logger = Logger.getLogger(WeChatController.class);


    /**
     * <p>
     * 提供给微信后台调用的api
     * <p>
     * 如果验证通过则返回echostr 否则什么都不返回
     */
    @Authority(description = "微信后台调用使用")
    @RequestMapping(value = "wx", method = RequestMethod.GET)
    @ResponseBody
    public void wx() throws IOException, DocumentException {
        //do something interesting

    }

    @RequestMapping(value = "wx", method = RequestMethod.POST)
    @ResponseBody
    public void processMessage(PrintWriter out, HttpServletRequest request) throws IOException, DocumentException {

    }


    @RequestMapping(value = "createMenu")
    public void createMenu() {



    }


}
