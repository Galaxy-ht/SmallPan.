package com.easypan.entity.vo;

//import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.easypan.utils.DateUtils;
import java.util.Date;

/**
* 用户CRUD
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-16
*/
@Data
public class UserInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Schema(description = "用户ID")
	private String userId;

//	@Schema(description = "用户昵称")
	private String nickName;

//	@Schema(description = "用户邮箱")
	private String email;

//	@Schema(description = "qqOpenID")
	private Integer qqOpenId;

//	@Schema(description = "qq头像")
	private String qqAvatar;

//	@Schema(description = "用户密码")
	private String password;

//	@Schema(description = "注册时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date joinTime;

//	@Schema(description = "最后登录时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date lastLoginTime;

//	@Schema(description = "用户状态(0禁用)")
	private Integer status;

//	@Schema(description = "使用空间")
	private Long useSpace;

//	@Schema(description = "总空间")
	private Long totalSpace;


}