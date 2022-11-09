package com.kx.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kx.myzhxy.mapper.AdminMapper;
import com.kx.myzhxy.pojo.Admin;
import com.kx.myzhxy.pojo.LoginForm;
import com.kx.myzhxy.service.AdminService;
import com.kx.myzhxy.util.MD5;
import com.kx.myzhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @ClassName AdminServiceImpl
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:14
 */

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Admin login(LoginForm loginForm) {
      QueryWrapper<Admin> queryWrapper = new QueryWrapper();
      queryWrapper.eq("name", loginForm.getUsername())
                      .eq("password",MD5.encrypt(loginForm.getPassword()));
        System.out.println(loginForm.getPassword());
        System.out.println(MD5.encrypt(loginForm.getPassword()));
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        Admin admin = adminMapper.selectById(userId);
        return admin;
    }

    @Override
    public IPage<Admin> getAdmin(Page<Admin> page, String adminName) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(adminName)){
            queryWrapper.like("name",adminName);
        }
        queryWrapper.orderByDesc("id");
        Page<Admin> page1 = adminMapper.selectPage(page, queryWrapper);
        return page1;
    }


}
