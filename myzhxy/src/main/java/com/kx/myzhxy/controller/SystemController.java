package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kx.myzhxy.pojo.Admin;
import com.kx.myzhxy.pojo.LoginForm;
import com.kx.myzhxy.pojo.Student;
import com.kx.myzhxy.pojo.Teacher;
import com.kx.myzhxy.service.AdminService;
import com.kx.myzhxy.service.StudentService;
import com.kx.myzhxy.service.TeacherService;
import com.kx.myzhxy.util.*;
import com.sun.deploy.net.HttpResponse;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpLE;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName SystemController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:18
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession session, HttpServletResponse response){
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        char[] verifiCode = CreateVerifiCodeImage.getVerifiCode();
        String verifiCodeString=new String(verifiCode);
        session.setAttribute("verifiCode",verifiCodeString);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(verifiCodeImage,"JPEG",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpSession session) {

        System.out.println(MD5.encrypt(loginForm.getPassword()));
        //验证码校验
        String verifiCode = (String) session.getAttribute("verifiCode");
        String verifiCode1 = loginForm.getVerifiCode();

        if("".equals(verifiCode)||null==verifiCode){
            return Result.fail().message("验证码失效请刷新后重试");
        }
        if (!verifiCode.equalsIgnoreCase(verifiCode1)) {
            return Result.fail().message("验证码输入错误请重新输入");
        }
        //从session中移除验证码

        Map<Object,String> map=new HashMap<>();
        //分用户类型进行校验
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin =  adminService.login(loginForm);
                    if(admin!=null){
                        String token = JwtHelper.createToken(admin.getId().longValue(), loginForm.getUserType());
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student =  studentService.login(loginForm);
                    if(student!=null){
                        String token = JwtHelper.createToken(student.getId().longValue(), loginForm.getUserType());
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher =  teacherService.login(loginForm);
                    if(teacher!=null){
                        String token = JwtHelper.createToken(teacher.getId().longValue(), loginForm.getUserType());
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("没有此用户");
    }


    @GetMapping("/getInfo")
    public Result getInfo(@RequestHeader("token")String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        Map<String,Object> map=new HashMap<>();
        switch (userType){
            case 1:
                    Admin admin = adminService.getAdminById(userId);
                    map.put("userType",1);
                    map.put("user",admin);
                    break;
            case 2:
                Student student=studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }

    @PostMapping("/headerImgUpload")
    public Result getHeaderImg(@RequestPart("multipartFile") MultipartFile multipartFile){
        if(!multipartFile.isEmpty()){
            String originalFilename = multipartFile.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            String s= UUID.randomUUID().toString();
            String name=originalFilename.substring(i);
            String filename=s.concat(name);
            String photoPath="D:\\code\\myzhxy\\target\\classes\\public\\upload";
            String finalFileName=photoPath+File.separator+filename;
            try {
                multipartFile.transferTo(new File(finalFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path="upload\\"+filename;
            return Result.ok(path);
        }
        return Result.fail().message("图片为空");
    }

    //sms/system/updatePwd/admin/111111
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,
                            @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null,ResultCodeEnum.TOKEN_ERROR);
        }
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        switch (userType){
            case 1:
              QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
              queryWrapper.eq("id",userId)
                      .eq("password", MD5.encrypt(oldPwd));
                Admin admin = adminService.getOne(queryWrapper);
                if(admin!=null){
                    admin.setPassword(MD5.encrypt(newPwd));
                    boolean b = adminService.saveOrUpdate(admin);
                    return Result.ok();
                }else{
                    return Result.fail().message("原密码不存在");
                }
            case 2:
                QueryWrapper<Student> queryWrapper1=new QueryWrapper<>();
                queryWrapper1.eq("id",userId)
                        .eq("password", MD5.encrypt(oldPwd));
                Student student = studentService.getOne(queryWrapper1);
                if(student!=null){
                    student.setPassword(MD5.encrypt(newPwd));
                    boolean b = studentService.saveOrUpdate(student);
                    return Result.ok();
                }else{
                    return Result.fail().message("原密码不存在");
                }
            case 3:
                QueryWrapper<Teacher> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId)
                        .eq("password", MD5.encrypt(oldPwd));
                Teacher teacher= teacherService.getOne(queryWrapper2);
                if(teacher!=null){
                    teacher.setPassword(MD5.encrypt(newPwd));
                    boolean b = teacherService.saveOrUpdate(teacher);
                    return Result.ok();
                }else{
                    return Result.fail().message("原密码不存在");
                }
        }
        return Result.fail().message("用户类型不存在");
    }
}
