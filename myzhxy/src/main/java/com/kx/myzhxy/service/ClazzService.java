package com.kx.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kx.myzhxy.pojo.Clazz;

public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getGrades(Page<Clazz> page, String gradeName, String name);
}
