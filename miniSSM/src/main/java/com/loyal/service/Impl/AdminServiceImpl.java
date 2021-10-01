package com.loyal.service.Impl;

import com.loyal.mapper.AdminMapper;
import com.loyal.pojo.Admin;
import com.loyal.pojo.AdminExample;
import com.loyal.service.AdminService;
import com.loyal.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    //注入数据访问层的对象
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        //根据传入的用户名，密码在数据库中寻找对应的用户
        //如果存在条件，则一定要创建AdminExample的对象，用来封装条件
        AdminExample adminExample = new AdminExample();
        //添加用户名a_name的条件
        adminExample.createCriteria().andANameEqualTo(name);
        //执行查询
        /**
         * select * from admin where a_name='admin'
         */
        List<Admin> list = adminMapper.selectByExample(adminExample);
        if(list.size()>0){
            Admin admin = list.get(0);
            //如果查询到用户对象，再进行密码比对
            String apass = MD5Util.getMD5(pwd);
            if(apass.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}
