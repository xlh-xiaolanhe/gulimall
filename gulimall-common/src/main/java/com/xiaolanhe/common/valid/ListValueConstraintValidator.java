package com.xiaolanhe.common.valid;

import sun.java2d.pipe.AAShapePipe;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/8 19:23
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    

    /**
     * desciption 判断是否检验成功
      * @param integer 需要校验的值
     * @param context 校验的上下文环境信息
     * @return boolean
     * @Date 2022/5/8 19:47
    */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext context) {
        return set.contains(integer);
    }


    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.vals();
        for(int cur : values)
        {
            set.add(cur);
        }
    }
}
