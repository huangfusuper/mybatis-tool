package com.zhishu.samples.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private Integer id;
    private Integer code;
    private String msg;
    private String name;
    private String phone;
    private String templateId;
    private Date timestamp;

}
