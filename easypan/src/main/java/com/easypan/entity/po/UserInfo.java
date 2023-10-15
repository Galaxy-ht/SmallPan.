package com.easypan.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 用户CRUD
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("user_info")
public class UserInfo {
	/**
	* 用户ID
	*/
	@TableId("user_id")
	private String userId;

	/**
	* 用户昵称
	*/
	private String nickName;

	/**
	* 用户邮箱
	*/
	private String email;

	/**
	* qqOpenID
	*/
	private Integer qqOpenId;

	/**
	* qq头像
	*/
	private String qqAvatar;

	/**
	* 用户密码
	*/
	private String password;

	/**
	* 注册时间
	*/
	private Date joinTime;

	/**
	* 最后登录时间
	*/
	private Date lastLoginTime;

	/**
	* 用户状态(0禁用)
	*/
	private Integer status;

	/**
	* 使用空间
	*/
	private Long useSpace;

	/**
	* 总空间
	*/
	private Long totalSpace;

}