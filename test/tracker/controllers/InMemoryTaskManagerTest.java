package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}