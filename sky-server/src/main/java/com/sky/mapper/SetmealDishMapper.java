package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对应的套餐数量
     * @param ids
     * @return
     */
    int countByDishId(List<Long> ids);
}
