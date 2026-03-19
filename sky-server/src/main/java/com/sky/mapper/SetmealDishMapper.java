package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对应的套餐数量
     * @param ids
     * @return
     */
    int countByDishId(List<Long> ids);

    /**
     * 根据菜品id查询套餐id
     * @param id
     * @return
     */
    @Select("select setmeal_id from setmeal_dish where dish_id = #{id} group by id")
    List<Long> getIdsByDishId(Long id);
}
