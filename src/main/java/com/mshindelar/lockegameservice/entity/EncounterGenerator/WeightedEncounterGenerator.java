package com.mshindelar.lockegameservice.entity.EncounterGenerator;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler;
import org.apache.commons.rng.simple.RandomSource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeightedEncounterGenerator extends EncounterGenerator {

    private UniformRandomProvider rng;

    public WeightedEncounterGenerator() { this.rng = RandomSource.JDK.create(); }

    @Override
    public Encounter getEncounter(List<Encounter> encounters) {
        Map<Encounter, Double> probabilities = encounters.stream().collect(Collectors.toMap(Function.identity(), Encounter::getDefaultEncounterRate));

        DiscreteProbabilityCollectionSampler<Encounter> sampler = new DiscreteProbabilityCollectionSampler<>(this.rng, probabilities);
        return sampler.sample();
    }
}
