package liga.medical.medicalmonitoring.core.hometask5;

public interface AntiI {
    void logMessageFromDailyQueue();

    void logMessageFromAlertQueue();

    void logMessageFromErrorQueue();
}
