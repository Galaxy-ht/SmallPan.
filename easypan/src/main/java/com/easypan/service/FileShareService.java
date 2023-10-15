package com.easypan.service;

import com.easypan.entity.FileShare;
import com.easypan.entity.dto.SessionShareDto;
import com.easypan.entity.vo.FileShareVO;
import com.easypan.page.PageResult;
import com.easypan.query.FileShareQuery;

import java.util.List;

/**
 * 分享信息
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-10-14
 */
public interface FileShareService extends BaseService<FileShare> {

    PageResult<FileShareVO> page(FileShareQuery query);

    void save(FileShareVO vo);

    void update(FileShareVO vo);

    void delete(List<Long> idList);

    void deleteFileShareBatch(String[] shareIdArray, String userId);

    SessionShareDto checkShareCode(String shareId, String code);
}