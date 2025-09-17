// package com.project.util;

// import java.time.DayOfWeek;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.Arrays;

// import com.project.entity.Anime;
// import com.project.entity.Episode;
// import com.project.entity.Notification;
// import com.project.entity.Season;
// import com.project.entity.Studio;
// import com.project.entity.Tracking;
// import com.project.model.SeasonId;
// import com.project.repository.AnimeRepository;
// import com.project.repository.EpisodeRepository;
// import com.project.repository.NotificationRepository;
// import com.project.repository.SeasonRepository;
// import com.project.repository.StudioRepository;
// import com.project.repository.TrackingRepository;

// import jakarta.persistence.EntityManagerFactory;

// public class SeedData {
//     public static void seeds() {
//     //     EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
//     //     AccountRepository accountRepository = new AccountRepository(emf);
//     //     SeasonRepository seasonRepository = new SeasonRepository(emf);
//     //     StudioRepository studioRepository = new StudioRepository(emf);
//     //     AnimeRepository animeRepository = new AnimeRepository(emf);
//     //     EpisodeRepository episodeRepository = new EpisodeRepository(emf);
//     //     NotificationRepository notificationRepository = new NotificationRepository(emf);
//     //     TrackingRepository trackingRepository = new TrackingRepository(emf);

//     //     if (accountRepository.count() > 0 || studioRepository.count() > 0 ||
//     //             seasonRepository.count() > 0 || animeRepository.count() > 0 ||
//     //             episodeRepository.count() > 0 || notificationRepository.count() > 0 ||
//     //             trackingRepository.count() > 0) {
//     //         System.out.println("-> Seeding skipped. Data already exists.");
//     //         return;
//     //     }

//     //     System.out.println("-> Seeding started...");
//     //     seedAccounts(accountRepository);
//     //     seedStudios(studioRepository);
//     //     seedSeasons(seasonRepository);
//     //     // with relation
//     //     seedAnimes(animeRepository, studioRepository, seasonRepository);
//     //     seedEpisodes(episodeRepository, animeRepository);
//     //     seedTrackings(trackingRepository, accountRepository, animeRepository);
//     //     seedNotifications(notificationRepository, accountRepository, animeRepository);
//     //     System.out.println("-> Seeding completed.");
//     // }

//     // private static void seedSeasons(SeasonRepository seasonRepository) {
//     //     System.out.println("-> Seeding Seasons Table...");

//     //     Season winter2023 = new Season();
//     //     winter2023.setId(new SeasonId("WINTER", (short) 2023));

//     //     Season spring2023 = new Season();
//     //     spring2023.setId(new SeasonId("SPRING", (short) 2023));

//     //     Season summer2023 = new Season();
//     //     summer2023.setId(new SeasonId("SUMMER", (short) 2023));

//     //     Season fall2023 = new Season();
//     //     fall2023.setId(new SeasonId("FALL", (short) 2023));

//     //     Season winter2024 = new Season();
//     //     winter2024.setId(new SeasonId("WINTER", (short) 2024));

//     //     Season spring2024 = new Season();
//     //     spring2024.setId(new SeasonId("SPRING", (short) 2024));

//     //     Season summer2024 = new Season();
//     //     summer2024.setId(new SeasonId("SUMMER", (short) 2024));

//     //     Season fall2024 = new Season();
//     //     fall2024.setId(new SeasonId("FALL", (short) 2024));

//     //     seasonRepository.saveAll(Arrays.asList(
//     //             winter2023, spring2023, summer2023, fall2023,
//     //             winter2024, spring2024, summer2024, fall2024));
//     // }

//     // private static void seedStudios(StudioRepository studioRepository) {
//     //     System.out.println("-> Seeding Studios Table...");
//     //     Studio mappa = new Studio();
//     //     mappa.setStudioName("MAPPA");
//     //     mappa.setBestAnimes("Jujutsu K,Kimetsu no Yaiba, Chains");

//     //     Studio ufotable = new Studio();
//     //     ufotable.setStudioName("ufotable");
//     //     ufotable.setBestAnimes("Fate Series, Demon Slayer, God Eater");

//     //     studioRepository.saveAll(Arrays.asList(mappa, ufotable));
//     // }

//     // private static void seedAccounts(AccountRepository accountRepository) {
//     //     System.out.println("-> Seeding Accounts Table...");
//     //     Account admin01 = new Account();

//     //     // admin01 - 123
//     //     admin01.setUsername("admin01");
//     //     admin01.setHashedPassword(HashUtil.encode("123"));
//     //     admin01.setUserRole(Account.Role.ADMIN);

//     //     // admin02 - 123
//     //     Account admin02 = new Account();

//     //     admin02.setUsername("admin02");
//     //     admin02.setHashedPassword(HashUtil.encode("123"));
//     //     admin02.setUserRole(Account.Role.ADMIN);

//     //     accountRepository.saveAll(Arrays.asList(admin01, admin02));

//     //     // user01 - 1234
//     //     Account user01 = new Account();
//     //     user01.setUsername("user01");
//     //     user01.setHashedPassword(HashUtil.encode("1234"));
//     //     user01.setUserRole(Account.Role.USER);

//     //     // user02 - 1234
//     //     Account user02 = new Account();
//     //     user02.setUsername("user02");
//     //     user02.setHashedPassword(HashUtil.encode("1234"));
//     //     user02.setUserRole(Account.Role.USER);

//     //     // user02 - 1234
//     //     Account user03 = new Account();
//     //     user03.setUsername("user03");
//     //     user03.setHashedPassword(HashUtil.encode("1234"));
//     //     user03.setUserRole(Account.Role.USER);

