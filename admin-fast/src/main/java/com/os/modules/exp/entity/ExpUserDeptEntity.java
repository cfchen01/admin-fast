package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户网点关系表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-24 19:17:49
 */
@Data
@TableName("exp_user_dept")
public class ExpUserDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 网点ID
	 */
	private Integer deptId;

}
