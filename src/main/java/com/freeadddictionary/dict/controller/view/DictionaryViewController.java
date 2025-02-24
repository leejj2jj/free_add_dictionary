package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/dictionary")
@RequiredArgsConstructor
@Slf4j
public class DictionaryViewController {

  private final DictionaryService dictionaryService;

  @GetMapping
  public String list(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {
    try {
      Page<Dictionary> dictionaries = dictionaryService.searchDictionaries(keyword, pageable);
      model.addAttribute("dictionaries", dictionaries);
      model.addAttribute("keyword", keyword);
      return "dictionary/dictionary_list";
    } catch (Exception e) {
      log.error("단어 목록 조회 중 오류 발생", e);
      return "error/500";
    }
  }

  @GetMapping("/{id}")
  public String detail(@PathVariable Long id, Model model) {
    Dictionary dictionary = dictionaryService.getDictionary(id);
    dictionaryService.incrementViewCount(id);
    model.addAttribute("dictionary", dictionary);
    return "dictionary/dictionary_detail";
  }

  @GetMapping("/form")
  public String form(@RequestParam(required = false) Long id, Model model) {
    if (id != null) {
      Dictionary dictionary = dictionaryService.getDictionary(id);
      model.addAttribute("dictionaryRequest", new DictionaryRequest(dictionary));
    } else {
      model.addAttribute("dictionaryRequest", new DictionaryRequest());
    }
    return "dictionary/dictionary_form";
  }
}
