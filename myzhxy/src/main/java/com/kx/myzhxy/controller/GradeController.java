package com.kx.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.myzhxy.pojo.Grade;
import com.kx.myzhxy.service.GradeService;
import com.kx.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GradeController
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 20:17
 */
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Autowired
    private GradeService gradeService;
//sms/gradeController/getGrades/1/3?gradeName=.....
    @GetMapping("/getGrades/{currentPage}/{bunch}")
    public Result getGrade(@ApiParam("通过年级进行查询学生") @RequestParam(value = "gradeName",required = false) String gradeName,
                           @ApiParam("当前页码") @PathVariable("currentPage")Integer currentPage,
                           @ApiParam("每页显示条数") @PathVariable("bunch")Integer bunch,
                            Model model){
            Page<Grade> page=new Page<>(currentPage,bunch);
            IPage<Grade> pageRs=gradeService.selectGrades(page,gradeName);
            return Result.ok(pageRs);
    }


    @PostMapping("/saveOrUpdateGrade")
    public Result saveGrade(@RequestBody Grade grade){
        boolean b = gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@RequestBody Integer[] id){
        boolean b = gradeService.removeByIds(Arrays.asList(id));
        return Result.ok();
    }

    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getBaseMapper().selectList(null);
        return Result.ok(grades);
    }
}
