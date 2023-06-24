package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.url, count(ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 and e.eventId in (?3)" +
            "group by e.app, e.url")
    List<ViewStatsDto> findByTimestampBetweenAndEventIdIn(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.url, count(distinct ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 and e.eventId in (?3)" +
            "group by e.app, e.url")
    List<ViewStatsDto> findDistinctByIpAndTimestampBetweenAndEventIdIn(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.url, count(ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 " +
            "group by e.app, e.url")
    List<ViewStatsDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.url, count(distinct ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 " +
            "group by e.app, e.url")
    List<ViewStatsDto> findDistinctByIpAndTimestampBetween(LocalDateTime start, LocalDateTime end);
}