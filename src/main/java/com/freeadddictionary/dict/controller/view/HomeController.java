package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.service.DictionaryService;
import com.freeadddictionary.dict.service.StatisticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final DictionaryService dictionaryService;
  private final StatisticsService statisticsService;

  @GetMapping("/")
  public String home(Model model) {
    List<Dictionary> recentDictionaries = dictionaryService.getRecentDictionaries();
    List<Dictionary> popularDictionaries = dictionaryService.getPopularDictionaries();

    model.addAttribute("recentDictionaries", recentDictionaries);
    model.addAttribute("popularDictionaries", popularDictionaries);
    model.addAttribute("statistics", statisticsService.getStatistics());

    return "home";
  }

  @GetMapping("/about")
  public String about() {
    return "about";
  }

  @GetMapping("/privacy")
  public String privacy() {
    return "privacy";
  }

  @GetMapping("/terms")
  public String terms() {
    return "terms";
  }
}
