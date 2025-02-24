package com.freeadddictionary.dict.controller.api;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.service.DictionaryService;
import com.freeadddictionary.dict.util.SecurityUtil;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryApiController {

  private final DictionaryService dictionaryService;

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody DictionaryRequest request) {
    String email = SecurityUtil.getCurrentUserEmail();
    Dictionary dictionary = dictionaryService.createDictionary(request, email);
    return ResponseEntity.created(URI.create("/dictionary/" + dictionary.getId())).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable Long id, @Valid @RequestBody DictionaryRequest request) {
    String email = SecurityUtil.getCurrentUserEmail();
    dictionaryService.updateDictionary(id, request, email);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    dictionaryService.deleteDictionary(id);
    return ResponseEntity.noContent().build();
  }
}
