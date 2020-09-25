

package com.os.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.exception.RRException;
import com.os.common.utils.Constant;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;
import com.os.modules.exp.service.ExpUserDeptService;
import com.os.modules.sys.dao.SysUserDao;
import com.os.modules.sys.entity.SysUserEntity;
import com.os.modules.sys.service.SysRoleService;
import com.os.modules.sys.service.SysUserRoleService;
import com.os.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private ExpUserDeptService expUserDeptService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");

		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.or().like(StringUtils.isNotBlank(username), "realname", username)
		);

		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.save(user);

		expUserDeptService.saveOrUpdate(user.getUserId(), user.getDeptId());

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleId());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);

		expUserDeptService.saveOrUpdate(user.getUserId(), user.getDeptId());

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleId());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleId() == null){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}

		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.contains(user.getRoleId())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}
}
