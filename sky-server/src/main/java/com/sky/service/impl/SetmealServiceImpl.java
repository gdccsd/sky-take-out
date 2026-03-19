package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        //将套餐信息保存到套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);
        //将套餐中关联的菜品保存到套餐菜品关系表
        Long setmealId = setmeal.getId();//获取当前套餐的id(由于前端此时插入数据时，套餐还没有插入，也就没有id，所以setmealId为null，所以这里自己获取一下)
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.save(setmealDishes);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //判断套餐是否启用中
        if (setmealMapper.getStatusCount(ids) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //删除套餐
        setmealMapper.deleteBatchBySetmealId(ids);
        //删除套餐菜品关联
        setmealDishMapper.deleteBatchBySetmealId(ids);

    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = setmealMapper.getById(id);
        setmealVO.setSetmealDishes(setmealDishMapper.getBySetmealId(id));
        return setmealVO;
    }
}
