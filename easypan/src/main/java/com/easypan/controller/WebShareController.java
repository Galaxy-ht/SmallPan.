package com.easypan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.convert.FileShareConvert;
import com.easypan.entity.FileShare;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SessionShareDto;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.FileShareVO;
import com.easypan.exception.ErrorCode;
import com.easypan.exception.FastException;
import com.easypan.page.PageResult;
import com.easypan.query.FileInfoQuery;
import com.easypan.service.FileShareService;
import com.easypan.service.UserInfoService;
import com.easypan.utils.Result;
import com.easypan.utils.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author Tao
 * @since 2023/10/14
 */
@RestController
@RequestMapping("/showShare")
public class WebShareController extends BaseController{
    @Resource
    private FileShareService fileShareService;

    @Resource
    private UserInfoService userInfoService;

    //
    @RequestMapping("/getShareLoginInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<FileShareVO> getShareLoginInfo(HttpSession session,
                                                 @VerifyParam(required = true) String shareId) {
        SessionShareDto shareSessionDto = getSessionShareFromSession(session, shareId);
        if (shareSessionDto == null) {
            return Result.ok();
        }
        FileShareVO shareInfoVO = getShareInfoCommon(shareId);
        //判断是否是当前用户分享的文件
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        shareInfoVO.setCurrentUser(userDto != null &&
                userDto.getUserId().equals(shareSessionDto.getShareUserId()));

        return Result.ok(shareInfoVO);
    }

    /**
     * 获取分享信息
     */
    @RequestMapping("/getShareInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<FileShareVO> getShareInfo(@VerifyParam(required = true) String shareId) {
        return Result.ok(getShareInfoCommon(shareId));
    }

    private FileShareVO getShareInfoCommon(String shareId) {
        // 根据shareId获得FileShare
        FileShare share = fileShareService.getById(shareId);
        // 如果FileShare为空或者已经过期
        if (share == null || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
            throw new FastException(ErrorCode.CODE_902.getMsg());
        }
        // FileShare映射为ShareInfoVO
        FileShareVO shareInfoVO = FileShareConvert.INSTANCE.convert(share);
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("file_id", share.getFileId());
        wrapper.eq("user_id", share.getUserId());
        FileInfo fileInfo = fileInfoService.getOne(wrapper);
        if (fileInfo == null || !FileDelFlagEnums.USING.getFlag().equals(fileInfo.getDelFlag())) {
            throw new FastException(ErrorCode.CODE_902.getMsg());
        }
        shareInfoVO.setFileName(fileInfo.getFileName());
        UserInfo userInfo = userInfoService.getById(share.getUserId());
        shareInfoVO.setNickName(userInfo.getNickName());
        shareInfoVO.setAvatar(userInfo.getQqAvatar());
        shareInfoVO.setUserId(userInfo.getUserId());
        return shareInfoVO;
    }

    /**
     * 校验分享码
     */
    @RequestMapping("/checkShareCode")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<String> checkShareCode(HttpSession session,
                                     @VerifyParam(required = true) String shareId,
                                     @VerifyParam(required = true) String code) {
        SessionShareDto shareSessionDto = fileShareService.checkShareCode(shareId, code);
        session.setAttribute(Constants.SESSION_SHARE_KEY + shareId, shareSessionDto);
        return Result.ok();
    }

    // 只能分享一个文件或者文件夹(可包含多个子文件夹和子文件)
    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<PageResult<FileInfoVO>> loadFileList(HttpSession session,
                                           @VerifyParam(required = true) String shareId, String filePid) {
        // 查询出对应链接下的SessionShareDto
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        FileInfoQuery query = new FileInfoQuery();

        // 如果父目录不是根目录
        if (!StringUtils.isEmpty(filePid) && !Constants.ZERO_STR.equals(filePid)) {
//            fileInfoService.checkRootFilePid(shareSessionDto.getFileId(), shareSessionDto.getShareUserId(), filePid);
            query.setFilePid(filePid);
        } else {
            // 如果是根目录
            query.setFileId(shareSessionDto.getFileId());
        }
        query.setUserId(shareSessionDto.getShareUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PageResult<FileInfoVO> resultVO = fileInfoService.page(query);
        return Result.ok(resultVO);
    }


    // 判断分享是否失效
    private SessionShareDto checkShare(HttpSession session, String shareId) {
        SessionShareDto shareSessionDto = getSessionShareFromSession(session, shareId);
        if (shareSessionDto == null) {
            throw new FastException(ErrorCode.CODE_903);
        }
        if (shareSessionDto.getExpireTime() != null && new Date().after(shareSessionDto.getExpireTime())) {
            throw new FastException(ErrorCode.CODE_902);
        }
        return shareSessionDto;
    }

    @RequestMapping("/getFolderInfo")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<List<FileInfoVO>> getFolderInfo(HttpSession session,
                                                  @VerifyParam(required = true) String shareId,
                                                  @VerifyParam(required = true) String path) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        return super.getFolderInfo(path, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/getFile/{shareId}/{fileId}")
    public void getFile(HttpServletResponse response, HttpSession session,
                        @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                        @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/ts/getVideoInfo/{shareId}/{fileId}")
    public void getVideoInfo(HttpServletResponse response,
                             HttpSession session,
                             @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                             @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/createDownloadUrl/{shareId}/{fileId}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public Result<String> createDownloadUrl(HttpSession session,
                                            @PathVariable("shareId") @VerifyParam(required = true) String shareId,
                                            @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        return super.createDownloadUrl(fileId, shareSessionDto.getShareUserId());
    }

    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("code") @VerifyParam(required = true) String code) throws Exception {
        super.download(request, response, code);
    }

    @RequestMapping("/saveShare")
    @GlobalInterceptor(checkParams = true)
    public Result<String> saveShare(HttpSession session,
                                    @VerifyParam(required = true) String shareId,
                                    @VerifyParam(required = true) String shareFileIds,
                                    @VerifyParam(required = true) String myFolderId) {
        // 校验分享链接
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        // 校验登陆用户和分享用户
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        if (shareSessionDto.getShareUserId().equals(webUserDto.getUserId())) {
            throw new FastException("自己分享的文件无法保存到自己的网盘");
        }

        fileInfoService.saveShare(shareSessionDto.getFileId(), shareFileIds, myFolderId,
                shareSessionDto.getShareUserId(), webUserDto.getUserId());
        return Result.ok();
    }
}
