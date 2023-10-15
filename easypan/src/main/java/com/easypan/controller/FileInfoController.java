package com.easypan.controller;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.convert.FileInfoConvert;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.enums.FileCategoryEnums;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.enums.FileFolderTypeEnums;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.page.PageResult;
import com.easypan.query.FileInfoQuery;
import com.easypan.service.FileInfoService;
import com.easypan.utils.Result;
import com.easypan.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 文件信息表
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-25
 */
@RestController
@RequestMapping("file")
//@Tag(name="文件信息表")
@AllArgsConstructor
public class FileInfoController extends BaseController {
    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping("loadDataList")
    @GlobalInterceptor
    public Result<PageResult<FileInfoVO>> page(HttpSession session, @Valid FileInfoQuery query, String category) {

        FileCategoryEnums categoryEnums = FileCategoryEnums.getByCode(category);
        if (null != categoryEnums) {
            query.setFileCategory(categoryEnums.getCategory());
        }
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());

        PageResult<FileInfoVO> page = fileInfoService.page(query);
        return Result.ok(page);
    }

    @RequestMapping("uploadFile")
    @GlobalInterceptor(checkParams = true)
    public Result<UploadResultDto> uploadFile(HttpSession session,
                                              String fileId,
                                              MultipartFile file,
                                              @VerifyParam(required = true) String fileName,
                                              @VerifyParam(required = true) String filePid,
                                              @VerifyParam(required = true) String fileMd5,
                                              @VerifyParam(required = true) Integer chunkIndex,
                                              @VerifyParam(required = true) Integer chunks) {

        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
        UploadResultDto resultDto = fileInfoService.uploadFile(sessionWebUserDto, fileId, file, fileName, filePid, fileMd5, chunkIndex, chunks);

        return Result.ok(resultDto);

    }

    @RequestMapping("getImage/{imageFolder}/{imageName}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response,
                                   @PathVariable("imageFolder") String imageFolder,
                                   @PathVariable("imageName") String imageName) {
        super.getImage(response, imageFolder, imageName);
    }

    @RequestMapping("ts/getVideoInfo/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getVideo(HttpSession session,
                         HttpServletResponse response,
                         @PathVariable("fileId") String fileId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, userDto.getUserId());
    }

    @RequestMapping("getFile/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getFile(HttpSession session,
                         HttpServletResponse response,
                         @PathVariable("fileId") String fileId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, userDto.getUserId());
    }

    @RequestMapping("delFile")
    @GlobalInterceptor(checkParams = true)
    public Result<String> delFile(HttpSession session, @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.removeFile2RecycleBatch(webUserDto.getUserId(), fileIds);
        return Result.ok();
    }

    @RequestMapping("newFolder")
    @GlobalInterceptor(checkParams = true)
    public Result<FileInfoVO> newFolder(HttpSession session,
                                    @VerifyParam(required = true) String filePid,
                                    @VerifyParam(required = true) String fileName) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfo fileInfo = fileInfoService.newFolder(filePid, webUserDto.getUserId(), fileName);
        return Result.ok(FileInfoConvert.INSTANCE.convert(fileInfo));
    }

    @RequestMapping("getFolderInfo")
    @GlobalInterceptor(checkParams = true)
    public Result<List<FileInfoVO>> getFolderInfo(HttpSession session, @VerifyParam(required = true) String path) {
        return super.getFolderInfo(path, getUserInfoFromSession(session).getUserId());
    }

    @RequestMapping("rename")
    @GlobalInterceptor(checkParams = true)
    public Result<FileInfoVO> rename(HttpSession session,
                                 @VerifyParam(required = true) String fileId,
                                 @VerifyParam(required = true) String fileName) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        return Result.ok(fileInfoService.rename(fileId, getUserInfoFromSession(session).getUserId(), fileName));
    }

    @RequestMapping("loadAllFolder")
    @GlobalInterceptor(checkParams = true)
    public Result<List<FileInfoVO>> loadAllFolder(HttpSession session,
                                         @VerifyParam(required = true) String filePid,
                                         String currentFileIds) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        FileInfoQuery query = new FileInfoQuery();
        query.setUserId(userDto.getUserId());
        query.setFilePid(filePid);
        query.setFolderType(FileFolderTypeEnums.FOLDER.getType());
        if (StringUtils.isNotEmpty(currentFileIds)) {
            query.setExcludeFileIdArray(currentFileIds.split(","));
        }
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        query.setOrderBy("create_time desc");
        List<FileInfoVO> fileInfoList = FileInfoConvert.INSTANCE.convertList(fileInfoService.list(query));
        return Result.ok(fileInfoList);
    }

    @RequestMapping("changeFileFolder")
    @GlobalInterceptor(checkParams = true)
    public Result<String> changeFileFolder (HttpSession session,
                                            @VerifyParam(required = true) String fileIds,
                                            @VerifyParam(required = true) String filePid) {
        fileInfoService.changeFileFolder(fileIds, filePid, getUserInfoFromSession(session).getUserId());
        return Result.ok();
    }

    @RequestMapping("/createDownloadUrl/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public Result<String> createDownloadUrl(HttpSession session,
                                        @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        return super.createDownloadUrl(fileId, getUserInfoFromSession(session).getUserId());
    }

    // 无需登陆校验
    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public void download(HttpServletRequest request,
                         HttpServletResponse response,
                         @PathVariable("code") @VerifyParam(required = true) String code) throws Exception {
        super.download(request, response, code);
    }


}