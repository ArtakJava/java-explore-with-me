package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.comment.*;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.priv.event.EventPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentPrivateController {
    private final EventPrivateService service;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable long userId,
                                        @PathVariable long eventId,
                                        @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info(InfoMessageManager.POST_REQUEST_COMMENT, newCommentDto, userId, eventId);
        return service.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentFullDto updateComment(@PathVariable long userId,
                                        @PathVariable long commentId,
                                        @Valid @RequestBody UpdateCommentUserRequest updateCommentUserRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST, commentId, updateCommentUserRequest);
        return service.updateComment(userId, commentId, updateCommentUserRequest);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        log.info(InfoMessageManager.DELETE_REQUEST, commentId);
        service.deleteComment(userId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentShortDto> getAllComments(
            @PathVariable long userId,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_COMMENTS) @Positive int size) {
        log.info(InfoMessageManager.GET_ALL_COMMENTS_REQUEST);
        return service.getAllComments(
                userId,
                new PageRequestCustom(from, size, ConstantManager.SORT_COMMENTS_BY_CREATE_DATE)
        );
    }
}