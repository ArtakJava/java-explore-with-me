package ru.practicum.service.admin.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.AbstractServiceImpl;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.*;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@Slf4j
public class CompilationAdminServiceImpl extends AbstractServiceImpl implements CompilationAdminService {

    public CompilationAdminServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository,
            CommentRepository commentRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository, commentRepository);
    }

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        Compilation compilation = CompilationMapper.mapToCompilationEntity(newCompilationDto);
        if (newCompilationDto.getEvents().size() != 0) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        Compilation result = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.mapToCompilationDto(
                result,
                getConfirmedRequestsCountByEvent(result.getEvents()),
                getViewsByEventId(result.getEvents(), ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getPublishedCommentsCountByEvent(events)
        );
        log.info(InfoMessageManager.SUCCESS_CREATE, compilationDto);
        return compilationDto;
    }

    @Override
    public void delete(long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilationRepository.delete(compilation);
        log.info(InfoMessageManager.SUCCESS_DELETE, compilation);
    }

    @Override
    public CompilationDto update(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation oldCompilation = compilationRepository.getReferenceById(compId);
        Compilation result = compilationRepository.save(getUpdatedCompilation(oldCompilation,updateCompilationRequest));
        CompilationDto resultDto = CompilationMapper.mapToCompilationDto(
                result,
                getConfirmedRequestsCountByEvent(result.getEvents()),
                getViewsByEventId(result.getEvents(), ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getPublishedCommentsCountByEvent(result.getEvents())
        );
        log.info(InfoMessageManager.SUCCESS_PATCH, compId, updateCompilationRequest);
        return resultDto;
    }

    @Transactional(readOnly = true)
    private Compilation getUpdatedCompilation(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        return compilation;
    }
}