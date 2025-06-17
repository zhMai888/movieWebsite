package com.movie.data.service;

import java.util.List;
import com.movie.data.domain.Users;

/**
 * 用户表Service接口
 * 
 * @author ruoyi
 * @date 2025-06-17
 */
public interface IUsersService 
{
    /**
     * 查询用户表
     * 
     * @param id 用户表主键
     * @return 用户表
     */
    public Users selectUsersById(Long id);

    /**
     * 查询用户表列表
     * 
     * @param users 用户表
     * @return 用户表集合
     */
    public List<Users> selectUsersList(Users users);

    /**
     * 新增用户表
     * 
     * @param users 用户表
     * @return 结果
     */
    public int insertUsers(Users users);

    /**
     * 修改用户表
     * 
     * @param users 用户表
     * @return 结果
     */
    public int updateUsers(Users users);

    /**
     * 批量删除用户表
     * 
     * @param ids 需要删除的用户表主键集合
     * @return 结果
     */
    public int deleteUsersByIds(Long[] ids);

    /**
     * 删除用户表信息
     * 
     * @param id 用户表主键
     * @return 结果
     */
    public int deleteUsersById(Long id);
}
