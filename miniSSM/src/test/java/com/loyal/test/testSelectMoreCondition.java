package com.loyal.test;

import com.loyal.mapper.ProductInfoMapper;
import com.loyal.pojo.ProductInfo;
import com.loyal.pojo.vo.ProductVo;
import com.loyal.service.ProductTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class testSelectMoreCondition {
    @Autowired
    ProductInfoMapper mapper;

    @Test
    public void test1(){
        ProductVo vo = new ProductVo();

        vo.setLprice(500);
        vo.setHprice(1000);
        List<ProductInfo> lists = mapper.selectMoreCondition(vo);
        for (int i = 0; i <lists.size() ; i++) {
            System.out.println(lists.get(i).toString());
        }
    }

}
