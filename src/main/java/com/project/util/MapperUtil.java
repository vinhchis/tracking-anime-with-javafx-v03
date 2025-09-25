package com.project.util;

import com.project.dto.AnimeCardDto;
import com.project.dto.DataSaveDto;
import com.project.dto.JikanAnimeResponse.AnimeData;
import com.project.entity.Anime;
import com.project.entity.Season;
import com.project.entity.Season.SEASON_NAME;
import com.project.entity.Studio;

public class MapperUtil {
    public static DataSaveDto mapToDataSave(AnimeCardDto dto) {
        DataSaveDto dataSaveDto = new DataSaveDto();
        Anime anime = new Anime();
        Studio studio = new Studio();
        Season season = new Season();

        // anime
        anime.setApiId(dto.getApiId());
        anime.setTitle(dto.getTitle());
        anime.setPosterUrl(dto.getPosterUrl());
        anime.setIntroduction(dto.getSynopsis());
        anime.setTotalEpisodes(dto.getTotalEpisodes());
       if (dto.getAnimeStatus() != null) {
            switch (dto.getAnimeStatus().toLowerCase()) {
                case "finished airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.COMPLETED);
                    break;
                case "currently airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.ONGOING);
                    break;
                case "not yet aired":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.UPCOMING);
                    break;
                case "canceled":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.CANCELLED);
                    break;
                case "hiatus":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.HIATUS);
                    break;
            }
        }

        if (dto.getAnimeType() != null) {
            switch (dto.getAnimeType().toUpperCase()) {
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
            }
        }

        // season
        if (dto.getSeasonYear() != null) {
            season.setSeasonYear(dto.getSeasonYear());
        }

        if (dto.getSeasonName() != null) {
            switch (dto.getSeasonName().toLowerCase()) {
                case "winter":
                    season.setSeasonName(Season.SEASON_NAME.WINTER);
                    break;
                case "spring":
                    season.setSeasonName(SEASON_NAME.SPRING);
                    break;
                case "summer":
                    season.setSeasonName(SEASON_NAME.SUMMER);
                    break;
                case "fall":
                    season.setSeasonName(SEASON_NAME.FALL);
                    break;
            }

        }
        studio.setStudioName(dto.getStudioName());

        dataSaveDto.setAnime(anime);
        dataSaveDto.setStudio(studio);
        dataSaveDto.setSeason(season);
        return dataSaveDto;
    }

    public static AnimeCardDto mapToAnimeCardDto(AnimeData dto) {
        AnimeCardDto animeCardDto = new AnimeCardDto();

        animeCardDto.setApiId(Integer.valueOf(dto.getMal_id()));
        animeCardDto.setTitle(dto.getTitle());
        animeCardDto.setSynopsis(dto.getSynopsis());
        animeCardDto.setScore(dto.getScore());
        animeCardDto.setUrl(dto.getUrl());
        animeCardDto.setPosterUrl(dto.getImages().getJpg().getImage_url());
        animeCardDto.setTotalEpisodes(dto.getEpisodes() != null ? Short.valueOf(dto.getEpisodes().toString()) : null);

        animeCardDto.setAnimeStatus(dto.getStatus());
        animeCardDto.setAnimeType(dto.getType());
        if (dto.getYear() != null) {
            animeCardDto.setSeasonYear(Short.valueOf(dto.getYear().toString()));
        }
        animeCardDto.setSeasonName(dto.getSeason());

        if (dto.getStudios() != null && !dto.getStudios().isEmpty()) {
            animeCardDto.setStudioName(String.valueOf(dto.getStudios().get(0).getName()));
        }

        return animeCardDto;

    }

}
