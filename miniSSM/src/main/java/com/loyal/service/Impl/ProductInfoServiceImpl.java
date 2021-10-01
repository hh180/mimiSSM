package com.loyal.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loyal.mapper.ProductInfoMapper;
import com.loyal.pojo.ProductInfo;
import com.loyal.pojo.ProductInfoExample;
import com.loyal.pojo.vo.ProductVo;
import com.loyal.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    //注入数据访问层对象
    @Autowired
    ProductInfoMapper productInfoMapper;
    //查询所有不分页
    @Override
    public List<ProductInfo> getAll() {
        //创建productInfoExample对象封装条件
        ProductInfoExample productInfoExample = new ProductInfoExample();
        return productInfoMapper.selectByExample(productInfoExample);
    }
    //查询所有，分页

    /**
     *select * from product_info limit 起始记录数=((当前页-1)*每页显示的条数)，每页显示的条数
     */
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum,pageSize);
        //进行PageInfo的数据封装
        //进行有条件的查询操作，必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按主键降序排序
        //select * from product_info order by p_id desc
        example.setOrderByClause("p_id desc");
        //执行查询
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //将查询到的集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        int i = productInfoMapper.insert(info);
        return i;
    }

    @Override
    public ProductInfo getProductById(int pid) {
        ProductInfo info = productInfoMapper.selectByPrimaryKey(pid);
        return info;
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delOne(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteMore(String[] pids) {
        return productInfoMapper.deleteMore(pids);
    }

    @Override
    public List<ProductInfo> selectMoreCondition(ProductVo vo) {
        List<ProductInfo> list = productInfoMapper.selectMoreCondition(vo);
        return list;
    }

    //PageSize,每页显示的条数
    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductVo vo, int PageSize) {
        //查询之前，设置PageHpelper.startPage()属性
        PageHelper.startPage(vo.getPage(),PageSize);
        //执行查询
        List<ProductInfo> list = productInfoMapper.selectMoreCondition(vo);
        PageInfo<ProductInfo> pageInfo = new PageInfo<ProductInfo>(list);

        return pageInfo;
    }


}
