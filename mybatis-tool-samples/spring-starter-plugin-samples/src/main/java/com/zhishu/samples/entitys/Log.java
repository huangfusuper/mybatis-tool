package com.zhishu.samples.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Serializable {
    private static final long serialVersionUID = 8691311924028729506L;
    private Integer id;
    private Integer code;
    private String msg;
    private String name;
    private String phone;
    private String templateId;
    private Date timestamp;

}
