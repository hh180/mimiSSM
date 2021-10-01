package com.loyal.service.Impl;

import com.loyal.mapper.ProductTypeMapper;
import com.loyal.pojo.ProductType;
import com.loyal.pojo.ProductTypeExample;
import com.loyal.service.ProductInfoService;
import com.loyal.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //注入数据访问层的对象
    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
