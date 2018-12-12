package com.leyou.library.le_library.comm.grand.modle;

/**
 * 达观fields字段
 *
 * Created by ss on 2018/7/11.
 */
public class GrandFieldVo {
    public String userid;// 用户登陆后的id，可以是会员码或手机号
    public String imei;// 用户的手机imei号
    public String itemid;// 除了搜索行为之外，此字段不能为空, 对应item表的itemid，和产品数据（item)中产品的itemid保持一致
    public Integer action_num;// 一次性购买1个产品多次需上报,行为数量。对于购买行为，可以是购买数量。如果是同时提交/支付不同产品，可分开上报购买行为

    public String action_type;//
    public String scene_type;//

    public Long timestamp;// 行为发生的时间，unix时间戳，精确到秒，没有传则置为收到请求的时间
    public String rec_requestid;// 如果此条行为是由达观推荐带来的，则此字段对应调达观推荐接口返回itemid所带的request_id
    public String keyword;// action_type是search，需要上报该字段,表示用户所搜索的内容
}
