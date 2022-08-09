package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.CustomerException;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.Setmeal;
import com.itheima.mapper.CategoryMapper;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import com.itheima.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 0:06
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    @Override
    public Boolean remove(Long id) {
        //查询条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId,id);
        LambdaQueryWrapper<Setmeal> slqw = new LambdaQueryWrapper<>();
        slqw.eq(Setmeal::getCategoryId,id);

        //查询该id下是否关联菜品
        if (dishService.count(lqw) > 0){
            //抛出异常
            throw new CustomerException("this category has bind some dishs");

        }
        //查询该id下是否关联分类
        if (setMealService.count(slqw) > 0){
            throw new CustomerException("this category has bind some setmeals");

        }

        super.removeById(id);
        return true;
    }
}
