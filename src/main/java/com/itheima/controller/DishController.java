package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.entity.Employee;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 17:19
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;




    //分页查询
    @GetMapping("/page")
    public R<Page> getAll(int page, int pageSize, @Nullable String name) {

        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //添加排序条件
        lqw.orderByDesc(Dish::getUpdateTime);
        //调用service的分页方法进行查询返回数据
        dishService.page(pageInfo, lqw);
        List<Dish> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<DishDto> dishDtos = records.stream().map(record ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);
            Long categoryId = record.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            String name1 = byId.getName();
            dishDto.setCategoryName(name1);
            return dishDto;

        }).collect(Collectors.toList());
        //for (Dish record : records) {
        //    DishDto dishDto = new DishDto();
        //    BeanUtils.copyProperties(record,dishDto);
        //    Long categoryId = record.getCategoryId();
        //    Category byId = categoryService.getById(categoryId);
        //    String name1 = byId.getName();
        //    dishDto.setCategoryName(name1);
        //}
        //BeanUtils.copyProperties(records,dishDtos);
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }

    /**
     * 新增数据 口味加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
       dishService.saveFlavor(dishDto);
        return R.success("save successfully!");
    }
    //进行回显
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        //根据id查询
        DishDto flavor = dishService.getFlavor(id);

        return R.success(flavor);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateFlavor(dishDto);
        return R.success("update successfully !");
    }

    @PostMapping("/status/{status}")
    public R<String> pause(@PathVariable Integer status, String ids){
        String[] split = ids.split(",");
        for (String s : split) {
           Long I = Long.parseLong(s);
            Dish byId = dishService.getById(s);
            byId.setStatus(status);
            dishService.updateById(byId);
        }
        return R.success("yes");
    }
    @DeleteMapping
    public R<String> delete(String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            Long I = Long.parseLong(s);
            dishService.delete(I);
        }
        return R.success("yes!");
    }
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //条件查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        //排个序
        dishLambdaQueryWrapper.orderByDesc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return R.success(list);
    }

}
