package com.easypan.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 分享信息
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-10-14
*/
@Data
//@Schema(description = "分享信息")
public class FileShareVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String shareId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date shareTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expireTime;

	private String nickName;

	private String fileName;

	private Boolean currentUser;

	private String fileId;

	private String avatar;

	private String userId;

	private String code;
}