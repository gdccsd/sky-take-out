package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);//属性拷贝
        dishMapper.save(dish);//向菜品表插入一条数据

        Long dishId = dish.getId();//获取当前菜品的id(由于前端此时插入数据时，菜品还没有插入，也就没有id，所以dishId为null，所以这里自己获取一下)

        if (dishDTO.getFlavors() == null || dishDTO.getFlavors().size() == 0) {
            return;
        }//菜品没有口味，不需要处理
        for (DishFlavor dishFlavor : dishDTO.getFlavors()) {
            dishFlavor.setDishId(dishId);
        }
        dishFlavorMapper.save(dishDTO.getFlavors());//向口味表插入n条数据
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());//设置分页参数
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        //判断是否起售中
        if (dishMapper.countByStatus(ids) > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //判断是否存在套餐与之关联
        if (setmealDishMapper.countByDishId(ids) > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
        dishMapper.deleteByIds(ids);
        //删除菜品对应的口味数据
        dishFlavorMapper.deleteByDishId(ids);
    }
}
