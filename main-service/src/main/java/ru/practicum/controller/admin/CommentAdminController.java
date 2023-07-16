package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.UpdateCommentAdminRequest;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.admin.event.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentAdminController {
    private final EventAdminService service;

    @PatchMapping("/{commentId}")
    public CommentFullDto updateComment(
            @PathVariable long commentId,
            @Valid @RequestBody UpdateCommentAdminRequest updateCommentAdminRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST_COMMENT, commentId, updateCommentAdminRequest);
        return service.updateComment(commentId, updateCommentAdminRequest);
    }

    @GetMapping
    public List<CommentFullDto> getComments(
            @RequestParam(required = false) String[] states,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_COMMENTS) @Positive int size) {
        PageRequestCustom pageRequest = new PageRequestCustom(from, size, ConstantManager.SORT_COMMENTS_BY_CREATE_DATE);
        log.info(InfoMessageManager.GET_ALL_COMMENTS_REQUEST);
        return service.getComments(states, pageRequest);
    }
}