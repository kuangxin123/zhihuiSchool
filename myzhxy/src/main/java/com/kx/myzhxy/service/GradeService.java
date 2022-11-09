package com.kx.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kx.myzhxy.pojo.Grade;

public interface GradeService extends IService<Grade> {
    IPage<Grade> selectGrades(Page<Grade> page, String gradeName);
}
