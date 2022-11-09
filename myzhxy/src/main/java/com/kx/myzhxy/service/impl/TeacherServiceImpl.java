package com.kx.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kx.myzhxy.mapper.TeacherMapper;
import com.kx.myzhxy.pojo.LoginForm;
import com.kx.myzhxy.pojo.Teacher;
import com.kx.myzhxy.service.TeacherService;
import com.kx.myzhxy.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @ClassName TeacherServiceImpl
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:14
 */
@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper,Teacher> implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername())
                .eq("password",MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = teacherMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        Teacher teacher = teacherMapper.selectById(userId);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeachers(Page<Teacher> page, String name, String clazzName) {
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("clazz_name",clazzName);
        }
        queryWrapper.orderByDesc("id");
        Page<Teacher> page1 = teacherMapper.selectPage(page, queryWrapper);
        return page1;
    }
}
