package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 订单表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_order")
public class ExpOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 网点id
	 */
	private Integer deptId;
	/**
	 * 订单码
	 */
	private String ordCode;
	/**
	 * 货物id
	 */
	private Integer goodsId;
	/**
	 * 货物件数
	 */
	private Integer ordNum;
	/**
	 * 包装id
	 */
	private Integer packingId;
	/**
	 * 运费
	 */
	private Integer freight;
	/**
	 * 垫付费
	 */
	private Integer advance;
	/**
	 * 已收垫付费
	 */
	private Integer advanceIn;
	/**
	 * 结算方式代码
	 */
	private String settleCode;

	/**
	 * 结算方式id
	 */
	private Integer settleId;
	/**
	 * 送货费
	 */
	private Integer delivery;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 开单员
	 */
	private Long userId;
	/**
	 * 托运方
	 */
	private String shipper;
	/**
	 * 托运方电话
	 */
	private String shipperTel;
	/**
	 * 收货方
	 */
	private String receiver;
	/**
	 * 收货方电话
	 */
	private String receiverTel;
	/**
	 * 订单状态--0、未确认，1、已确认，2、返单
	 */
	private Integer status;
	/**
	 * 发货日期
	 */
	private LocalDate deliverDate;
	/**
	 * 创建时间
	 */
	private Date createTime;

	@TableField(exist = false)
	private List<ExpFileEntity> fileList;

	/**
	 * 货物名称
	 */
	private String goodsName;
	/**
	 * 网点名称
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * 结算方式名称
	 */
	@TableField(exist = false)
	private String settleName;
	/**
	 * 包装名称
	 */
	@TableField(exist = false)
	private String packingName;
	/**
	 * 开单员
	 */
	@TableField(exist = false)
	private String realname;

}
