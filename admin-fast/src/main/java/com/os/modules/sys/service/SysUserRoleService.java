

package com.os.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.modules.sys.entity.SysUserRoleEntity;

import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

	void saveOrUpdate(Long userId, Long roleId);

	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
