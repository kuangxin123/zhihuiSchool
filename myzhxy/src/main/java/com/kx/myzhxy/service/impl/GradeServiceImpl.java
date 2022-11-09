package com.kx.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kx.myzhxy.mapper.GradeMapper;
import com.kx.myzhxy.pojo.Grade;
import com.kx.myzhxy.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @ClassName GradeServiceImpl
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:14
 */
@Service
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper,Grade> implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Override
    public IPage<Grade> selectGrades(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName)
                    .orderByAsc("id");
            Page<Grade> page1 = gradeMapper.selectPage(page, queryWrapper);
            return page1;
        }else{
            queryWrapper.orderByAsc("id");
            Page<Grade> page1 = gradeMapper.selectPage(page, queryWrapper);
            return page1;
        }
    }
}
