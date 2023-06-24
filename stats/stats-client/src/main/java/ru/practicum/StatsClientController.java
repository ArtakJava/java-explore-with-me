package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.messageManager.MessageHolder;

import javax.validation.Valid;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsClientController {
	private final StatsClient client;

	@PostMapping("/hit")
	public ResponseEntity<Object> hit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
		log.info(MessageHolder.POST_REQUEST_HIT, endpointHitDto);
		return client.hit(endpointHitDto);
	}

	@GetMapping("/stats")
	public ResponseEntity<Object> stats(@RequestParam String start,
										@RequestParam String end,
										@RequestParam(required = false) String[] uris,
										@RequestParam(required = false, defaultValue = "false") Boolean unique) {
		log.info(MessageHolder.GET_REQUEST_STATS, start, end);
		return client.stats(start, end, uris, unique);
	}
}