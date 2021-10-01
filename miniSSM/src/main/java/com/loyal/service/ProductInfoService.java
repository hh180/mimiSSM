package com.loyal.service;

import com.github.pagehelper.PageInfo;
import com.loyal.pojo.ProductInfo;
import com.loyal.pojo.vo.ProductVo;

import java.util.List;

public interface ProductInfoService {
    List<ProductInfo> getAll();

    PageInfo splitPage(int pageNum,int PageSize);

    int save(ProductInfo info);

    ProductInfo getProductById(int pid);

    //更新商品
    int update(ProductInfo info);

    //删除商品
    int delOne(int pid);

    //批量删除
    int deleteMore(String[] pids);

    //多条件查询
    List<ProductInfo> selectMoreCondition(ProductVo vo);

    //多条件查询分页
    PageInfo<ProductInfo> splitPageVo(ProductVo vo,int PageSize);
}
