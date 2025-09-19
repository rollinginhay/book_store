package sd_009.bookstore.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sd_009.bookstore.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

}
