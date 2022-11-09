package com.kx.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kx.myzhxy.mapper.StudentMapper;
import com.kx.myzhxy.pojo.LoginForm;
import com.kx.myzhxy.pojo.Student;
import com.kx.myzhxy.service.StudentService;
import com.kx.myzhxy.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @ClassName StudentServiceImpl
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:14
 */
@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername())
                .eq("password",MD5.encrypt(loginForm.getPassword()));
        Student student = studentMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        Student student = studentMapper.selectById(userId);
        return student;
    }

    @Override
    public IPage<Student> getClazzsByPage(Page<Student> page, String clazzName, String name) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("clazz_name",clazzName);
        }
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        Page<Student> page1 = studentMapper.selectPage(page, queryWrapper);
        return page1;
    }
}
