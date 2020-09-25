

package com.os.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.MapUtils;
import com.os.modules.sys.dao.SysUserRoleDao;
import com.os.modules.sys.entity.SysUserRoleEntity;
import com.os.modules.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

	@Override
	public void saveOrUpdate(Long userId, Long roleId) {
		//先删除用户与角色关系
		this.removeByMap(new MapUtils().put("user_id", userId));

		if(roleId == null){
			return ;
		}

		//保存用户与角色关系
		SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
		sysUserRoleEntity.setUserId(userId);
		sysUserRoleEntity.setRoleId(roleId);

		this.save(sysUserRoleEntity);
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return baseMapper.queryRoleIdList(userId);
	}

	@Override
	public int deleteBatch(Long[] roleIds){
		return baseMapper.deleteBatch(roleIds);
	}
}
