package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.RequestState;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("select count(requester_id) " +
            "from ParticipationRequest as r " +
            "where r.event.id = ?1 and state = 'CONFIRMED'")
    Integer findConfirmedParticipationCount(long eventId);

    @Query("select requester.id " +
            "from ParticipationRequest as r " +
            "where r.event.id = ?1")
    List<Long> findAllRequesterIdsInEvent(long eventId);

    List<ParticipationRequest> findAllByRequesterId(long userId);

    List<ParticipationRequest> findByEventIdInAndState(Set<Long> collect, RequestState confirmed);

    List<ParticipationRequest> findAllByEventId(long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findAllByIdInAndState(List<Long> requestIds, RequestState confirmed);
}