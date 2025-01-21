package com.freeadddictionary.dict.word.controller;

import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.response.WordListViewResponse;
import com.freeadddictionary.dict.word.dto.response.WordViewResponse;
import com.freeadddictionary.dict.word.service.WordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WordController {

  private final WordService wordService;

  @GetMapping("/words")
  public String getWords(Model model) {
    List<WordListViewResponse> words =
        wordService.findAll().stream().map(WordListViewResponse::new).toList();

    model.addAttribute("words", words);
    return "word/wordList";
  }

  @GetMapping("/words/{id}")
  public String getWord(@PathVariable Long id, Model model) {
    Word word = wordService.findById(id);
    model.addAttribute("word", new WordViewResponse(word));

    return "word/word";
  }

  @GetMapping("/words/new")
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
