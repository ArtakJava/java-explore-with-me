package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByStateIn(List<ModerationState> states, Pageable pageable);

    List<Comment> findAllByEventIdIn(Set<Long> eventIds);

    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findByEventIdInAndState(Set<Long> eventIds, ModerationState state);

    List<Comment> findAllByAuthorId(long userId, Pageable pageable);

    List<Comment> findByEventIdAndState(long eventId, ModerationState state, Pageable pageable);
}