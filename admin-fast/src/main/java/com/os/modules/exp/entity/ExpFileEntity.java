package com.os.modules.exp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件信息表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@Data
@TableName("exp_file")
public class ExpFileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 存储路径
	 */
	private String path;
	/**
	 * 访问路径
	 */
	private String url;
	/**
	 * 描述
	 */
	private String name;
	/**
	 * 上传用户id
	 */
	private Long userId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
