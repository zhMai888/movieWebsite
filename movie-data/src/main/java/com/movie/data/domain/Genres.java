package com.movie.data.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.movie.common.annotation.Excel;
import com.movie.common.core.domain.BaseEntity;

/**
 * 类别表对象 genres
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public class Genres extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 电影类别 */
    @Excel(name = "电影类别")
    private String type;

    /** 类别数量 */
    @Excel(name = "类别数量")
    private Long nums;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setNums(Long nums) 
    {
        this.nums = nums;
    }

    public Long getNums() 
    {
        return nums;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("nums", getNums())
            .toString();
    }
}
