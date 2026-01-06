package com.fengpei.web;

import com.fengpei.web.controller.HelloController;
import com.fengpei.web.entiry.BaseData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebApplication  {//implements CommandLineRunner
//    private final HelloController myController; // 注入 Controller
//
//    public WebApplication(HelloController myController) {
//        this.myController = myController;
//    }
    public static void main(String[] args) {
       SpringApplication.run(WebApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        // 调用 sendMsgToUser
//        Mono<BaseData> resultMono = myController.sendMsgToUser("【商企云信】张星星您好：您提交的申请系统已审核通过，详情请联系业务员！", "18766282683");
//
//        // 异步处理
//        resultMono.subscribe(result -> {
//            System.out.println("发送结果: " + result.msg);
//        });
//
//
//    }
}
