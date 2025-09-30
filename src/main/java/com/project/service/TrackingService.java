package com.project.service;

import java.util.List;

import com.project.dto.TrackingDto;
import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.repository.TrackingRepository;
import com.project.util.MapperUtil;

public class TrackingService {
    public final TrackingRepository trackingRepository;
    public TrackingService() {
        trackingRepository = new TrackingRepository();
    }

    public List<Tracking> getAllTrackings() {
        return trackingRepository.findAll();
    }

    public List<TrackingDto> getTrackingDtos(){
        return trackingRepository.getTrackingFullInfo().stream().map(this::mapperToDto).toList();
    }

    public void saveTracking(Tracking tracking) {
        trackingRepository.save(tracking);
    }
    public void saveAll(List<TrackingDto> trackingDtos) {
        List<Tracking> trackings = trackingDtos.stream().map(dto -> dtoToMapper(dto)).toList();
        trackingRepository.saveAll(trackings);
    }

    public void deleteTracking(Tracking tracking) {
        // delete anime ?
        trackingRepository.delete(tracking);
    }

    public void deleteTrackingById(Long id) {
        trackingRepository.deleteById(id);
    }

    private TrackingDto mapperToDto(Tracking tracking) {
        TrackingDto dto = new TrackingDto();
        // tracking
        dto.setTrackingId(tracking.getId());
        dto.setTrackingStatus(tracking.getTrackingStatus());
        dto.setLastWatchedEpisode(tracking.getLastWatchedEpisode());
        dto.setScheduleDay(tracking.getScheduleDay());
        dto.setScheduleLocalTime(tracking.getScheduleTime());
        dto.setRating(tracking.getRating());
        dto.setNote(tracking.getNote());

        if (tracking.getAnime() != null) {
            // anime
            dto.setAnimeId(tracking.getAnime().getId());
            dto.setApiId(tracking.getAnime().getApiId());
            dto.setAnimeTitle(tracking.getAnime().getTitle());
            dto.setAnimeStatus(tracking.getAnime().getAnimeStatus());
            dto.setAnimeType(tracking.getAnime().getAnimeType());
            dto.setImageUrl(tracking.getAnime().getPosterUrl());
            dto.setTotalEpisodes(tracking.getAnime().getTotalEpisodes() != null ? tracking.getAnime().getTotalEpisodes() : 0);
             // studio
            if (tracking.getAnime().getStudio() != null) {
                dto.setStudioName(tracking.getAnime().getStudio().getStudioName());
            }
            // season
            if (tracking.getAnime().getSeason() != null) {
                dto.setSeasonName(tracking.getAnime().getSeason().getSeasonName());
                dto.setSeasonYear(tracking.getAnime().getSeason().getSeasonYear());
            }
        }

        return dto;
    }

    private Tracking dtoToMapper(TrackingDto dto) {
        Tracking tracking = trackingRepository.findById((long) dto.getTrackingId()).orElseThrow(() -> new IllegalArgumentException("Tracking not found"));
            // update fields
            tracking.setTrackingStatus(dto.getTrackingStatus());
            tracking.setLastWatchedEpisode(dto.getLastWatchedEpisode());
            tracking.setScheduleDay(dto.getScheduleDay());
            tracking.setScheduleTime(dto.getScheduleLocalTime());
            tracking.setRating(dto.getRating());
            tracking.setNote(dto.getNote());
            return tracking;
    }

    public boolean checkExistedAnimeOfTracking(String title) {
        boolean isExisted = trackingRepository.getTrackingFullInfo().stream()
                .filter(t -> t.getAnime() != null && t.getAnime().getTitle().equalsIgnoreCase(title))
                .findFirst().isPresent();
       return isExisted;
    }


    public long countAllAnime() {
        return trackingRepository.count();
    }


    // for watching only
    public List<TrackingScheduleCardDto> getScheduleCardDtos(){
        return trackingRepository.getTrackingsForSchedule().stream()
            .filter(t -> t.getTrackingStatus() == TRACKINGS_STATUS.WATCHING)
            .map(t -> MapperUtil.mapToScheduleCardDto(t)).toList();
    }



}
