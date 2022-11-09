package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.myzhxy.pojo.Student;
import com.kx.myzhxy.service.StudentService;
import com.kx.myzhxy.util.MD5;
import com.kx.myzhxy.util.Result;
import io.swagger.annotations.Api;
import org.reflections.vfs.Vfs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @ClassName StudentController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:18
 */
@Api("StudentController")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;

    //sms/studentController/getStudentByOpr/1/3
    @GetMapping("/getStudentByOpr/{currentPage}/{bunch}")
    public Result getStudent(@RequestParam(value = "clazzName",required = false) String clazzName,
                             @RequestParam(value = "name",required = false) String name,
                             @PathVariable("currentPage") Integer currentPage,
                             @PathVariable("bunch") Integer bunch){
        Page<Student> page=new Page<>(currentPage,bunch);
        IPage<Student> iPage=studentService.getClazzsByPage(page,clazzName,name);
        return Result.ok(iPage);
    }

    //sms/studentController/addOrUpdateStudent
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        if(student.getId()==null || student.getId()==0){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        boolean b = studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @DeleteMapping("delStudentById")
    public Result delStudent(@RequestBody Integer[] ids){
        boolean b = studentService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }

}
