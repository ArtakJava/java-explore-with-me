package ru.practicum.service.pub.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.AbstractServiceImpl;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Compilation;
import ru.practicum.repository.*;
import ru.practicum.service.admin.compilation.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
public class CompilationPublicServiceImpl extends AbstractServiceImpl implements CompilationPublicService {

    public CompilationPublicServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository);
    }

    @Override
    public List<CompilationDto> getAll(boolean pinned, PageRequestCustom pageRequestCustom) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequestCustom);
        List<CompilationDto> result = compilations.stream()
                .map(compilation -> CompilationMapper.mapToCompilationDto(
                        compilation,
                        getConfirmedRequestsByEvent(compilation.getEvents()),
                        getViewsByEventId(compilation.getEvents(), ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
                        )
                )
                .collect(Collectors.toList());
        log.info(InfoMessageManager.SUCCESS_ALL_COMPILATIONS_REQUEST);
        return result;
    }

    @Override
    public CompilationDto getById(long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        CompilationDto compilationDto = CompilationMapper.mapToCompilationDto(
                compilation,
                getConfirmedRequestsByEvent(compilation.getEvents()),
                getViewsByEventId(compilation.getEvents(), ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_COMPILATION_REQUEST, compId);
        return compilationDto;
    }
}