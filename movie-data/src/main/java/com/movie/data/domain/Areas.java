package com.movie.data.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.movie.common.annotation.Excel;
import com.movie.common.core.domain.BaseEntity;

/**
 * 地区对象 areas
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public class Areas extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 地区名 */
    @Excel(name = "地区名")
    private String areaname;

    /** 地区电影数 */
    @Excel(name = "地区电影数")
    private Long novienum;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAreaname(String areaname) 
    {
        this.areaname = areaname;
    }

    public String getAreaname() 
    {
        return areaname;
    }

    public void setNovienum(Long novienum) 
    {
        this.novienum = novienum;
    }

    public Long getNovienum() 
    {
        return novienum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("areaname", getAreaname())
            .append("novienum", getNovienum())
            .toString();
    }
}
