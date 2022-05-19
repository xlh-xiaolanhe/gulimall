package com.xiaolanhe.common.constant;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/19 23:22
 */

public class WareConstant {

    public enum PurchaseStatusEnum{
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"), FINISH(3, "已完成"),
        HASERROR(4, "有异常");

        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum PurchaseDetailEnum{
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        RECEIVE(2, "正在采购"), FINISH(3, "已完成"),
        HASERROR(4, "采购失败");

        private int code;
        private String msg;

        PurchaseDetailEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
