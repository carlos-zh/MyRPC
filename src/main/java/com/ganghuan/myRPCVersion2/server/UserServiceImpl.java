package com.ganghuan.myRPCVersion2.server;


import com.ganghuan.myRPCVersion2.common.User;
import com.ganghuan.myRPCVersion2.service.UserService;

//import java.util.concurrent.TimeUnit;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        // 模拟从数据库中取用户的行为
        User user = User.builder().id(id).userName("he2121").sex(true).build();
        System.out.println("客户端查询了"+id+"用户");
        // @@@@@@@@@@@@@@@@@@@@@@@@@
//        TimeUnit.SECONDS.sleep(5);
//        Thread.currentThread().sleep(1000);
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功："+user);
        return 1;
    }
}
