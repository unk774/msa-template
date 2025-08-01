package org.template.aws_storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.template.aws_storage.service.s3.S3FileMetaData;
import org.template.aws_storage.api.dto.GetMetadata200Response;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface S3MetadataMapper {
    @Mapping(target = "lastModified", source = "lastModified", qualifiedByName = "instantToDate")
    GetMetadata200Response toResponse(S3FileMetaData s3Meta);

    @Named("instantToDate")
    static Date instantToDate(Instant instant) {
        return instant != null ? Date.from(instant) : null;
    }
}