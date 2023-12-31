package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.service.dto.FileDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileDocument} and its DTO {@link FileDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileDocumentMapper extends EntityMapper<FileDocumentDTO, FileDocument> {}
