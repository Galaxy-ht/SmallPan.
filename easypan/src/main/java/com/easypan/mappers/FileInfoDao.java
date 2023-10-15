package com.easypan.mappers;

import com.easypan.entity.po.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 文件信息表
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-25
*/
@Mapper
public interface FileInfoDao extends BaseDao<FileInfo> {

	Long selectUseSpace(@Param("userId") String userId);

    void updateFileDelFlagBatch(@Param("bean") FileInfo fileInfo,
                                @Param("userId") String userId,
                                @Param("filePidList") List<String> filePidList,
                                @Param("fileIdList") List<String> fileIdList,
                                @Param("oldDelFlag") Integer oldDelFlag);

    Integer updateByFileIdAndUserId(@Param("bean") FileInfo t, @Param("fileId") String fileId, @Param("userId") String userId);

    void delFileBatch(@Param("userId") String userId,
                      @Param("filePidList") List<String> filePidList,
                      @Param("fileIdList") List<String> fileIdList,
                      @Param("oldDelFlag") Integer oldDelFlag);
}