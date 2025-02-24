package com.freeadddictionary.dict.controller.view;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.freeadddictionary.dict.config.TestConfig;
import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.dto.response.StatisticsResponse;
import com.freeadddictionary.dict.service.DictionaryService;
import com.freeadddictionary.dict.service.StatisticsService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(HomeController.class)
@Import(TestConfig.class)
class HomeControllerTest {

  private MockMvc mockMvc;

  @Mock private DictionaryService dictionaryService;

  @Mock private StatisticsService statisticsService;

  @InjectMocks private HomeController homeController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
  }

  @Test
  void home_Success() throws Exception {
    // given
    Dictionary dictionary =
        Dictionary.builder().word("test").language("English").meaning("시험").build();

    StatisticsResponse statistics =
        StatisticsResponse.builder().totalUsers(100).totalDictionaries(500).build();

    given(dictionaryService.getRecentDictionaries()).willReturn(List.of(dictionary));
    given(dictionaryService.getPopularDictionaries()).willReturn(List.of(dictionary));
    given(statisticsService.getStatistics()).willReturn(statistics);

    // when & then
    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andExpect(model().attributeExists("recentDictionaries"))
        .andExpect(model().attributeExists("popularDictionaries"))
        .andExpect(model().attributeExists("statistics"));
  }

  @Test
  void about_Success() throws Exception {
    mockMvc.perform(get("/about")).andExpect(status().isOk()).andExpect(view().name("about"));
  }

  @Test
  void privacy_Success() throws Exception {
    mockMvc.perform(get("/privacy")).andExpect(status().isOk()).andExpect(view().name("privacy"));
  }

  @Test
  void terms_Success() throws Exception {
    mockMvc.perform(get("/terms")).andExpect(status().isOk()).andExpect(view().name("terms"));
  }
}
