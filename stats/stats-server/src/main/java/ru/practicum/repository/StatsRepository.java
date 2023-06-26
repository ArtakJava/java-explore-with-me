package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 and e.uri in (?3)" +
            "group by e.app, e.uri " +
            "order by count(ip) desc")
    List<ViewStatsDto> findByTimestampBetweenAndEventIdIn(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(distinct ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 and e.uri in (?3)" +
            "group by e.app, e.uri " +
            "order by count(distinct ip) desc")
    List<ViewStatsDto> findDistinctByIpAndTimestampBetweenAndEventIdIn(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 " +
            "group by e.app, e.uri " +
            "order by count(ip) desc")
    List<ViewStatsDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.ViewStatsDto(e.app, e.uri, count(distinct ip)) " +
            "from EndpointHit as e " +
            "where timestamp between ?1 and ?2 " +
            "group by e.app, e.uri " +
            "order by count(distinct ip) desc")
    List<ViewStatsDto> findDistinctByIpAndTimestampBetween(LocalDateTime start, LocalDateTime end);
}