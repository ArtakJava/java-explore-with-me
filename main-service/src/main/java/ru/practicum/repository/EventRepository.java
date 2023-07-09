package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.enums.EventState;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndEventDateAfterAndState(
            String text,
            String text1,
            long[] categories,
            boolean paid,
            LocalDateTime rangeStart,
            EventState eventState,
            Pageable pageable
    );

    List<Event> findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndEventDateAfterAndEventDateBeforeAndState(
            String text,
            String text1,
            long[] categories,
            boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            EventState eventState,
            Pageable pageable
    );

    List<Event> findByInitiatorId(long userId, Pageable pageable);

    List<Event> findByIdIn(List<Long> events);

    List<Event> findAllByEventDateAfter(LocalDateTime rangeStart, Pageable pageable);

    List<Event> findAllByInitiatorIdInAndCategoryIdInAndEventDateAfter(
            long[] users,
            long[] categories,
            LocalDateTime rangeStart,
            Pageable pageable
    );

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfter(
            long[] users,
            List<EventState> states,
            long[] categories,
            LocalDateTime now,
            Pageable pageable
    );

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
            long[] users,
            List<EventState> states,
            long[] categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    );

    List<Event> findByCategoryIdInAndEventDateAfterAndState(
            long[] categories,
            LocalDateTime rangeStart,
            EventState eventState,
            Pageable pageable
    );

    List<Event> findByEventDateAfterAndState(LocalDateTime rangeStart, EventState eventState, Pageable pageable);

    List<Event> findAllByCategoryIdInAndEventDateAfter(long[] categories, LocalDateTime rangeStart, Pageable pageable);
}