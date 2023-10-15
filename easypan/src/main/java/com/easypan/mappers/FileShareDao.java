package com.easypan.mappers;

import com.easypan.entity.FileShare;
import com.easypan.query.FileShareQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 分享信息
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-10-14
*/
@Mapper
public interface FileShareDao extends BaseDao<FileShare> {

    Integer deleteFileShareBatch(@Param("shareIdArray") String[] shareIdArray, @Param("userId") String userId);

    List<FileShare> selectList(FileShareQuery query);
}