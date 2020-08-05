package com.zhishu.samples.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测试实体
 * @author huangfu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity implements Serializable {
    private static final long serialVersionUID = -5691865050833850863L;
    private Integer id;
    private String name;
    private Integer sal;
    private Integer deptid;
}
