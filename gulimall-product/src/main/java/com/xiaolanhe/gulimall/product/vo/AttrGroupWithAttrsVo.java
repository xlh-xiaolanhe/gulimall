package com.xiaolanhe.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xiaolanhe.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/16 19:17
 */

@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
