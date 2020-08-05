package com.zhishu.samples.mappers;

import com.zhishu.common.dto.Page;
import com.zhishu.samples.entitys.TestEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试查询
 * @author huangfu
 */
public interface TestMapper {

    /**
     * 查询全部
     * @param page 分页参数
     * @return 返回数据
     */
    List<TestEntity> findAll(Page<TestEntity> page);

    List<TestEntity> findAllById(Page<TestEntity> page, @Param("id") Integer id);
}
