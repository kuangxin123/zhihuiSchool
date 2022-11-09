package com.kx.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kx.myzhxy.mapper.ClazzMapper;
import com.kx.myzhxy.pojo.Clazz;
import com.kx.myzhxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @ClassName ClazzServiceImpl
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:14
 */
@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper,Clazz> implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Override
    public IPage<Clazz> getGrades(Page<Clazz> page, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        Page<Clazz> page1 = clazzMapper.selectPage(page, queryWrapper);
        return page1;
    }
}
