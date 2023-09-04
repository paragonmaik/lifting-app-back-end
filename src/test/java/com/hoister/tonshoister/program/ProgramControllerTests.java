package com.hoister.tonshoister.program;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ProgramDTO;
import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.controllers.ProgramController;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.PrincipalService;
import com.hoister.tonshoister.services.ProgramService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProgramController.class)
public class ProgramControllerTests {

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PrincipalService principalService;
  @MockBean
  TokenService tokenService;
  @MockBean
  UserRepository userRepository;
  @MockBean
  ProgramService programService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void createProgramSuccess() throws Exception {
    Program program = new Program(1, "uuid", "Starting Strength", 40, "Rookie Program.", null, null);
    ProgramDTO programDTO = new ProgramDTO(program.getId(),
        program.getUserId(), program.getName(),
        program.getDurationWeeks(),
        program.getDescription(), program.getDateCreated(), null);

    when(DTOsMapper.convertToEntity(any(ProgramDTO.class))).thenReturn(program);
    when(programService.createProgram(any(Program.class))).thenReturn(program);
    when(DTOsMapper.convertToDto(any(Program.class))).thenReturn(programDTO);

    mockMvc
        .perform(
            post("/api/programs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(program)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(programDTO.id())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(programDTO.name())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.durationWeeks", CoreMatchers.is(programDTO.durationWeeks())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(programDTO.description())));

    verify(DTOsMapper).convertToEntity(any(ProgramDTO.class));
    verify(programService).createProgram(any(Program.class));
    verify(DTOsMapper).convertToDto(any(Program.class));
  }

  @Test
  public void getProgramsSuccess() throws Exception {
    Program program = new Program(1, "uuid", "GVT", 12, "German Volume training", null, null);
    ProgramDTO programDTO = new ProgramDTO(program.getId(), program.getUserId(), program.getName(),
        program.getDurationWeeks(),
        program.getDescription(), program.getDateCreated(), null);

    List<Program> programs = new ArrayList<Program>();
    List<ProgramDTO> programsDTO = new ArrayList<ProgramDTO>();
    programs.add(program);
    programsDTO.add(programDTO);

    when(programService.findAllByUserId()).thenReturn(programs);
    when(DTOsMapper.convertToDto(any(Program.class))).thenReturn(programDTO);

    mockMvc.perform(get("/api/programs"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$..id").value(programsDTO.get(0).id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..name").value(programsDTO.get(0).name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..durationWeeks").value(programsDTO.get(0).durationWeeks()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..description").value(programsDTO.get(0).description()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..workouts").exists());

    verify(programService).findAllByUserId();
    when(DTOsMapper.convertToDto(any(Program.class))).thenReturn(programDTO);
  }

  @Test
  public void getProgramsThrowsException() throws Exception {
    when(programService.findAllByUserId()).thenThrow(new ProgramNotFoundException());

    mockMvc.perform(get("/api/programs"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

    verify(programService).findAllByUserId();
  }

  @Test
  public void getProgramByIdSuccess() throws Exception {
    Program program = new Program(1, "uuid", "GVT", 12, "German Volume training", null, null);
    ProgramDTO programDTO = new ProgramDTO(program.getId(), program.getUserId(),
        program.getName(),
        program.getDurationWeeks(),
        program.getDescription(), program.getDateCreated(), null);

    when(programService.findById(1)).thenReturn(program);
    when(DTOsMapper.convertToDto(any(Program.class))).thenReturn(programDTO);

    mockMvc.perform(get("/api/programs/temp/1"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(programDTO.id())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(program.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.durationWeeks").value(program.getDurationWeeks()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(program.getDescription()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..workouts").exists());

    verify(programService).findById(1);
    when(DTOsMapper.convertToDto(any(Program.class))).thenReturn(programDTO);
  }

  @Test
  public void getProgramByIdThrowsException() throws Exception {
    when(programService.findById(1)).thenThrow(new ProgramNotFoundException());

     mockMvc.perform(get("/api/programs/temp/1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

        verify(programService).findById(1);
  }

  @Test
  public void updateProgramSuccess() throws Exception {
    Program program = new Program(1, null, "5x5", 12, "Rookie Program.", null, null);

    when(DTOsMapper.convertToEntity(any(ProgramDTO.class))).thenReturn(program);
    when(programService.updateProgram(any(Program.class))).thenReturn(program);

    mockMvc
        .perform(
            put("/api/programs").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(program)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(DTOsMapper).convertToEntity(any(ProgramDTO.class));
    verify(programService).updateProgram(any(Program.class));
  }

  @Test
  public void updateProgramThrowsException() throws Exception {
    Program program = new Program(1, null, "5x5", 12, "Rookie Program.", null, null);

    when(DTOsMapper.convertToEntity(any(ProgramDTO.class))).thenReturn(program);
    when(programService.updateProgram(any(Program.class))).thenThrow(new ProgramNotFoundException());

    mockMvc.perform(put("/api/programs").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(program)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists());

    verify(DTOsMapper).convertToEntity(any(ProgramDTO.class));
    verify(programService).updateProgram(any(Program.class));
  }

  @Test
  public void updateProgramThrowsUserIdsDoNotMatchException() throws Exception {
    Program program = new Program(1, "uuid", "5x5", 12, "Rookie Program.", null, null);

    when(DTOsMapper.convertToEntity(any(ProgramDTO.class))).thenReturn(program);
    when(programService.updateProgram(any(Program.class)))
        .thenThrow(new UserIdDoesNotMatchException());

    mockMvc.perform(put("/api/programs").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(program)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").exists());

    verify(DTOsMapper).convertToEntity(any(ProgramDTO.class));
    verify(programService).updateProgram(any(Program.class));
  }

  @Test
  public void deleteProgramSuccess() throws Exception {
    doNothing().when(programService).deleteProgram(1);

    mockMvc
        .perform(
            delete("/api/programs/1"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(programService).deleteProgram(1);
  }

  @Test
  public void deleteProgramThrowsException() throws Exception {
    doThrow(new ProgramNotFoundException()).when(programService).deleteProgram(1);

    mockMvc
        .perform(
            delete("/api/programs/1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(programService).deleteProgram(1);
  }

  @Test
  public void deleteProgramThrowsUserIdsDoNotMatchException() throws Exception {
    doThrow(new UserIdDoesNotMatchException()).when(programService).deleteProgram(1);

    mockMvc
        .perform(
            delete("/api/programs/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    verify(programService).deleteProgram(1);
  }
}
