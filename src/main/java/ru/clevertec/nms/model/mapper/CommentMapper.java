package ru.clevertec.nms.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.entity.Comment;
import ru.clevertec.nms.model.entity.News;

import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(
            source = "news",
            target = "newsId",
            qualifiedByName = "newsToNewsId"
    )
    CommentDtoResponse entityToDtoResponse(Comment entity);

    Comment dtoRequestToEntity(CommentDtoRequest dto);

    @Named("newsToNewsId")
    static Long newsToNewsId(News news) {
        return Objects.isNull(news) ? null : news.getId();
    }
}
