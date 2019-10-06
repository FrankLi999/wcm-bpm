package com.bpwizard.wcm.repo.tensorflow.jet.controller;

import com.hazelcast.core.IMap;
import java.util.List;

import static java.util.Arrays.asList;

public class SampleReviews {
    public static void populateReviewsMap(IMap<Long, String> reviewsMap) {
        List<String> reviews = asList(
                "the movie was good",
                "the movie was bad",
                "excellent movie best piece ever",
                "absolute disaster, worst movie ever",
                "had both good and bad parts, excellent casting, but camera was poor");

        long key = 0;
        for (String review : reviews) {
            reviewsMap.put(key++, review);
        }
    }
}
