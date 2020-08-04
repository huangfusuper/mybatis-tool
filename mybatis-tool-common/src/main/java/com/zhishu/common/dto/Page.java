package com.zhishu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据载体
 * @author huangfu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -3395369982924864702L;
    /**
     * 页码 默认第一页
     */
    private int pageNo = 1;
    /**
     * 每页显示的记录数，默认是15
     */
    private int pageSize = 15;
    /**
     * 总记录数
     */
    private int totalRecord;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 对应的当前页记录
     */
    private List<T> results;

    /**
     * 计算总页数
     * @param totalRecord 总条数
     */
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        // 在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }
}
