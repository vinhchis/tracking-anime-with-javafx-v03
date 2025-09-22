package com.project.dto;

import java.util.List;

import com.project.dto.JikanAnimeResponse.AnimeData;

public class JikanSearchResponse {
    private List<AnimeData> data;

    public List<AnimeData> getData() {
        return data;
    }

    public void setData(List<AnimeData> data) {
        this.data = data;
    }
}
