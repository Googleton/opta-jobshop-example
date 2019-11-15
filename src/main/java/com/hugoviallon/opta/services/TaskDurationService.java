package com.hugoviallon.opta.services;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class TaskDurationService {
    public static Duration getTaskDuration() {
        return Duration.ofHours(ThreadLocalRandom.current().nextInt(5, 24));
    }
}
