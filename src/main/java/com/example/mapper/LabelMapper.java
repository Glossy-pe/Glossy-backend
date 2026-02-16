package com.example.mapper;

import com.example.dtos.request.LabelRequest;
import com.example.dtos.response.LabelResponse;
import com.example.entity.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    LabelResponse toResponse(Label label);

    Label toEntity(LabelRequest labelRequest);
}
