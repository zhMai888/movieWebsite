package com.movie.data.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.movie.common.annotation.Log;
import com.movie.common.core.controller.BaseController;
import com.movie.common.core.domain.AjaxResult;
import com.movie.common.enums.BusinessType;
import com.movie.data.domain.Users;
import com.movie.data.service.IUsersService;
import com.movie.common.utils.poi.ExcelUtil;
import com.movie.common.core.page.TableDataInfo;

/**
 * 用户表Controller
 * 
 * @author ruoyi
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/users/users")
public class UsersController extends BaseController
{
    @Autowired
    private IUsersService usersService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询用户表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Users users)
    {
        startPage();
        List<Users> list = usersService.selectUsersList(users);
        return getDataTable(list);
    }

    /**
     * 导出用户表列表
     */
    @Log(title = "用户表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Users users)
    {
        List<Users> list = usersService.selectUsersList(users);
        ExcelUtil<Users> util = new ExcelUtil<Users>(Users.class);
        util.exportExcel(response, list, "用户表数据");
    }

    /**
     * 获取用户表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(usersService.selectUsersById(id));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Users Login(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        return usersService.login(username, password);
    }

    /**
     * 验证码校验
     */

    @PostMapping("/captcha")
    public int JudgeCaptcha(@RequestBody Map<String, String> data){
        String code = data.get("code");
        String uuid = data.get("uuid");
        if (uuid == null || uuid.isEmpty()) {
            return 0;
        }
        String key = "captcha_codes:" + uuid;
        String cacheCode = stringRedisTemplate.opsForValue().get(key);
        if (cacheCode == null) {
            return 2; // 验证码过期或不存在
        }
        if (cacheCode.startsWith("\"") && cacheCode.endsWith("\"")) {
            cacheCode = cacheCode.substring(1, cacheCode.length() - 1);
        }
        if (cacheCode.equalsIgnoreCase(code)) {
            stringRedisTemplate.delete(key);
            return 1; // 验证码正确
        } else {
            return 0; // 验证码错误
        }
    }



    /**
     * 新增用户表
     */
    @Log(title = "用户表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Users users)
    {
        return toAjax(usersService.insertUsers(users));
    }

    /**
     * 修改用户表
     */
    @Log(title = "用户表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Users users)
    {
        return toAjax(usersService.updateUsers(users));
    }

    /**
     * 删除用户表
     */
    @Log(title = "用户表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(usersService.deleteUsersByIds(ids));
    }
}
