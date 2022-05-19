/**
  * Copyright 2022 bejson.com 
  */
package com.xiaolanhe.gulimall.product.vo;

import lombok.Data;

/**
 * Auto-generated: 2022-05-16 19:57:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
public class Images {

    private String imgUrl;
    private int defaultImg;
    public void setImgUrl(String imgUrl) {
         this.imgUrl = imgUrl;
     }
     public String getImgUrl() {
         return imgUrl;
     }

    public void setDefaultImg(int defaultImg) {
         this.defaultImg = defaultImg;
     }
     public int getDefaultImg() {
         return defaultImg;
     }

}