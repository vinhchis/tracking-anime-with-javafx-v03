package com.project.service;

import java.util.List;

import com.project.dto.TrackingDto;
import com.project.entity.Tracking;
import com.project.repository.TrackingRepository;

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
        dto.setTrackingStatus(tracking.getTrackingStatus().toString());
        dto.setLastWatchedEpisode(tracking.getLastWatchedEpisode());
        dto.setScheduleDay(tracking.getScheduleDay().toString());
        dto.setScheduleLocalTime(tracking.getScheduleTime());
        dto.setRating(tracking.getRating());
        dto.setNote(tracking.getNote());

        if (tracking.getAnime() != null) {
            // anime
            dto.setAnimeId(tracking.getAnime().getId());
            dto.setApiId(tracking.getAnime().getApiId());
            dto.setAnimeTitle(tracking.getAnime().getTitle());
            dto.setAnimeStatus(tracking.getAnime().getAnimeStatus().toString());
            dto.setAnimeType(tracking.getAnime().getAnimeType().toString());
            dto.setImageUrl(tracking.getAnime().getPosterUrl());
            dto.setTotalEpisodes(tracking.getAnime().getTotalEpisodes());
             // studio
            if (tracking.getAnime().getStudio() != null) {
                dto.setStudioName(tracking.getAnime().getStudio().getStudioName());
            }
            // season
            if (tracking.getAnime().getSeason() != null) {
                dto.setSeasonName(tracking.getAnime().getSeason().getSeasonName().toString());
                dto.setSeasonYear(tracking.getAnime().getSeason().getSeasonYear());
            }
        }

        return dto;
    }



}
