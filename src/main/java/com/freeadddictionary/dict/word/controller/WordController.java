package com.freeadddictionary.dict.word.controller;

import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.service.UserService;
import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.WordAddRequest;
import com.freeadddictionary.dict.word.dto.request.WordUpdateRequest;
import com.freeadddictionary.dict.word.dto.response.WordViewResponse;
import com.freeadddictionary.dict.word.service.WordService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

  private final WordService wordService;
  private final UserService userService;

  @GetMapping("/list")
  public String getWordList(
      Model model,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "") String kw) {
    Page<Word> paging = wordService.getList(page, kw);
    model.addAttribute("paging", paging);
    model.addAttribute("kw", kw);
    return "word/word_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model, @PathVariable Long id, WordViewResponse wordResponse) {
    Word word = wordService.getWord(id);
    model.addAttribute("word", word);
    return "word/word_detail";
  }

  @GetMapping("/create")
  @PreAuthorize("isAuthenticated()")
  public String createWord(WordAddRequest wordRequest) {
    return "word/new_word";
  }

  @PostMapping("/create")
  @PreAuthorize("isAuthenticated()")
  public String createWord(
      @Valid WordAddRequest wordRequest, BindingResult bindingResult, Principal principal) {
    if (bindingResult.hasErrors()) {
      return "word/new_word";
    }
    DictUser dictUser = userService.getUserByEmail(principal.getName());
    wordService.create(wordRequest, dictUser);
    return "redirect:/word/list";
  }

  @GetMapping("/modify/{id}")
  @PreAuthorize("isAuthenticated()")
  public String modifyWord(
      @Valid WordUpdateRequest request,
      BindingResult bindingResult,
      Principal principal,
      @PathVariable Long id) {
    if (bindingResult.hasErrors()) {
      return "new_word";
    }
    Word word = wordService.getWord(id);
    if (!word.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    wordService.modify(id, request);
    return String.format("redirect:/word/detail/%s", id);
  }

  @GetMapping("/delete/{id}")
  @PreAuthorize("isAuthenticated()")
  public String deleteWord(Principal principal, @PathVariable Long id) {
    Word word = wordService.getWord(id);
    if (!word.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
    }
    wordService.delete(id);
    return "redirect:/";
  }
}
