package com.loyal.controller;

import com.github.pagehelper.PageInfo;
import com.loyal.mapper.ProductInfoMapper;
import com.loyal.pojo.ProductInfo;
import com.loyal.pojo.vo.ProductVo;
import com.loyal.service.ProductInfoService;
import com.loyal.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductAction {
    //每页显示的条数
    public static final int PAGE_SIZE = 5;
    //图片文件名
    public String saveFileName="";
    //注入逻辑业务层对象
    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public ModelAndView getAll(){
    List<ProductInfo> list = productInfoService.getAll();
         ModelAndView modelAndView = new ModelAndView();
         modelAndView.addObject("list",list);
         modelAndView.setViewName("product");
         return modelAndView;
    }
    //查询所有商品分页处理
    @RequestMapping("/split")
    public ModelAndView splitPage(HttpSession session){
        PageInfo<ProductInfo> pageInfo = null;
        //获取session中的多条件及页码
        ProductVo vo = (ProductVo) session.getAttribute("prodVo");
        if(vo!=null){
            //session不为空时，得到更新页面的中的页码实行分页
            pageInfo = productInfoService.splitPageVo(vo, PAGE_SIZE);
            //用完之后，清掉session
            session.removeAttribute("prodVo");
        }else{
            //得到第一页的五条数据
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("info",pageInfo);
        modelAndView.setViewName("product");
        return modelAndView;
    }
    //ajax翻页
    @ResponseBody//解析ajax请求，将服务器端的值，以json格式返回给客户端
    @RequestMapping("/ajaxsplit")
    public void ajaxSplit(ProductVo vo, HttpSession session){
        PageInfo info = productInfoService.splitPageVo(vo,PAGE_SIZE);
        //将查到的数据存到session的info中，然后在客户端进行读取
        session.setAttribute("info",info);
    }

    //异步ajax文件上传及回显处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request){
        /**
         * 图片上传
         */
        //提取生成文件名UUID+上传图片的后缀.jpg,.png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/img_big");

        try {
            //转存,将文件存储到--D:\code\miniSSM\src\main\webapp\image_big\sdfyuiytdyub.jpg
            pimage.transferTo(new File(path+File.pathSeparator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 图片回显
         */
        //返回客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgurl",saveFileName);

        //将json对象转换为json字符串
        return jsonObject.toString();
    }

    //添加物品
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的五个数据，有异步ajax上传上来的图片名称数据，有上架时间数据
        //业务层调用save()方法
        int i=-1;
        try{
            i = productInfoService.save(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(i>0){
            //添加成功
            request.setAttribute("msg","添加成功");
        }else{
            //添加失败
            request.setAttribute("msg","添加失败");
        }
        //清空saveFileName的值，为了瑕疵增加或修改的异步ajax的上传处理
        saveFileName = "";
        //添加成功后，应重新跳转到分页显示的action上
        return "forward:/product/split.action";
    }

    //根据id查询商品
    @RequestMapping("/getProductById")
    //这个pid名字需要与客户端的pid名字一致，才能被spring注入
    public ModelAndView getProductById(int pid,ProductVo vo,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        ProductInfo info = productInfoService.getProductById(pid);
        modelAndView.addObject("prod",info);
        //将多条件及页码放入session中，更新处理结束后分页读取条件和页码进行处理
        session.setAttribute("prodVo",vo);
        modelAndView.setViewName("update");
        return modelAndView;
    }

    //更新商品
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        /**
         * 因为ajax的异步图片上传，如果上传过，则saveFileName里有上传上来的图片的名称
         * 如果没有上传过，则saveFileName="",实体类info使用隐藏表单域提供上来的PImage原始图片的名称
         */
         if(!saveFileName.equals("")){
             info.setpImage(saveFileName);
         }

         //完成更新处理:更新处理一般都捕获一下异常
         int num = -1;
         try {
             num = productInfoService.update(info);
         }catch (Exception e){
             e.printStackTrace();
         }
         //判断是否更新成功
        if(num>0){
            request.setAttribute("msg","更新成功！");
        }else{
            request.setAttribute("msg","更新失败！");
        }
        /**
         * 处理完更新操作之后，saveFileName里可能有数据
         * 而下一次的更新时，就要使用这个变量作为判断的依据，就会出错，所以必须清空saveFileName
         */
        saveFileName = "";
        return "forward:/product/split.action";
    }

    //单个删除
    @RequestMapping(value = "/delOne")
    public String delOne(int pid,ProductVo vo,HttpServletRequest request){
        int i=-1;
        try{
             i = productInfoService.delOne(pid);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(i>0){
            request.setAttribute("msg","删除成功");
            request.getSession().setAttribute("deleteProduVo",vo);
        }else {
            request.setAttribute("msg","删除失败");
        }

        //删除结束，跳转到ajax分页，分页显示
        return "forward:/product/delAjaxSplit.action" ;
    }
    @ResponseBody
    @RequestMapping(value = "/delAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object delAjaxSplit(HttpServletRequest request){
        PageInfo<ProductInfo> info = null;
        //拿到在deleteOne中，储藏在session中的中的各种条件
        ProductVo deleteVo = (ProductVo) request.getSession().getAttribute("deleteProduVo");
        if(deleteVo!=null){
            //获取删除这个商品的这一页所有的查询条件
            info = productInfoService.splitPageVo(deleteVo, PAGE_SIZE);
        }else{
            //取得第一页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        //由于在客户端页面使用重新加载#table，所以需要将页面数据放在session中
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //批量删除
    @RequestMapping("/deleteMore")
    public String deleteMore(String pids,HttpServletRequest request){
        //将客户端上传的pids字符串分割为字符数组
        String[] ids = pids.split(",");
        try{
            int i = productInfoService.deleteMore(ids);
            System.out.println(i);
            if(i>0){
                request.setAttribute("msg","删除成功");
            }else{
                request.setAttribute("msg","删除失败");
            }
        }catch (Exception e){
            request.setAttribute("msg","商品不可删除");
        }


        //删除结束，跳转到ajax分页，分页显示
        return "forward:/product/delAjaxSplit.action";
    }

    //多条件查询
    @ResponseBody
    @RequestMapping("/condition")
    public void selectMoreCondition(ProductVo vo,HttpServletRequest request){
        List<ProductInfo> list = productInfoService.selectMoreCondition(vo);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
        HttpSession session = request.getSession();
        //将所查询的集合，放在session中
        session.setAttribute("list",list);
    }
}
