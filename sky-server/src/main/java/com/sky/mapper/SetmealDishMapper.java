package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
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

    /**
     * 批量插入套餐和菜品的关联数据
     * @param setmealDishes
     */
    @AutoFill(OperationType.INSERT)
    void save(List<SetmealDish> setmealDishes);

    /**
     * 批量删除套餐和菜品的关联数据
     * @param ids
     */
    void deleteBatchBySetmealId(List<Long> ids);

    /**
     * 根据套餐id查询套餐和菜品的关联数据
     * @param id
     * @return
     */
    @Select("select id, setmeal_id, dish_id, name, price, copies from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);
}
