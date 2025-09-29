package com.project.dto;

import com.project.entity.Anime;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.entity.Tracking;

import lombok.Data;

@Data
public class DataSaveDto {
    Anime anime;
    Season season;
    Studio studio;
}
