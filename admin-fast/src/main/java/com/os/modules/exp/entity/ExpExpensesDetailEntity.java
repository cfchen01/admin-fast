package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 公司支出明细表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_expenses_detail")
public class ExpExpensesDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 支出金额
	 */
	private Integer money;
	/**
	 * 操作人
	 */
	private Long userId;
	/**
	 * 支出日期
	 */
	private LocalDate createDate;
	/**
	 * 操作时间
	 */
	private Date createTime;

}
