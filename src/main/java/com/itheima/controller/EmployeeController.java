package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-04 15:09
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录(密码进行了MD5加密)
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //将密码进行MD5解密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        lqw.eq(Employee::getUsername, employee.getUsername());
        //返回uername的结果集
        Employee one = employeeService.getOne(lqw);
        //进行密码判断
        if (one == null) {
            return R.error("account not exist!");
        }
        if (!(one.getPassword().equals(password))) {
            return R.error("password error !");
        }
        if (one.getStatus() == 0) {
            return R.error("account disabled !");
        }
        //将员工id存入session中并返回登录结果既查出的one
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("logout success !");
    }

    @PostMapping
    public R<String> insert(@RequestBody Employee employee, HttpServletRequest request) {
        //设置初始密码
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        ////设置创建时间
        //employee.setCreateTime(LocalDateTime.now());
        ////设置修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //得到当前用户登录id
        //Long employeeId = (Long) request.getSession().getAttribute("employee");
        //设置创建人和修改人
        //employee.setCreateUser(employeeId);
        //employee.setUpdateUser(employeeId);
        //判断数据库中是否已经存在该用户
        //LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //lqw.eq(Employee::getUsername,employee.getUsername());
        //Employee one = employeeService.getOne(lqw);
        //if (one == null) {
        employeeService.save(employee);
        return R.success("insert success !");
        //}else {
        //    return R.error("account exist !");
        //}
    }

    @GetMapping("/page")
    public R<Page> getAll(int page, int pageSize, @Nullable String name) {

        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);
        //调用service的分页方法进行查询返回数据
        employeeService.page(pageInfo, lqw);

        return R.success(pageInfo);
    }

    /**
     * 修改员工的帐号状态
     * @return
     */
    @PutMapping
    public R<String> put(HttpServletRequest request, @RequestBody Employee emp) {

        //获取当前登录帐号的权限信息
        Long empId = (Long) request.getSession().getAttribute("employee");
        //调用service查询
        Employee emp1 = employeeService.getById(empId);
        //判断emp的权限
        if (emp1.getUsername().equals("admin")) {
            //当前用户是管理员采用修改操作
            //更新emp的更新信息
            emp.setUpdateUser(empId);
            emp.setUpdateTime(LocalDateTime.now());
            employeeService.updateById(emp);
            return R.success("update successfully !");
        }
        return R.error("update failed");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id) {
        //根据id查询
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }
}
