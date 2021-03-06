package com.mshindelar.lockegameservice.pokeapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mshindelar.lockegameservice.pokeapi.PokeApiClient;
import lombok.Data;

import java.util.*;

@Data
public class Pokemon {
    private int id;
    private String name;
    private List<Ability> abilities;
    private Set<Type> types;
    private BaseStats baseStats;
    private int evolutionChainId;

    @JsonProperty("stats")
    public void flattenStats(List<Object> stats) {
        BaseStatsBuilder baseStatsBuilder = new BaseStatsBuilder();

        for(Object stat : stats) {
            String name = (String) ((LinkedHashMap)((LinkedHashMap) stat).get("stat")).get("name");
            int value = (Integer) ((LinkedHashMap) stat).get("base_stat");
            baseStatsBuilder.withStat(name, value);
        }

        this.baseStats = baseStatsBuilder.build();
    }

    @JsonProperty("evolution_chain")
    public void pullEvolutionChainId(List<Object> evolutionChain) {
        String url = (String) ((LinkedHashMap) evolutionChain).get("name");

        String[] tokens = url.split("/");

        this.evolutionChainId = Integer.parseInt(tokens[tokens.length - 1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return id == pokemon.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
