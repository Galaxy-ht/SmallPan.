package com.easypan.service;

import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.page.PageResult;
import com.easypan.query.FileInfoQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件信息表
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-25
 */
public interface FileInfoService extends BaseService<FileInfo> {

    PageResult<FileInfoVO> page(FileInfoQuery query);

    void save(FileInfoVO vo);

    void update(FileInfoVO vo);

    void delete(List<Long> idList);

    UploadResultDto uploadFile(SessionWebUserDto sessionWebUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks);

    void removeFile2RecycleBatch(String userId, String fileIds);

    FileInfo newFolder(String filePid, String userId, String fileName);

    List<FileInfo> list(FileInfoQuery query);

    FileInfoVO rename(String fileId, String userId, String fileName);

    void changeFileFolder(String fileIds, String filePid, String userId);

    void recoverFileBatch(String userId, String fileIds);

    void delFileBatch(String userId, String fileIds, boolean b);

    void saveShare(String shareRootFilePid, String shareFileIds, String myFolderId, String shareUserId, String currentUserId);
}