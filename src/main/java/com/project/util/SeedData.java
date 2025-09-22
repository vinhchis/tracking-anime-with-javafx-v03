package com.project.util;

import java.time.LocalTime;
import java.util.Arrays;

import com.project.entity.Anime;
import com.project.entity.Notification;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.entity.Tracking;
import com.project.entity.Season.SEASON_NAME;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.repository.AnimeRepository;
import com.project.repository.NotificationRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;
import com.project.repository.TrackingRepository;


public class SeedData {
    public static void seeds() {
        AnimeRepository animeRepo = new AnimeRepository();
        StudioRepository studioRepo = new StudioRepository();
        SeasonRepository seasonRepo = new SeasonRepository();
        TrackingRepository trackingRepo = new TrackingRepository();
        NotificationRepository notificationRepo = new NotificationRepository();

        if (studioRepo.count() == 0) {
            Studio studio1 = Studio.builder()
                .studioName("Studio Ghibli")
                .logoUrl("Studio Ghibli.jpg")
                .bestAnimes("My Neighbor Totoro, Spirited Away, Princess Mononoke")
                .build();
            Studio studio2 = Studio.builder()
                .studioName("Kyoto Animation")
                .logoUrl("Kyoto Animation.jpg")
                .bestAnimes("Clannad, Violet Evergarden, A Silent Voice")
                .build();
            Studio studio3 = Studio.builder()
                .studioName("Madhouse")
                .logoUrl("Madhouse.jpg")
                .bestAnimes("Death Note, One Punch, Hunter x Hunter")
                .build();
            Studio studio4 = Studio.builder()
                .studioName("Bones")
                .logoUrl("Bones.jpg")
                .bestAnimes("Fullmetal Alchemist, My Hero Academia, Mob Psycho 100")
                .build();
            Studio studio5 = Studio.builder()
                .studioName("Ufotable")
                .logoUrl("Ufotable.jpg")
                .bestAnimes("Demon Slayer, Fate series, The Garden of Sinners")
                .build();

            studioRepo.saveAll(Arrays.asList(studio1, studio2, studio3, studio4, studio5));
        }

        if (seasonRepo.count() == 0) {
            Season season1 = Season.builder().seasonName(SEASON_NAME.FALL).seasonYear((short)2024).build();
            Season season2 = Season.builder().seasonName(SEASON_NAME.SUMMER).seasonYear((short)2024).build();
            Season season3 = Season.builder().seasonName(SEASON_NAME.SPRING).seasonYear((short)2024).build();
            Season season4 = Season.builder().seasonName(SEASON_NAME.WINTER).seasonYear((short)2024).build();

            seasonRepo.saveAll(Arrays.asList(season1, season2, season3, season4));
        }

        if(animeRepo.count() == 0) {
            Studio studioGhibli = studioRepo.findBy("studioName", "Studio Ghibli").getFirst();
            Studio studioMadhouse = studioRepo.findBy("studioName", "Madhouse").getFirst();
            Season seasonFall2024 = seasonRepo.findBySeasonNameAndSeasonYear(SEASON_NAME.FALL, (short)2024).get();
            Season seasonSummer2024 = seasonRepo.findBySeasonNameAndSeasonYear(SEASON_NAME.SUMMER, (short)2024).get();

            Anime anime1 = Anime.builder()
                    // .apiId("1")
                    .title("My Neighbor Totoro")
                    .posterUrl("My Neighbor Totoro.jpg")
                    .introduction("My Neighbor Totoro is a heartwarming tale of two young sisters who move to the countryside and discover magical creatures in the nearby forest, including the gentle and iconic Totoro. Directed by Hayao Miyazaki, this beloved Studio Ghibli film explores themes of family, nature, and childhood wonder.")
                    .animeType(Anime.ANIME_TYPE.MOVIE)
                    .animeStatus(Anime.ANIME_STATUS.COMPLETED)
                    .totalEpisodes((short)1)
                    .studio(studioGhibli)
                    .season(seasonFall2024)
                    .build();

            Anime anime2 = Anime.builder()
                    // .apiId("2")
                    .title("Death Note")
                    .posterUrl("Death Note.jpg")
                    .introduction("Death Note is a psychological thriller anime that follows Light Yagami, a high school student who discovers a mysterious notebook that allows him to kill anyone by writing their name in it. As Light takes on the persona of 'Kira' and aims to create a utopia free of crime, he is pursued by the brilliant detective L in a high-stakes game of cat and mouse.")
                    .animeType(Anime.ANIME_TYPE.TV)
                    .animeStatus(Anime.ANIME_STATUS.COMPLETED)
                    .totalEpisodes((short)37)
                    .studio(studioMadhouse)
                    .season(seasonSummer2024)
                    .build();

            animeRepo.saveAll(Arrays.asList(anime1, anime2));
        }

        if (trackingRepo.count() == 0) {
            Anime anime1 = animeRepo.findBy("title", "My Neighbor Totoro").getFirst();
            Anime anime2 = animeRepo.findBy("title", "Death Note").getFirst();

            Tracking tracking1 = Tracking.builder()
                    .trackingStatus(TRACKINGS_STATUS.WATCHING)
                    .lastWatchedEpisode((short)1)
                    .scheduleDay(Tracking.DAY_OF_WEEK.SATURDAY)
                    .scheduleTime(LocalTime.of(18, 30))
                    .rating((byte)4)
                    .note("watch on TV360, good quality")
                    .anime(anime1)
                    .build();

            Tracking tracking2 = Tracking.builder()
                    .trackingStatus(TRACKINGS_STATUS.PLAN_TO_WATCH)
                    .lastWatchedEpisode((short)14)
                    .scheduleDay(Tracking.DAY_OF_WEEK.SUNDAY)
                    .scheduleTime(LocalTime.of(20, 0))
                    .rating((byte)0)
                    .note("wait for the dub version")
                    .anime(anime2)
                    .build();

            trackingRepo.saveAll(Arrays.asList(tracking1, tracking2));
        }

        // Seed Notifications - Todo
        if (notificationRepo.count() == 0) {

        }

    }
}