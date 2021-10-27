package com.mshindelar.lockegameservice.controller;

import com.mshindelar.lockegameservice.dto.EncounterDto;
import com.mshindelar.lockegameservice.entity.EncounterGenerator.Encounter;
import com.mshindelar.lockegameservice.entity.EncounterGenerator.EncounterMode;
import com.mshindelar.lockegameservice.entity.squadlocke.Nature;
import com.mshindelar.lockegameservice.entity.squadlocke.Squadlocke;
import com.mshindelar.lockegameservice.entity.squadlocke.SquadlockeParticipant;
import com.mshindelar.lockegameservice.entity.squadlocke.SquadlockePokemon;
import com.mshindelar.lockegameservice.entity.squadlocke.configuration.SquadlockeSettings;
import com.mshindelar.lockegameservice.service.SquadlockeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games/squadlocke")
@Slf4j
public class SquadlockeController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SquadlockeService squadlockeService;

    private static Logger logger = LoggerFactory.getLogger(SquadlockeController.class);

    private EncounterDto convertToDto(Encounter encounter) { return this.modelMapper.map(encounter, EncounterDto.class); }

    @PutMapping("create")
    private Squadlocke create(@RequestParam(name = "participantId") String participantId, @RequestBody SquadlockeSettings squadlockeSettings) {
        return this.squadlockeService.createSquadlocke(participantId, squadlockeSettings);
    }

    @GetMapping("{gameId}")
    private Squadlocke get(@PathVariable("gameId") String gameId, @RequestHeader Map<String, String> headers) {
        return this.squadlockeService.getSquadlocke(gameId);
    }

    @PostMapping("{gameId}/join")
    private Squadlocke join(@PathVariable(name = "gameId") String gameId, @RequestParam(name = "participantId") String participantId) {
        return this.squadlockeService.joinSquadlocke(gameId, participantId);
    }

    @PostMapping("{gameId}/start")
    private Squadlocke start(@PathVariable("gameId") String gameId) {
        return this.squadlockeService.startSquadlocke(gameId);
    }

    @PostMapping("{gameId}/finalize")
    private Squadlocke finalize(@PathVariable("gameId") String gameId) {
        return this.squadlockeService.finalizeSquadlocke(gameId);
    }

    @GetMapping("{gameId}/encounter")
    private EncounterDto getEncounter(@PathVariable("gameId") String gameId, @RequestParam("participantId") String participantId, @RequestParam("locationId") String locationId,
                                   @RequestParam("encounterMode") EncounterMode encounterMode, @RequestParam("filterSpeciesClause") boolean filterSpeciesClause) {
        Encounter e = this.squadlockeService.getEncounter(gameId, participantId, locationId, encounterMode, filterSpeciesClause);

        if(e == null) return null;

        EncounterDto eDto = this.convertToDto(e);
        return eDto;
    }

    @PostMapping("{gameId}/encounter")
    private SquadlockePokemon saveEncounter(@PathVariable("gameId") String gameId, @RequestParam("participantId") String participantId,
                                            @RequestParam("locationId") String locationId, @RequestParam("nickname") String nickname,
                                            @RequestParam("abilityIndex") int abilityIndex, @RequestParam("nature") Nature nature,
                                            @RequestParam("isShiny") boolean isShiny) {
        return this.squadlockeService.saveEncounter(gameId, participantId, locationId, nickname, abilityIndex, nature, isShiny);
    }

    @GetMapping("{gameId}/participants/{participantId}")
    private SquadlockeParticipant getParticipant(@PathVariable("gameId") String gameId, @PathVariable("participantId") String participantId) {
        return this.squadlockeService.getParticipant(gameId, participantId);
    }

    @PostMapping("{gameId}/participants/{participantId}/ready")
    private SquadlockeParticipant ready(@PathVariable("gameId") String gameId, @PathVariable("participantId") String participantId) {
        return this.squadlockeService.readyParticipant(gameId, participantId);
    }


    @GetMapping("/by-userid/{userId}")
    private List<Squadlocke> getSquadlockeByUserId(@PathVariable("userId") String userId) {
        return this.squadlockeService.getSquadlockeByUserId(userId);
    }

    @GetMapping("/joinable")
    private List<Squadlocke> getJoinableGames(@RequestParam("userId") String userId) {
        return this.squadlockeService.getJoinableGames(userId);
    }
}
