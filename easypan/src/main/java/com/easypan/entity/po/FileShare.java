package com.easypan.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 分享信息
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-10-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("file_share")
public class FileShare {
	/**
	* 分享ID
	*/
	@TableId
	private String shareId;

	/**
	* 文件ID
	*/
	private String fileId;

	/**
	* 用户ID
	*/
	private String userId;

	/**
	* 有效期类型 0：1天 1：7天 2：30天 3：永久有效
	*/
	private Integer validType;

	/**
	* 失效时间
	*/
	private Date expireTime;

	/**
	* 分享时间
	*/
	private Date shareTime;

	/**
	* 提取码
	*/
	private String code;

	/**
	* 浏览次数
	*/
	private Integer showCount;

}