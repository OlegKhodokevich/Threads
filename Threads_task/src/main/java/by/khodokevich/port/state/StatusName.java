package by.khodokevich.port.state;

public enum StatusName {
    NEWCOMER("newcomer"),
    WAITING("waiting"),
    IN_WORK("in work"),
    DEPARTING("departing");

    private String state;

    StatusName(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
