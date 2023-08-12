package com.hoister.tonshoister.programTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.controllers.ProgramController;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.services.ProgramService;

@WebMvcTest(ProgramController.class)
public class ProgramControllerTests {

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ProgramService programService;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void createProgramSuccess() throws Exception {
    Program program = new Program("Starting Strength", 40, "Rookie Program.");
    when(programService.createProgram(program)).thenReturn(program);

    mockMvc
        .perform(
            post("/api/programs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(program)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(program.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.durationWeeks", CoreMatchers.is(program.getDurationWeeks())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(program.getDescription())));
  }

  @Test
  public void getProgramsSuccess() throws Exception {
    Program program = new Program("GVT", 12, "German Volume training");
    List<Program> programs = new ArrayList<Program>();
    programs.add(program);

    when(programService.findAll()).thenReturn(programs);

    mockMvc.perform(get("/api/programs"))
        .andExpect(status().isOk()).andExpect(jsonPath("$..name").value("GVT"));

    verify(programService).findAll();
  }

  @Test
  public void getProgramsThrowsException() throws Exception {
    when(programService.findAll()).thenThrow(new ProgramNotFoundException());

     mockMvc.perform(get("/api/programs"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

        verify(programService).findAll();    
  }

  @Test
  public void getProgramByIdSuccess() throws Exception {
    Program program = new Program("GVT", 12, "German Volume training");

    when(programService.findById(1)).thenReturn(program);

    mockMvc.perform(get("/api/programs/1"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("GVT"));

    verify(programService).findById(1);
  }

  @Test
  public void getProgramByIdThrowsException() throws Exception {
    when(programService.findById(1)).thenThrow(new ProgramNotFoundException());

     mockMvc.perform(get("/api/programs/1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

        verify(programService).findById(1);
  }
}
