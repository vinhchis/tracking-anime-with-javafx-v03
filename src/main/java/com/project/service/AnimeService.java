package com.project.service;

import com.project.entity.Anime;
import com.project.repository.AnimeRepository;

public class AnimeService {
    private final AnimeRepository animeRepository;
    public AnimeService() {
        animeRepository = new AnimeRepository();
    }

    public Anime saveAnime(Anime anime) {
        return animeRepository.save(anime);
    }
}
