package ru.practicum.service.priv.event;

import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.CommentShortDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.service.admin.user.UserMapper;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment mapToCommentEntity(User user, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .event(event)
                .author(user)
                .createdOn(LocalDateTime.now())
                .state(ModerationState.PENDING)
                .build();
    }

    public static CommentShortDto mapToCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.mapToUserShortDto(comment.getAuthor()))
                .publishedOn(comment.getPublishedOn())
                .build();
    }

    public static CommentFullDto mapToCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.mapToUserShortDto(comment.getAuthor()))
                .publishedOn(comment.getPublishedOn())
                .createdOn(comment.getCreatedOn())
                .state(comment.getState())
                .build();
    }
}