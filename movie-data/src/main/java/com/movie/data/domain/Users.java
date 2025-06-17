package com.movie.data.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.movie.common.annotation.Excel;
import com.movie.common.core.domain.BaseEntity;

/**
 * 用户表对象 users
 * 
 * @author ruoyi
 * @date 2025-06-17
 */
public class Users extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 账户 */
    @Excel(name = "账户")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 手机 */
    @Excel(name = "手机")
    private String phone;

    /** 性别(0男1女) */
    @Excel(name = "性别(0男1女)")
    private Long gender;

    /** 是否为vip(0否1是) */
    @Excel(name = "是否为vip(0否1是)")
    private Long userType;

    /** 用户图片地址 */
    @Excel(name = "用户图片地址")
    private String userurl;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setGender(Long gender) 
    {
        this.gender = gender;
    }

    public Long getGender() 
    {
        return gender;
    }

    public void setUserType(Long userType) 
    {
        this.userType = userType;
    }

    public Long getUserType() 
    {
        return userType;
    }

    public void setUserurl(String userurl) 
    {
        this.userurl = userurl;
    }

    public String getUserurl() 
    {
        return userurl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("email", getEmail())
            .append("phone", getPhone())
            .append("gender", getGender())
            .append("userType", getUserType())
            .append("userurl", getUserurl())
            .toString();
    }
}
