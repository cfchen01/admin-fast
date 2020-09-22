package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 网点日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_dep_day_settle")
public class ExpDepDaySettleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 公司月结表id
	 */
	private Integer expComDayId;
	/**
	 * 网点id
	 */
	private Integer deptId;
	/**
	 * 已付订单总费用
	 */
	private Integer paidMoney;
	/**
	 * 已付订单已收费用
	 */
	private Integer paidMoneyIn;
	/**
	 * 提货订单总费用
	 */
	private Integer arrivalMoney;
	/**
	 * 提货订单已收费用
	 */
	private Integer arrivalMoneyIn;
	/**
	 * 公司已收费用
	 */
	private Integer comMoneyIn;
	/**
	 * 发货日期(结算日期）
	 */
	private LocalDate deliverDate;
	/**
	 * 月结订单总费用
	 */
	private Integer monthMoney;
	/**
	 * 月结订单已收费用
	 */
	private Integer monthMoneyIn;
	/**
	 * 网点收入(运费)
	 */
	private Integer income;
	/**
	 * 结算人
	 */
	private Long userId;
	/**
	 * 结算状态--0、未结算，1、已结算
	 */
	private Integer status;

}
