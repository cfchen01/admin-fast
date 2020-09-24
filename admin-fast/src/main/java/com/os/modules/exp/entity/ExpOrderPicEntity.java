package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单图片表
 * 
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@Data
@TableName("exp_order_pic")
public class ExpOrderPicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 订单id
	 */
	private Integer orderId;
	/**
	 * 文件id
	 */
	private Integer fileId;

}
