package com.easypan.convert;

import com.easypan.entity.FileShare;
import com.easypan.entity.vo.FileShareVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 分享信息
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-10-14
*/
@Mapper
public interface FileShareConvert {
    FileShareConvert INSTANCE = Mappers.getMapper(FileShareConvert.class);

    FileShare convert(FileShareVO vo);

    FileShareVO convert(FileShare entity);

    List<FileShareVO> convertList(List<FileShare> list);

}