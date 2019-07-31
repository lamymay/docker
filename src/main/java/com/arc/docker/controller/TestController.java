package com.arc.docker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：t_sys_menu控制层
 *
 * @author yechao
 * @date 2018-10-18 16:37:18
 */
@Slf4j
@RestController
@RequestMapping(path = "/test")
public class TestController {


    @GetMapping("/code")
    public ResponseEntity code() {
        Map<String, Object> map = new HashMap<String, Object>();
        ResponseEntity responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/code2")
    public ResponseEntity code2() {
        ResponseEntity responseEntity = new ResponseEntity<Object>("abc", HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 文件下载方法
     *
     * @param response
     * @param filePath
     * @param encode
     */
    public static void download(HttpServletResponse response, String filePath, String encode) {
        response.setContentType("text/html;charset=" + encode);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String downLoadPath = filePath;
        try {
            File file = new File(downLoadPath);
            long fileLength = file.length();
            String fileName = file.getName();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(encode), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
        }
    }

    /**
     * 以流的方式下载
     *
     * @param response
     * @param filePath
     * @param encode
     * @return response
     */
    public static HttpServletResponse downloadStream(HttpServletResponse response, String filePath, String encode) {
        response.setContentType("text/html;charset=" + encode);
        try {
            // path是指欲下载的文件的路径
            File file = new File(filePath);
            // 取得文件名
            String filename = file.getName();
            // 取得文件的后缀名
            // String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(encode), "ISO8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return response;
    }

    /**
     * 下载本地文件
     *
     * @param response
     * @param filePath
     * @param encode
     */
    public static void downloadLocal(HttpServletResponse response, String filePath, String encode) {
        response.setContentType("text/html;charset=" + encode);
        try {
            // 读到流中
            InputStream inStream = new FileInputStream(filePath); // 文件的存放路径
            // path是指欲下载的文件的路径
            File file = new File(filePath);
            // 取得文件名
            String fileName = file.getName();
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(encode), "ISO8859-1") + "\"");
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
