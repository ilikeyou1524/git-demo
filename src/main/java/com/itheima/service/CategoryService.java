package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Category;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-06 0:05
 */
public interface CategoryService extends IService<Category> {
    Boolean remove(Long id);
}
