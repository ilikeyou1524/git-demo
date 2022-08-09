package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.DishDto;
import com.itheima.entity.Dish;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 1:10
 */
public interface DishService extends IService<Dish> {
    //新增方法,同时插入菜品以及菜品的口味数据
    void saveFlavor(DishDto dishDto);

    //得到口味数据以及菜品数据
    DishDto getFlavor(Long id);

    //新增方法,同时插入菜品以及菜品的口味数据
    void updateFlavor(DishDto dishDto);

    //删除相关口味以及菜品
    void delete(Long id);
}
