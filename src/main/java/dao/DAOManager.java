package dao;

import model.Match;
import model.Team;

import java.util.ArrayList;
import java.util.Date;

public interface DAOManager {
    boolean AddTeam(Team team);
    void ImportTeams(String fileTeams);
    Team GetTeam(String teamAbbreviation);
    String GetTeamAbbreviation(String teamName);
    boolean AddMatch(Match oneMatch);
    void ImportMatches(String fileMatches);
    Match GetMatch(Date matchDay, Team home, Team away);
    int HomeGoals( );
    int AwayGoals( );
    ArrayList<Match> MatchesOfTeam(Team oneTeam);
    int RedCards(Team oneTeam);
    ArrayList<Team> TopRedCards( );
}
