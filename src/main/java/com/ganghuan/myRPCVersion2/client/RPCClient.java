package com.ganghuan.myRPCVersion2.client;


import com.ganghuan.myRPCVersion2.common.Blog;
import com.ganghuan.myRPCVersion2.common.User;
import com.ganghuan.myRPCVersion2.service.BlogService;
import com.ganghuan.myRPCVersion2.service.UserService;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RPCClient {
    public static void main(String[] args) {
        RPCClientProxy rpcClientProxy = new RPCClientProxy("127.0.0.1", 8899);

        // （一）一个客户端多次远程调用，依次建立connection
//        UserService userService = rpcClientProxy.getProxy(UserService.class);

//        User userByUserId = userService.getUserByUserId(10);
//        System.out.println("从服务端得到的user为：" + userByUserId);
//
//        User user = User.builder().userName("张三").id(100).sex(true).build();
//        Integer integer = userService.insertUserId(user);
//        System.out.println("向服务端插入数据："+integer);
//
//        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
//        Blog blogById = blogService.getBlogById(10000);
//        System.out.println("从服务端得到的blog为：" + blogById);


        ////////////////////////////////////////////////////////////////////////////////
        // （二）模拟多客户端并发的情况
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        new Thread(() -> {
            User userByUserId = userService.getUserByUserId(10);
            System.out.println("从服务端得到的user为：" + userByUserId);
        }, "userByUserId").start();

        new Thread(() -> {
            User user = User.builder().userName("张三").id(100).sex(true).build();
            Integer integer = userService.insertUserId(user);
            System.out.println("向服务端插入数据："+integer);
        }, "insertUserId").start();

        new Thread(() -> {
            BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
            Blog blogById = blogService.getBlogById(10000);
            System.out.println("从服务端得到的blog为：" + blogById);
        }, "getBlogById").start();

        // 使用lamda表达式来做
        new Thread(new Runnable() {
            @Override
            public void run() {
                User userByUserId = userService.getUserByUserId(10);
                System.out.println("从服务端得到的user为：" + userByUserId);
            }
        }, "testNewRunnable").start();

        // 使用自定义类实现Runnable接口来做
        new Thread(new testRunnalbe(rpcClientProxy, UserService.class, "getUserByUserId", new Class[]{Integer.class},
                new Object[]{20}), "testImplementsRunnable").start();

    }
}

@AllArgsConstructor
class testRunnalbe implements Runnable {
    RPCClientProxy rpcClientProxy;
    private Class<?> ServiceClass;
    private String methodName;
    private Class<?>[] paramsTypes;
    private Object[] params;


    @Override
    public void run() {
        Object service = this.rpcClientProxy.getProxy(ServiceClass);
        try {
            Method method = service.getClass().getMethod(methodName, paramsTypes);
            Object response = method.invoke(service, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
