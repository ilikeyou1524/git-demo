package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Category;
import com.itheima.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 0:08
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> getAll(int page, int pageSize) {

        log.info("page"+page+"pageSize"+pageSize);
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //添加排序条件
        lqw.orderByDesc(Category::getSort);
        //调用service的分页方法进行查询返回数据
        categoryService.page(pageInfo,lqw);

        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);
        return R.success("insert success !");
    }

    /**
     * 修改菜品分类
     * @param category
     * @return
     */
    @PutMapping
    public  R<String> putById(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("update yes!");
    }

    /**
     * 删除菜品分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(Long ids){

        Boolean remove = categoryService.remove(ids);
        if (remove) {

            return R.success("delete successfully");
        }else {

            return R.error("there is something wrong!");
        }
    }

    @GetMapping("/list")
    public R<List<Category>> get(Category category){

        //新建条件查询器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null,Category::getType,category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }

}
