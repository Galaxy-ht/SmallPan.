package com.easypan.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 文件信息表
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("file_info")
public class FileInfo {
	/**
	* 文件Id
	*/
	@TableId("file_id")
	private String fileId;

	/**
	* 用户Id
	*/
	private String userId;

	/**
	* 文件MD5值
	*/
	private String fileMd5;

	/**
	* 父级Id
	*/
	private String filePid;

	/**
	* 文件大小
	*/
	private Long fileSize;

	/**
	* 文件名
	*/
	private String fileName;

	/**
	* 文件封面
	*/
	private String fileCover;

	/**
	* 文件路径
	*/
	private String filePath;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	* 最后更新时间
	*/
	private Date lastUpdateTime;

	/**
	* 0:文件 1:目录
	*/
	private Integer folderType;

	/**
	* 文件分类 1:视频 2:音频 3:图片 4:文档 5:其他
	*/
	private Integer fileCategory;

	/**
	* 1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他
	*/
	private Integer fileType;

	/**
	* 0:转码中 1:转码失败 2:转码成功
	*/
	private Integer status;

	/**
	* 进入回收站时间
	*/
	private Date recoveryTime;

	/**
	* 0:删除 1:回收站 2:正常
	*/
	private Integer delFlag;

}