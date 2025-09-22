package com.project.service;

import java.util.List;

import com.project.entity.Studio;
import com.project.repository.StudioRepository;

public class StudioService {
    private final StudioRepository studioRepository;

    public StudioService() {
        studioRepository = new StudioRepository();
    }

    public Studio saveStudio(Studio studio) {
        return studioRepository.save(studio);
    }

    public Studio getStudioByName(String name) {
        List<Studio> list = studioRepository.findBy("studioName", name);
        if(list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
