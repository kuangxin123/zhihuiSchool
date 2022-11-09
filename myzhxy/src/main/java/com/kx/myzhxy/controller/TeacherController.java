package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.myzhxy.pojo.Teacher;
import com.kx.myzhxy.service.TeacherService;
import com.kx.myzhxy.util.MD5;
import com.kx.myzhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @ClassName TeacherController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:18
 */
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    //8080/sms/teacherController/getTeachers/1/3?name=%E5%8C%A1%E9%91%AB&clazzName=%E4%B8%8

    @GetMapping("getTeachers/{currentPage}/{bunch}")
    public Result getTeachers(@RequestParam(value = "name",required = false)String name,
                              @RequestParam(value = "clazzName",required = false) String clazzName,
                              @PathVariable("currentPage")Integer currentPage,
                              @PathVariable("bunch") Integer bunch){
        Page<Teacher> page=new Page<>(currentPage,bunch);
        IPage<Teacher> iPage=teacherService.getTeachers(page,name,clazzName);
        return Result.ok(iPage);
    }

    ///sms/teacherController/saveOrUpdateTeacher
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        if(teacher.getId()==null||teacher.getId()==0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        boolean b = teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @DeleteMapping("/deleteTeacher")
    public Result delTeacher(@RequestBody Integer[] ids){
        boolean b = teacherService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }
}
