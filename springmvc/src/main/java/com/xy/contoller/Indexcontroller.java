package com.xy.contoller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.xy.services.IndexServices;

import cn.hutool.core.lang.Dict;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

@Controller
public class Indexcontroller {
    private static final Log log = LogFactory.get();

    @Autowired
    private IndexServices indexService;

    private static final String UPLOAD_ROOT_PATH = "uploadFile";

    @RequestMapping(value = "/test")
    public String index(HttpServletRequest request) {
        request.setAttribute("xy", "request测试");
        Dict p = new Dict();
        p.put("xc", "郑州");
        List<Map<String, Object>> list = indexService.query(p);
        // System.out.println(list);
        return "index";
    }
    @RequestMapping(value = "/error/500")
    public String error(HttpServletRequest request) {
        request.setAttribute("xy", "request测试");
        Dict p = new Dict();
        return "error/500";
    }

    @RequestMapping(value = "/upLoad")
    @ResponseBody
    public String keupload(MultipartFile file, HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        String dirName = ServletRequestUtils.getStringParameter(request, "dir", file.getContentType().split("/")[0]);
        System.out.println("上传：" + dirName);
       
        
        int tp = ServletRequestUtils.getIntParameter(request, "tp", 0);
        String savePath = request.getRealPath("/") + UPLOAD_ROOT_PATH + "/";

        // 定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png");
        extMap.put("flash", "swf,flv");
        extMap.put("video", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
        extMap.put("pdf", "pdf");
        // 最大文件大小
        long maxSize = 1024 * 1000 * 1000;

        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();
        // 检查扩展名
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
            obj.put("error", 1);
            obj.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
            return obj.toString();
        }
        // 检查文件大小
        if (fileSize > maxSize) {
            obj.put("error", 1);
            obj.put("message", "上传文件大小超过限制");
            return obj.toString();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath += dirName + "/" + ymd + "/";
        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }

        String newFileName = UUID() + "." + fileExt;
        try {
            File targetFile = new File(savePath, newFileName);
            file.transferTo(targetFile);
            targetFile.setReadOnly();
            targetFile.setWritable(false);
            obj.put("realFileName", fileName);
            if (tp == 1) {
                obj.put("filePath", "/" + UPLOAD_ROOT_PATH + "/" + dirName + "/" + ymd + "/" + newFileName);
            } else {
                obj.put("filePath", "/" + UPLOAD_ROOT_PATH + "/" + dirName + "/" + ymd + "/" + newFileName);
            }
            obj.put("success", true);
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("msg", "上传文件失败");
            return obj.toString();
        }

        return obj.toString();
    }

    /*
     * 采用spring提供的上传文件的方法
     */
    @RequestMapping("springUpload")
    @ResponseBody
    public String springUpload(HttpServletRequest request) throws IllegalStateException, IOException, ServletRequestBindingException {
        try {
            int i = 1/0;
        } catch (Exception e) {
            log.error(e);
        }
        String pname = ServletRequestUtils.getStringParameter(request, "name", "");//获取参数，添加默认值
        System.out.println("name:" + pname);
        JSONObject json = new JSONObject();
        long startTime = System.currentTimeMillis();
        // 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
      
        String savePath = request.getRealPath("/") + UPLOAD_ROOT_PATH;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath = savePath + File.separator + ymd;
        File ufile = new File(savePath);
        if (!ufile.exists()) {
            ufile.mkdirs();
        }
        System.out.println(savePath);
        String path = "";
        String fileName = "";
        long size = 0;
        DecimalFormat df = new DecimalFormat("0.00");// 格式化数字，保留两位小数
        // 最大文件大小 1024*1024*1000字节 1000M
        long maxSize = 1024 * 1024 * 1000;
        // 检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            // 将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                String name = file.getName();
                String oname = file.getOriginalFilename();
                String type = file.getContentType();
                size = file.getSize();

                if(size > maxSize){
                    json.put("error", 1);
                    json.put("info", "上传失败");
                    return json.toString();
                }
                System.out.println("name:" + name);
                System.out.println("oname:" + oname);
                System.out.println("type:" + type);
                System.out.println("size:" + size);
                double s = size / 1024.00;
                String suf = oname.substring(oname.lastIndexOf(".") + 1).toLowerCase();
                System.out.println(s);
                s = s / 1024.00;
                System.out.println(s);
                if (file != null) {
                    fileName = file.getOriginalFilename();
                    path = savePath + File.separator + UUID() + "." + suf;
                    // 上传
                    file.transferTo(new File(path));
                }
            }

        }
        long endTime = System.currentTimeMillis();
        json.put("path:", path);
        System.out.println(path);
        json.put("time:", String.valueOf(endTime - startTime) + "ms");
        json.put("type:", "spring上传文件");
        json.put("fileName:", fileName);
        json.put("size:", size);
        return json.toString();
    }

    public String UUID() {
        return java.util.UUID.randomUUID().toString();
    }
}
