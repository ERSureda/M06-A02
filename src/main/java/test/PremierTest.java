package test;

import model.Match;
import model.Team;

import dao.DAOFactory;
import dao.DAOManager;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PremierTest {
    public static void main (String[] args) {
        DAOFactory factory = new DAOFactory();
        DAOManager dao = factory.createDAOManager();
        // dao.ImportTeams("C:\\Users\\Eduard Ribas\\IdeaProjects\\M06-A2\\src\\main\\resources\\clubs.csv");
        // dao.ImportMatches("C:\\Users\\Eduard Ribas\\IdeaProjects\\M06-A2\\src\\main\\resources\\results.csv");

        // Ex_03
        // Team team = dao.GetTeam("ARS");
        // System.out.println(team.toString());

        // Ex_04
        // System.out.println(dao.GetTeamAbbreviation("Arsenal"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = dateFormat.parse("2022-08-27");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date DataSql = new java.sql.Date(data.getTime());

        /* EX_07
        Team team = dao.GetTeam("SOU");
        Team team_2 = dao.GetTeam("MNU");
        System.out.println(team.toString());
        System.out.println(team_2.toString());
        Match match = dao.GetMatch(DataSql, team, team_2);
        System.out.println(match.toString());
        */

        // EX_08
        // System.out.println(dao.HomeGoals());

        // EX_09
        // System.out.println(dao.AwayGoals());

        // EX_10
        /*Team team = dao.GetTeam("MNU");
        ArrayList<Match> matchs = dao.MatchesOfTeam(team);

        for (Match match: matchs) {
            System.out.println(match.toString());
        }
        */

        // EX_11
        // Team team = dao.GetTeam("MNU");
        // System.out.println(dao.RedCards(team));

        // EX_12
        ArrayList<Team> teams = dao.TopRedCards();

        for (Team team: teams) {
            System.out.println(team.toString());
        }


    }
}