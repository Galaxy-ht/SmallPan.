package com.easypan.entity.vo;

//import io.swagger.v3.oas.annotations.media.Schema;

import com.easypan.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 邮箱验证码
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-19
*/
@Data
//@Schema(description = "邮箱验证码")
public class EmailCodeVO implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Schema(description = "邮箱")
	private String email;

//	@Schema(description = "验证码")
	private String code;

//	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date creatTime;

//	@Schema(description = "0:未使用 1:已使用")
	private Integer status;


}