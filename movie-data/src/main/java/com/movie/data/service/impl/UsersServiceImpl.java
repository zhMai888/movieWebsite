package com.movie.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movie.data.mapper.UsersMapper;
import com.movie.data.domain.Users;
import com.movie.data.service.IUsersService;

/**
 * 用户表Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-17
 */
@Service
public class UsersServiceImpl implements IUsersService 
{
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 查询用户表
     * 
     * @param id 用户表主键
     * @return 用户表
     */
    @Override
    public Users selectUsersById(Long id)
    {
        return usersMapper.selectUsersById(id);
    }

    /**
     * 查询用户表列表
     * 
     * @param users 用户表
     * @return 用户表
     */
    @Override
    public List<Users> selectUsersList(Users users)
    {
        return usersMapper.selectUsersList(users);
    }

    /**
     * 新增用户表
     * 
     * @param users 用户表
     * @return 结果
     */
    @Override
    public int insertUsers(Users users)
    {
        return usersMapper.insertUsers(users);
    }

    /**
     * 修改用户表
     * 
     * @param users 用户表
     * @return 结果
     */
    @Override
    public int updateUsers(Users users)
    {
        return usersMapper.updateUsers(users);
    }

    /**
     * 批量删除用户表
     * 
     * @param ids 需要删除的用户表主键
     * @return 结果
     */
    @Override
    public int deleteUsersByIds(Long[] ids)
    {
        return usersMapper.deleteUsersByIds(ids);
    }

    /**
     * 删除用户表信息
     * 
     * @param id 用户表主键
     * @return 结果
     */
    @Override
    public int deleteUsersById(Long id)
    {
        return usersMapper.deleteUsersById(id);
    }
}
