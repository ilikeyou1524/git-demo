package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.dto.DishDto;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 1:11
 */
@Slf4j
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品的同时添加口味
     * @param dishDto
     */
    @Override
    public void saveFlavor(DishDto dishDto) {
        //保存dish
        this.save(dishDto);

        //保存口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(flavor->{
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        //dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(dlqw);
        DishDto dishDto = new DishDto();
        dishDto.setFlavors(list);
        BeanUtils.copyProperties(dish,dishDto);
        return dishDto;
    }

    @Override
    public void updateFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dlqw);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(flavor->{
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
        LambdaQueryWrapper<DishFlavor> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(dlqw);
    }
}
