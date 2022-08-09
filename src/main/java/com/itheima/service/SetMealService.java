package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 1:11
 */
public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
}
