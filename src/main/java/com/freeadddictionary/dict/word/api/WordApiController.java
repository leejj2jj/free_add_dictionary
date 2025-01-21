package com.freeadddictionary.dict.word.api;

import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.AddWordRequest;
import com.freeadddictionary.dict.word.dto.request.UpdateWordRequest;
import com.freeadddictionary.dict.word.dto.response.WordResponse;
import com.freeadddictionary.dict.word.service.WordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WordApiController {

  private final WordService wordService;

  @PostMapping("/api/words")
  public ResponseEntity<Word> addWord(@Validated @RequestBody AddWordRequest request) {
    Word savedWord = wordService.save(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedWord);
  }

  @GetMapping("/api/words")
  public ResponseEntity<List<WordResponse>> findAllWords() {
    List<WordResponse> words = wordService.findAll().stream().map(WordResponse::new).toList();

    return ResponseEntity.ok().body(words);
  }

  @GetMapping("/api/words/{id}")
  public ResponseEntity<WordResponse> findWord(@PathVariable long id) {
    Word word = wordService.findById(id);
    return ResponseEntity.ok().body(new WordResponse(word));
  }

  @DeleteMapping("/api/words/{id}")
  public ResponseEntity<Void> deleteWord(@PathVariable long id) {
    wordService.delete(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/api/words/{id}")
  public ResponseEntity<WordResponse> updateWord(
      @PathVariable long id, @Validated @RequestBody UpdateWordRequest request) {
    Word updatedWord = wordService.update(id, request);
    return ResponseEntity.ok().body(new WordResponse(updatedWord));
  }
}
