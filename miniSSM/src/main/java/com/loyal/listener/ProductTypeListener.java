package com.loyal.listener;

import com.loyal.pojo.ProductType;
import com.loyal.service.Impl.ProductTypeServiceImpl;
import com.loyal.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //手工从Spring容器中取出ProductTypeServiceImpl对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_*.xml");
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList = productTypeService.getAll();
        //放入到全局的作用域中，供新增，修改，删除，查询功能提供全部的商品类型集合
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("typeList",typeList);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
