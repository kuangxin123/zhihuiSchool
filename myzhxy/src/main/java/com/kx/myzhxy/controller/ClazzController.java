package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.myzhxy.pojo.Clazz;
import com.kx.myzhxy.service.ClazzService;
import com.kx.myzhxy.util.Result;
import org.omg.PortableServer.POA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ClazzController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:17
 */
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    ///sms/clazzController/getClazzsByOpr/1/3
    @Autowired
    private ClazzService clazzService;
    //getClazzByOpr/1/3?gradeName=&name=
    @GetMapping("/getClazzsByOpr/{currentPage}/{bunchs}")
    public Result getClazz(@RequestParam(value = "gradeName",required = false) String gradeName,
                           @RequestParam(value = "name",required = false) String name,
                           @PathVariable("currentPage") Integer currentPage,
                           @PathVariable("bunchs")Integer bunchs){
        Page<Clazz> page=new Page<>(currentPage,bunchs);
        IPage<Clazz> page1=clazzService.getGrades(page,gradeName,name);
        return Result.ok(page1);
    }

    @PostMapping("/saveOrUpdateClazz")
    public Result saveClazz(@RequestBody Clazz clazz){
        boolean b = clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @DeleteMapping("/deleteClazz")
        public Result deleteClazz(@RequestBody Integer[] id){
        boolean b = clazzService.removeByIds(Arrays.asList(id));
        return Result.ok();
    }

    @GetMapping("/getClazzs")
    public Result getClazzs(){
        QueryWrapper<Clazz> queryWrapper=new QueryWrapper<>();
        List<Clazz> clazzes = clazzService.getBaseMapper().selectList(queryWrapper);
        return Result.ok(clazzes);
    }
}
