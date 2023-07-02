package ru.clevertec.nms.model.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsDtoResponse entityToDtoResponse(News entity);

    News dtoRequestToEntity(NewsDtoRequest dto);
}
