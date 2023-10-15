package com.easypan.convert;

import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 文件信息表
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-25
*/
@Mapper(componentModel = "spring")
public interface FileInfoConvert {
    FileInfoConvert INSTANCE = Mappers.getMapper(FileInfoConvert.class);

    FileInfo convert(FileInfoVO vo);

    FileInfoVO convert(FileInfo entity);

    List<FileInfoVO> convertList(List<FileInfo> list);

}