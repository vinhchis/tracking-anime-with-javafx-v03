package com.project.service;

import java.util.Optional;

import com.project.entity.Season;
import com.project.repository.SeasonRepository;

public class SeasonService {
    private final SeasonRepository seasonRepository;

    public SeasonService() {
        seasonRepository = new SeasonRepository();
    }

    public Season saveSeason(Season season) {
        return seasonRepository.save(season);
    }

    public Season getSeasonByName(Season.SEASON_NAME seasonName, Short year) {
        return seasonRepository.findBySeasonNameAndSeasonYear(seasonName, year).orElse(null);
    }
}
