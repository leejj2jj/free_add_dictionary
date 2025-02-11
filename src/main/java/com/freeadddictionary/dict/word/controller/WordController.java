package com.freeadddictionary.dict.word.controller;

import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.response.WordViewResponse;
import com.freeadddictionary.dict.word.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

  private final WordService wordService;

  @GetMapping("")
  public String getWords(
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      Model model) {
    Page<Word> words = wordService.findAll(pageable);
    model.addAttribute("words", words);
    return "word/wordList";
  }

  @GetMapping("/{id}")
  public String getWord(@PathVariable Long id, Model model) {
    Word word = wordService.findById(id);
    model.addAttribute("word", new WordViewResponse(word));
    return "word/word";
  }

  @GetMapping("/new")
  public String newWord(@RequestParam(required = false) Long id, Model model) {
    if (id == null) {
      model.addAttribute("word", new WordViewResponse());
    } else {
      Word word = wordService.findById(id);
      model.addAttribute("word", new WordViewResponse(word));
    }
    return "word/newWord";
  }
}
