package com.leyou.library.le_library.comm.grand.modle.response;

import java.util.List;

/**
 * 达观response
 *
 * Created by ss on 2018/7/10.
 */
public class GrandResponse {
    public String status;//	string	执行结果，OK为成功。FAIL为失败，数据不会入库。WARN为有部分非重要字段异常，数据会正常入库，但请根据返回错误信息进行排查。
    public long request_id;//	string	该条上报记录的序号，仅用于排查问题使用
    public List<Error> errors;//	string	错误信息

    public class Error {
        public int code;
        public String message;
    }
}