//     //     accountRepository.saveAll(Arrays.asList(user01, user02, user03));
//     // }

//     // private static void seedAnimes(AnimeRepository animeRepo, StudioRepository studioRepo,
//     //         SeasonRepository seasonRepo) {
//     //     System.out.println("-> Seeding Animes Table...");

//     //     Studio mappa = studioRepo.findBy("studioName", "MAPPA").getFirst();
//     //     Season fall2024 = seasonRepo.findBy("id", new SeasonId("FALL", (short) 2024))
//     //             .getFirst();

//     //     // Jujutsu Kaisen
//     //     Anime anime1 = new Anime();
//     //     anime1.setTitle("Jujutsu Kaisen");
//     //     anime1.setIntroduction(
//     //             "Yuuji Itadori, a kind-hearted teenager with exceptional physical abilities, finds himself thrust into the world of curses and sorcery after a fateful encounter with a cursed object. Determined to protect");
//     //     anime1.setTotalEpisodes((short) 24);
//     //     anime1.setScheduleDay(DayOfWeek.SATURDAY);
//     //     anime1.setScheduleTime(LocalTime.of(17, 0)); // 5:00 PM
//     //     anime1.setPosterUrl("a1.png");
//     //     anime1.setAnimeType(Anime.AnimeType.TV);
//     //     anime1.setAnimeStatus(Anime.AnimeStatus.COMPLETED);
//     //     anime1.setStudio(mappa);
//     //     anime1.setSeason(fall2024);

//     //     animeRepo.save(anime1);

//     //     // Kimetsu no Yaiba
//     //     Anime anime2 = new Anime();
//     //     anime2.setTitle("Kimetsu no Yaiba");
//     //     anime2.setIntroduction("In Taisho-era Japan, a kind-hearted ...");
//     //     anime2.setTotalEpisodes((short) 26);
//     //     anime2.setScheduleDay(DayOfWeek.SUNDAY);
//     //     anime2.setScheduleTime(LocalTime.of(12, 0)); // 12:
//     //     anime2.setPosterUrl("a1.png");
//     //     anime2.setAnimeType(Anime.AnimeType.TV);
//     //     anime2.setAnimeStatus(Anime.AnimeStatus.ONGOING);

//     //     anime2.setStudio(mappa);
//     //     anime2.setSeason(fall2024);

//     //     animeRepo.save(anime2);
//     // }

//     // private static void seedEpisodes(EpisodeRepository episodeRepo, AnimeRepository animeRepo) {
//     //     System.out.println("-> Seeding Episodes Table...");

//     //     final String juTitle = "Jujutsu Kaisen";
//     //     Anime anime1 = animeRepo.findBy("title", juTitle).getFirst();
//     //     ;

//     //     // 24 episodes
//     //     for (int i = 1; i <= 24; i++) {
//     //         Episode ep = new Episode();
//     //         ep.setAnime(anime1);
//     //         ep.setEpisodeNumber((short) i);
//     //         ep.setEpisodeTitle(juTitle + " - Episode " + i);
//     //         ep.setAirDate(LocalDateTime.now().plusDays(i * 7)); // weekly
//     //         episodeRepo.save(ep);
//     //     }

//     //     final String kimetsuTitle = "Kimetsu no Yaiba";
//     //     Anime anime2 = animeRepo.findBy("title", kimetsuTitle).getFirst();
//     //     // 26 episodes
//     //     for (int i = 1; i <= 26; i++) {
//     //         Episode ep = new Episode();
//     //         ep.setAnime(anime2);
//     //         ep.setEpisodeNumber((short) i);
//     //         ep.setEpisodeTitle(kimetsuTitle + " - Episode " + i);
//     //         ep.setAirDate(LocalDateTime.now().plusDays(i * 7)); // weekly
//     //         episodeRepo.save(ep);
//     //     }
//     // }

//     // private static void seedTrackings(TrackingRepository trackingRepo, AccountRepository accRepo,
//     //         AnimeRepository animeRepo) {
//     //     System.out.println("-> Seeding Trackings Table...");

//     //     Account user01 = accRepo.findByUsername("user01").isPresent()
//     //             ? accRepo.findByUsername("user01").get()
//     //             : null;
//     //     Anime anime1 = animeRepo.findBy("title", "Jujutsu Kaisen").getFirst();
//     //     Tracking tracking = new Tracking();
//     //     tracking.setAccount(user01);
//     //     tracking.setAnime(anime1);

//     //     tracking.setTrackingStatus(Tracking.TrackingStatus.WATCHING);
//     //     tracking.setLastWatchedEpisode((short) 5);
//     //     tracking.setRating((byte) 3);
//     //     tracking.setNote("TV360, good anime");
//     //     trackingRepo.save(tracking);
//     // }

//     // private static void seedNotifications(NotificationRepository notifRepo, AccountRepository accRepo,
//     //         AnimeRepository animeRepo) {
//     //     System.out.println("-> Seeding Notifications Table...");
//     //     Account user01 = accRepo.findByUsername("user01").orElseThrow();
//     //     Notification notif1 = new Notification();
//     //     Anime anime1 = animeRepo.findBy("title", "Jujutsu Kaisen").getFirst();

//     //     notif1.setAccount(user01);
//     //     notif1.setNotifiableType(Notification.NotifiableType.EPISODE_RELEASE);
//     //     notif1.setNotifiableId(anime1.getId().longValue());

//     //     notif1.setCreatedAt(LocalDateTime.now());
//     //     notif1.setTitle("New episode released for " + anime1.getTitle());
//     //     notif1.setIsRead(false);
//     //     notifRepo.save(notif1);
//     }

// }
