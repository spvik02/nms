package ru.clevertec.nms.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.News;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    NewsDtoResponse entityToDtoResponse(News entity);

    News dtoRequestToEntity(NewsDtoRequest dto);
}
