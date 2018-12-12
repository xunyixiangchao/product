package com.leyou.library.le_library.model;

/**
 * 闪送运费说明
 *
 * Created by ss on 2018/10/24.
 */
public class FlashPostageVo {
    public String freight_value;
    public String freight_name;

    public FlashPostageVo(String name, String value) {
        this.freight_value = name;
        this.freight_name = value;
    }
}
