package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 业务员提货单费用表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Data
@TableName("exp_user_money")
public class ExpUserMoneyEntity implements Serializable {
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
	 * 网点id
	 */
	private Integer deptId;
	/**
	 * 日结id
	 */
	private Integer daySettleId;
	/**
	 * 业务员应交总费用
	 */
	private Integer moneyAll;
	/**
	 * 业务员已提交费用
	 */
	private Integer moneyIn;
	/**
	 * 结算日期
	 */
	private LocalDate deliverDate;

}
