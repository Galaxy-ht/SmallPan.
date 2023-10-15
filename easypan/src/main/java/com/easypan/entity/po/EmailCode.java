package com.easypan.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 邮箱验证码
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("email_code")
public class EmailCode {
	/**
	* 邮箱
	*/
	@TableId("email")
	private String email;

	/**
	* 验证码
	*/
	private String code;

	/**
	* 创建时间
	*/
	private Date creatTime;

	/**
	* 0:未使用 1:已使用
	*/
	private Integer status;

}