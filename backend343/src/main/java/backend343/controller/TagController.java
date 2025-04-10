package backend343.controller;

import backend343.enums.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @GetMapping("/all-tags")
    public List<Tag> getAllTags() {
        return Arrays.asList(Tag.values());
    }
}
