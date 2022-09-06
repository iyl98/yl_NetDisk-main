package com.yl;

import com.yl.mapper.UserMapper;
import com.yl.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class YlNetDiskMainApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserMapper userMapper;

    //测试数据库连接
    @Test
    void contextLoads() throws SQLException{
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }

    //测试数据库查询
    @Test
    void contextLoad_(){
        User user = userMapper.queryById(10000);
        System.out.println(user);
    }

}
