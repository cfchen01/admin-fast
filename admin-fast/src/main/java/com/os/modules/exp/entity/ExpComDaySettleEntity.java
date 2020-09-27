package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 公司日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_com_day_settle")
public class ExpComDaySettleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 日常支出
	 */
	private Integer dailyExpenses;
	/**
	 * 运费收入
	 */
	private Integer freightIncome;
	/**
	 * 公司垫付
	 */
	private Integer comAdvance;
	/**
	 * 公司送货费
	 */
	private Integer comDelivery;
	/**
	 * 总支出
	 */
	private Integer totalExpenses;
	/**
	 * 总收入
	 */
	private Integer totalIncome;
	/**
	 * 盈利
	 */
	private Integer profit;
	/**
	 * 结算日期
	 */
	private LocalDate deliverDate;
	/**
	 * 结算月份
	 */
	private String deliverMonth;
	/**
	 * 结算人
	 */
	private Long userId;
	/**
	 * 结算状态--0、未结算，1、已结算
	 */
	private Integer status;

	/**
	 * 是否可以核算
	 */
	@TableField(exist = false)
	private Boolean canSubmit;

}
