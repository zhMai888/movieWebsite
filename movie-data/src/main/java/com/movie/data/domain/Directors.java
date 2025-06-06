package com.movie.data.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.movie.common.annotation.Excel;
import com.movie.common.core.domain.BaseEntity;

/**
 * 导演对象 directors
 * 
 * @author ruoyi
 * @date 2025-05-24
 */
public class Directors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 导演名字 */
    @Excel(name = "导演名字")
    private String name;

    /** 导演照片 */
    @Excel(name = "导演照片")
    private String photourl;

    /** 简介 */
    @Excel(name = "简介")
    private String resume;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setPhotourl(String photourl) 
    {
        this.photourl = photourl;
    }

    public String getPhotourl() 
    {
        return photourl;
    }

    public void setResume(String resume) 
    {
        this.resume = resume;
    }

    public String getResume() 
    {
        return resume;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("photourl", getPhotourl())
            .append("resume", getResume())
            .toString();
    }
}
