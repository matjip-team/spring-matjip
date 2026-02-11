package com.restaurant.matjip.ai.service;

import com.restaurant.matjip.ai.repository.AiPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryPreferenceService {

    private final AiPreferenceRepository aiPreferenceRepository;

    public List<String> getTopCategories(Long userId) {
        if (userId == null) return Collections.emptyList();

        List<Object[]> results =
                aiPreferenceRepository.findTopCategoriesByUser(userId);

        if (results.isEmpty()) return Collections.emptyList();

        return results.stream()
                .map(r -> (String) r[0]) // category name
                .limit(3)
                .toList();
    }
}
