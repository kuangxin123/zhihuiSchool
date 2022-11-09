package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.myzhxy.pojo.Admin;
import com.kx.myzhxy.service.AdminService;
import com.kx.myzhxy.util.MD5;
import com.kx.myzhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @ClassName AdminController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:17
 */
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;

    ///sms/adminController/getAllAdmin/1/3?adminName=%E5%8C%A1%E9%91%AB
    @GetMapping("/getAllAdmin/{currentPage}/{bunch}")
    public Result getAllAdmin(@RequestParam(value = "adminName",required = false) String adminName,
                              @PathVariable("currentPage") Integer currentPage,
                              @PathVariable("bunch") Integer bunch){
        Page<Admin> page=new Page<>(currentPage,bunch);
        IPage<Admin> iPage = adminService.getAdmin(page,adminName);
        return Result.ok(iPage);
    }

    ///sms/adminController/saveOrUpdateAdmin
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        if(admin.getId()==null||admin.getId()==0){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        boolean b = adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    //sms/adminController/deleteAdmin
    //请求方法: DELETE
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody Integer[] ids){
        boolean b = adminService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }
}
