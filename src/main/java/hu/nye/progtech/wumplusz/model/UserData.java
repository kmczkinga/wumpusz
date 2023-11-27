package hu.nye.progtech.wumplusz.model;

public class UserData {

    private String username;

    private Integer wins;

    public UserData(String username, Integer wins) {
        this.username = username;
        this.wins = wins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }
}
