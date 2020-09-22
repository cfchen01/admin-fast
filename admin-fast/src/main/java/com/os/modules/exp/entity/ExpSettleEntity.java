package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 结算方式
 * 
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_settle")
public class ExpSettleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 结算方式名称
	 */
	private String settleName;
	/**
	 * 结算代码
	 */
	private String settleCode;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
