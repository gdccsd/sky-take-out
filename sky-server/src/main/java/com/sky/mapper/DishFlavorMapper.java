package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

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
}
