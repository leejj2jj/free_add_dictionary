package com.freeadddictionary.dict.dto.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> {
  private final List<T> content;
  private final int pageNumber;
  private final int pageSize;
  private final long totalElements;
  private final int totalPages;
  private final boolean first;
  private final boolean last;

  public PageResponse(Page<T> page) {
    this.content = page.getContent();
    this.pageNumber = page.getNumber();
    this.pageSize = page.getSize();
    this.totalElements = page.getTotalElements();
    this.totalPages = page.getTotalPages();
    this.first = page.isFirst();
    this.last = page.isLast();
  }
}
