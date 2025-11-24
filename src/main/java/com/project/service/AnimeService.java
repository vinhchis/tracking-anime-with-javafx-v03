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

    public Anime findByTitle(String title) {
        try {
            return animeRepository.findBy("title", title).getFirst();
        } catch (Exception e) {
            System.err.println("Can't find anime with title: " + title + " \n " + e.getMessage());
            return null;
        }
    }

    public Anime findByApiId(int apiId) {
        try {
            return animeRepository.findBy("apiId", apiId).getFirst();
        } catch (Exception e) {
            System.err.println("Can't find anime with apiId: " + apiId + " \n " + e.getMessage());
            return null;
        }
    }


}
