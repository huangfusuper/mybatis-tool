package com.zhishu.samples.mapper;

import com.zhishu.common.dto.Page;
import com.zhishu.samples.entitys.Log;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogMapper {

    List<Log> findAll(Page<Log> page);
}
