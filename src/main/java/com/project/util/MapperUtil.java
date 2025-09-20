package com.project.util;

import com.project.dto.JikanAnimeResponse.AnimeData;
import com.project.entity.Anime;
import com.project.entity.Tracking;

public class MapperUtil {
    public static Anime mapToAnime(AnimeData dto) {
        Anime anime = new Anime();
        anime.setApiId(String.valueOf(dto.getMal_id()));
        anime.setTitle(dto.getTitle());
        anime.setPosterUrl(dto.getImages().getJpg().getImage_url());
        anime.setIntroduction(dto.getSynopsis());

        if (dto.getStatus() != null) {
            switch (dto.getStatus().toLowerCase()) {
                case "finished airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.COMPLETED);
                    break;
                case "currently airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.ONGOING);
                    break;
                case "not yet aired":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.UPCOMING);
                    break;

            }
        }

        if (dto.getType() != null) {
            switch (dto.getType().toUpperCase()) {
                case "TV":
                    anime.setAnimeType(Anime.ANIME_TYPE.TV);
                    break;
                case "MOVIE":
                    anime.setAnimeType(Anime.ANIME_TYPE.MOVIE);
                    break;
                case "OVA":
                    anime.setAnimeType(Anime.ANIME_TYPE.OVA);
                    break;
                case "SPECIAL":
                    anime.setAnimeType(Anime.ANIME_TYPE.SPECIAL);
                    break;
                default:
                    anime.setAnimeType(Anime.ANIME_TYPE.TV);
                    break;
            }
        }

        anime.setTotalEpisodes(dto.getEpisodes() != null ? dto.getEpisodes().shortValue() : null);

        return anime;
    }

}
