package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-04 15:06
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
