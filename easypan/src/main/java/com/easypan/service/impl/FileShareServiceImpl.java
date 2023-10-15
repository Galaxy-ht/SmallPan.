package com.easypan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.easypan.convert.FileShareConvert;
import com.easypan.entity.FileShare;
import com.easypan.entity.dto.SessionShareDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.vo.FileShareVO;
import com.easypan.exception.ErrorCode;
import com.easypan.exception.FastException;
import com.easypan.mappers.FileShareDao;
import com.easypan.page.PageResult;
import com.easypan.query.FileShareQuery;
import com.easypan.service.FileInfoService;
import com.easypan.service.FileShareService;
import com.easypan.service.UserInfoService;
import com.easypan.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 分享信息
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-10-14
 */
@Service
@AllArgsConstructor
public class FileShareServiceImpl extends BaseServiceImpl<FileShareDao, FileShare> implements FileShareService {

    @Resource
    private UserInfoService userService;

    @Resource
    private FileInfoService fileService;

    @Override
    public PageResult<FileShareVO> page(FileShareQuery query) {
        IPage<FileShare> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        List<FileShare> fileShareList = page.getRecords();
        List<FileShareVO> fileShareVOList = FileShareConvert.INSTANCE.convertList(fileShareList);
        for (FileShareVO fileShareVO : fileShareVOList) {
            UserInfo curUser = userService.getById(fileShareVO.getUserId());
            FileInfo curFile = fileService.getById(fileShareVO.getFileId());
            fileShareVO.setNickName(curUser.getNickName());
            fileShareVO.setAvatar(curUser.getQqAvatar());
            fileShareVO.setFileName(curFile.getFileName());
        }
        return new PageResult<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), fileShareVOList);
    }

    private QueryWrapper<FileShare> getWrapper(FileShareQuery query) {
        QueryWrapper<FileShare> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(query.getShareId()), "share_id", query.getShareId());
        wrapper.eq(StringUtils.isNotEmpty(query.getFileId()), "file_id", query.getFileId());
        wrapper.eq(StringUtils.isNotEmpty(query.getUserId()), "user_id", query.getUserId());
        wrapper.eq(query.getValidType() != null, "valid_type", query.getValidType());
        wrapper.eq(query.getCode() != null, "code", query.getCode());
        wrapper.eq(query.getShowCount() != null, "show_count", query.getShowCount());
        wrapper.last(StringUtils.isNotEmpty(query.getOrderBy()), "order by " + query.getOrderBy());
        return wrapper;
    }

    @Override
    public void save(FileShareVO vo) {
        FileShare entity = FileShareConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(FileShareVO vo) {
        FileShare entity = FileShareConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public void deleteFileShareBatch(String[] shareIdArray, String userId) {
        Integer count = this.baseMapper.deleteFileShareBatch(shareIdArray, userId);
        if (count != shareIdArray.length) {
            throw new FastException(ErrorCode.CODE_600);
        }
    }

    @Override
    public SessionShareDto checkShareCode(String shareId, String code) {
        FileShare share = this.getById(shareId);
        if (null == share || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
            throw new FastException(ErrorCode.CODE_902);
        }
        if (!share.getCode().equals(code)) {
            throw new FastException("提取码错误");
        }

        //更新浏览次数
        Integer showCount = share.getShowCount();
        if (null == showCount) {
            showCount = 1;
        } else {
            showCount += 1;
        }
        share.setShowCount(showCount);
        this.updateById(share);

        SessionShareDto shareSessionDto = new SessionShareDto();
        shareSessionDto.setShareId(shareId);
        shareSessionDto.setShareUserId(share.getUserId());
        shareSessionDto.setFileId(share.getFileId());
        shareSessionDto.setExpireTime(share.getExpireTime());
        return shareSessionDto;
    }

}