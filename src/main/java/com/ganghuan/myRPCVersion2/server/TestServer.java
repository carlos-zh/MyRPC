package com.ganghuan.myRPCVersion2.server;


import com.ganghuan.myRPCVersion2.service.BlogService;
import com.ganghuan.myRPCVersion2.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();
//        下面三行没有用了，已经通过provideService类封装了注册新服务和创建服务实体类的功能
//        Map<String, Object> serviceProvide = new HashMap<>();
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.UserService",userService);
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.BlogService",blogService);
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);


//        // 方法一：使用传统BIO进行客户端请求监听
//        RPCServer RPCServer = new SimpleRPCRPCServer(serviceProvider);

        // 方法二：使用线程池进行客户端请求监听
        RPCServer RPCServer = new ThreadPoolRPCRPCServer(serviceProvider);

        RPCServer.start(8899);
    }
}