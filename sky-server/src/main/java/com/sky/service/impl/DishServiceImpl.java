package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);//属性拷贝
        dishMapper.save(dish);//向菜品表插入一条数据

        Long dishId = dish.getId();//获取当前菜品的id(由于前端此时插入数据时，菜品还没有插入，也就没有id，所以dishId为null，所以这里自己获取一下)

        if (dishDTO.getFlavors() == null || dishDTO.getFlavors().size() == 0){
            return;
        }//菜品没有口味，不需要处理
        for (DishFlavor dishFlavor: dishDTO.getFlavors()){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorMapper.save(dishDTO.getFlavors());//向口味表插入n条数据
    }
}
