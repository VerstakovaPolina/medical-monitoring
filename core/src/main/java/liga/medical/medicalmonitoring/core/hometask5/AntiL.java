package liga.medical.medicalmonitoring.core.hometask5;

public class AntiL {
    public void sendResponse() {
        System.out.println("response");
    }
}

class AntiL2 extends AntiL {
    @Override
    public String sendResponse() {
        return "response";
    }
}
