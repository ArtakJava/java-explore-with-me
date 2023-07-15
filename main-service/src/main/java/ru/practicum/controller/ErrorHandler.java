package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundData(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorMessageManager.REQUIRED_OBJECT_NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MissingServletRequestParameterException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorMessageManager.INCORRECTLY_MADE_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorMessageManager.INCORRECTLY_MADE_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventNotPublishedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleMethodNotPublishedEventException(final EventNotPublishedException e) {
        log.info("404 {}", e.getMessage());
        return new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorMessageManager.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventNotPublishedForRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventNotPublishedForRequestException(final EventNotPublishedForRequestException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventAlreadyPublishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleMethodEventAlreadyPublishedException(final EventAlreadyPublishedException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(CommentAlreadyPublishedOrRejectedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleMethodCommentAlreadyPublishedException(final CommentAlreadyPublishedOrRejectedException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodEventDateException(final EventDateException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorMessageManager.INCORRECTLY_MADE_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotAuthorCommentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleMethodUserNotAuthorCommentException(final UserNotAuthorCommentException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(RangeParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodEventDateException(final RangeParametersException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorMessageManager.INCORRECTLY_MADE_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventParticipantLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventParticipantLimitException(final EventParticipantLimitException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(EventParticipantOwnerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventParticipantOwnerException(final EventParticipantOwnerException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(UserAlreadySendRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserAlreadySendRequestException(final UserAlreadySendRequestException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(RequestAlreadyConfirmedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestAlreadyConfirmedException(final RequestAlreadyConfirmedException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT,
                ErrorMessageManager.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.info("500 {}", e.getMessage());
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessageManager.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }
}