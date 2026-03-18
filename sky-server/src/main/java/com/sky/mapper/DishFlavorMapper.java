package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增口味
     * @param flavors
     */
    @AutoFill(OperationType.INSERT)
    void save(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据
     * @param ids
     */
    void deleteByDishId(List<Long> ids);

    /**
     * 根据菜品id查询对应的口味数据
     * @param id
     * @return
     */
    @Select("select id, dish_id, name, value from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
