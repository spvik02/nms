package ru.clevertec.nms.utils;

import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.Comment;
import ru.clevertec.nms.model.entity.News;

import java.time.LocalDateTime;

public class TestData {

    private static final String NEWS_TITLE = "how the next test ended";
    private static final String NEWS_TEXT = "read more...";
    private static final String COMMENT_TEXT = "amazing comment";
    private static final String COMMENT_USERNAME = "amazing commentator";
    private static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);

    public static News buildNews() {
        return News.builder()
                .id(1L)
                .time(YESTERDAY)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static Comment buildComment() {
        return Comment.builder()
                .id(1L)
                .time(YESTERDAY)
                .text(COMMENT_TEXT)
                .username(COMMENT_USERNAME)
                .news(buildNews())
                .build();
    }

    public static NewsDtoRequest buildNewsDtoRequest() {
        return NewsDtoRequest.builder()
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static CommentDtoRequest buildCommentDtoRequest() {
        return CommentDtoRequest.builder()
                .text(COMMENT_TEXT)
                .username(COMMENT_USERNAME)
                .newsId(1L)
                .build();
    }

    public static NewsDtoResponse buildNewsDtoResponse() {
        return NewsDtoResponse.builder()
                .id(1L)
                .time(YESTERDAY)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static CommentDtoResponse buildCommentDtoResponse() {
        return CommentDtoResponse.builder()
                .id(1L)
                .time(YESTERDAY)
                .text(COMMENT_TEXT)
                .username(COMMENT_USERNAME)
                .newsId(1L)
                .build();
    }
}
