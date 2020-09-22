package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 货物表
 * 
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_goods")
public class ExpGoodsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 货物名称
	 */
	private String goodsName;
	/**
	 * 上级id
	 */
	private Integer superGoodsId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
