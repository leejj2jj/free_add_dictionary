package com.freeadddictionary.dict.word.api;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.service.UserService;
import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.AddWordRequest;
import com.freeadddictionary.dict.word.dto.request.UpdateWordRequest;
import com.freeadddictionary.dict.word.dto.response.WordResponse;
import com.freeadddictionary.dict.word.service.WordService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/words")
@RestController
@RequiredArgsConstructor
public class WordApiController {

  private final WordService wordService;
  private final UserService userService;

  @GetMapping("")
  public ResponseEntity<Page<WordResponse>> findAllWords(
      @PageableDefault(size = 10) Pageable pageable) {
    Page<WordResponse> words = wordService.findAll(pageable).map(WordResponse::new);
    return ResponseEntity.ok().body(words);
  }

  @PostMapping("")
  public ResponseEntity<Word> addWord(
      @Validated @RequestBody AddWordRequest request, Principal principal) {
    String email = principal.getName();
    User user = userService.findByEmail(email);
    Word word = wordService.createWord(request, user.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(word);
  }

  @GetMapping("/{id}")
  public ResponseEntity<WordResponse> findWord(@PathVariable long id) {
    Word word = wordService.findById(id);
    return ResponseEntity.ok().body(new WordResponse(word));
  }

  @PutMapping("/{id}")
  public ResponseEntity<WordResponse> updateWord(
      @PathVariable long id, @Validated @RequestBody UpdateWordRequest request) {
    Word updatedWord = wordService.update(id, request);
    return ResponseEntity.ok().body(new WordResponse(updatedWord));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWord(@PathVariable long id) {
    wordService.delete(id);
    return ResponseEntity.ok().build();
  }
}
