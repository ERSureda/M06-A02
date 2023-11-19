package dao;

import model.Match;
import model.Team;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.ParseException;

public class DAOManagerJDBCImpl implements DAOManager {

    Connection dbConnection;

    // Connection Constructor FET
    public DAOManagerJDBCImpl() {
        String connectionUrl = "jdbc:mysql://localhost:3306/premierleague";
        String user = "root";
        String password = "";

        // Try Connection
        try {
            this.dbConnection = DriverManager.getConnection(connectionUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ex_01 FET
    public boolean AddTeam(Team team) {
        // Result boolean
        boolean result = false;
        try {
            // Prepare the call
            String storedProcedure = "{call insert_club(?,?,?,?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Set input parameters
            callableStatement.setString(1, team.getClub_name());
            callableStatement.setString(2, team.getAbv());
            callableStatement.setString(3, team.getHex_code());
            callableStatement.setString(4, team.getLogo_link());

            // Execute the stored procedure
            result = callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return result;
    }

    // Ex_02 FET
    public void ImportTeams(String fileTeams) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileTeams))) {
            String line;
            // Read First Line
            br.readLine();
            // Read each line
            while ((line = br.readLine()) != null) {
                // Separate lines

                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                String club_name = tokenizer.nextToken().trim();
                String abv = tokenizer.nextToken().trim();
                String hex_code = tokenizer.nextToken().trim();
                String logo_link = tokenizer.nextToken().trim();
                Team team = new Team(club_name, abv, hex_code, logo_link);
                // Call Add Team
                AddTeam(team);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ex_03 FET
    public Team GetTeam(String teamAbbreviation) {
        // Result boolean
        boolean result = false;
        // Team
        Team team = null;
        try {
            // Prepare the call
            String storedProcedure = "{call get_club(?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Set input parameters
            callableStatement.setString(1, teamAbbreviation);

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    // Get Result
                    String club_name = resultSet.getString(1);
                    String abv = resultSet.getString(2);
                    String hex_code = resultSet.getString(3);
                    String logo_link = resultSet.getString(4);

                    // Create Team
                    team = new Team(club_name, abv, hex_code, logo_link);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return team;
    }

    // Ex_04 FET
    public String GetTeamAbbreviation(String teamName) {
        // Result boolean
        boolean result = false;
        // Abv value
        String abv = "false";
        try {
            // Prepare the call
            String storedProcedure = "{call get_club_abv(?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Set input parameters
            callableStatement.setString(1, teamName);

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    abv = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return abv;
    }

    // Ex_05 FET
    public boolean AddMatch(Match oneMatch) {
        // Result boolean
        boolean result = false;
        try {
            // Prepare the call
            String storedProcedure = "{call insert_match(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);
            // Set input parameters
            callableStatement.setString(1, oneMatch.getDivision());
            callableStatement.setDate(2, oneMatch.getDate());
            callableStatement.setTime(3, oneMatch.getTime());
            callableStatement.setString(4, oneMatch.getHomeTeam());
            callableStatement.setString(5, oneMatch.getAwayTeam());
            callableStatement.setInt(6, oneMatch.getFullTimeHomeGoals());
            callableStatement.setInt(7, oneMatch.getFullTimeAwayGoals());
            callableStatement.setString(8, oneMatch.getFullTimeResult());
            callableStatement.setInt(9, oneMatch.getHalfTimeHomeGoals());
            callableStatement.setInt(10, oneMatch.getHalfTimeAwayGoals());
            callableStatement.setString(11, oneMatch.getHalfTimeResult());
            callableStatement.setString(12, oneMatch.getReferee());
            callableStatement.setInt(13, oneMatch.getHS());
            callableStatement.setInt(14, oneMatch.getAS());
            callableStatement.setInt(15, oneMatch.getHST());
            callableStatement.setInt(16, oneMatch.getAST());
            callableStatement.setInt(17, oneMatch.getHF());
            callableStatement.setInt(18, oneMatch.getAF());
            callableStatement.setInt(19, oneMatch.getHC());
            callableStatement.setInt(20, oneMatch.getAC());
            callableStatement.setInt(21, oneMatch.getHY());
            callableStatement.setInt(22, oneMatch.getHRC());
            callableStatement.setInt(23, oneMatch.getARC());
            // Execute the stored procedure
            result = callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return result;
    }

    // Ex_06 FET
    public void ImportMatches(String fileMatches) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileMatches))) {
            String line;
            // Read First Line
            br.readLine();
            // Read each line
            while ((line = br.readLine()) != null) {
                // Separate lines
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                String division = tokenizer.nextToken().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = dateFormat.parse(tokenizer.nextToken().trim());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime localTime = LocalTime.parse(tokenizer.nextToken().trim(), formatter);
                java.sql.Time time = java.sql.Time.valueOf(localTime);
                String homeTeam = tokenizer.nextToken().trim();
                String awayTeam = tokenizer.nextToken().trim();
                int fullTimeHomeGoals = Integer.parseInt(tokenizer.nextToken());
                int fullTimeAwayGoals = Integer.parseInt(tokenizer.nextToken());
                String fullTimeResult = tokenizer.nextToken().trim();
                int halfTimeHomeGoals = Integer.parseInt(tokenizer.nextToken());
                int halfTimeAwayGoals = Integer.parseInt(tokenizer.nextToken());
                String halfTimeResult = tokenizer.nextToken().trim();
                String referee = tokenizer.nextToken().trim();
                int hs = Integer.parseInt(tokenizer.nextToken());
                int as = Integer.parseInt(tokenizer.nextToken());
                int hst = Integer.parseInt(tokenizer.nextToken());
                int ast = Integer.parseInt(tokenizer.nextToken());
                int hf = Integer.parseInt(tokenizer.nextToken());
                int af = Integer.parseInt(tokenizer.nextToken());
                int hc = Integer.parseInt(tokenizer.nextToken());
                int ac = Integer.parseInt(tokenizer.nextToken());
                int hy = Integer.parseInt(tokenizer.nextToken());
                int ay = Integer.parseInt(tokenizer.nextToken());
                int hrc = Integer.parseInt(tokenizer.nextToken());
                int arc = Integer.parseInt(tokenizer.nextToken());
                Match match = new Match(division, sqlDate, time, homeTeam, awayTeam, fullTimeHomeGoals, fullTimeAwayGoals, fullTimeResult, halfTimeHomeGoals, halfTimeAwayGoals, halfTimeResult, referee, hs, as, hst, ast, hf, af, hc, ac, hy, ay, hrc, arc);
                // Call Add Team
                AddMatch(match);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Ex_07 FET
    public Match GetMatch(Date matchDay, Team home, Team away) {
        // Result boolean
        boolean result = false;
        // Team
        Match match = null;
        try {
            // Prepare the call
            String storedProcedure = "{call get_match(?, ?, ?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Set input parameters
            callableStatement.setDate(1, (java.sql.Date) matchDay);
            callableStatement.setString(2, home.getClub_name());
            callableStatement.setString(3, away.getClub_name());

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String division = resultSet.getString(2);
                    Date date = resultSet.getDate(3);
                    Time time = resultSet.getTime(4);
                    String homeTeam = resultSet.getString(5);
                    String awayTeam = resultSet.getString(6);
                    int fullTimeHomeGoals = resultSet.getInt(7);
                    int fullTimeAwayGoals = resultSet.getInt(8);
                    String fullTimeResult = resultSet.getString(9);
                    int halfTimeHomeGoals = resultSet.getInt(10);
                    int halfTimeAwayGoals = resultSet.getInt(11);
                    String halfTimeResult = resultSet.getString(12);
                    String referee = resultSet.getString(13);
                    int hs = resultSet.getInt(14);
                    int as = resultSet.getInt(15);
                    int hst = resultSet.getInt(16);
                    int ast = resultSet.getInt(17);
                    int hf = resultSet.getInt(18);
                    int af = resultSet.getInt(19);
                    int hc = resultSet.getInt(20);
                    int ac = resultSet.getInt(21);
                    int hy = resultSet.getInt(22);
                    int ay = resultSet.getInt(23);
                    int hrc = resultSet.getInt(24);
                    int arc = resultSet.getInt(25);

                    // Create Team
                    match = new Match(division, date, time, homeTeam, awayTeam, fullTimeHomeGoals, fullTimeAwayGoals, fullTimeResult, halfTimeHomeGoals, halfTimeAwayGoals, halfTimeResult, referee, hs, as, hst, ast, hf, af, hc, ac, hy, ay, hrc, arc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return match;
    }

    // Ex_08 FET
    public int HomeGoals( ) {
        // Result boolean
        boolean result = false;
        // Goals Number
        int goals = 0;
        try {
            // Prepare the call
            String storedProcedure = "{call get_home_team_goals()}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    goals = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return goals;
    }

    // Ex_09 FET
    public int AwayGoals( ) {
        // Result boolean
        boolean result = false;
        // Goals Number
        int goals = 0;
        try {
            // Prepare the call
            String storedProcedure = "{call get_away_team_goals()}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    goals = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return goals;
    }

    // Ex_10 FET
    public ArrayList<Match> MatchesOfTeam(Team oneTeam) {
        // Result boolean
        boolean result = false;
        // Team
        ArrayList<Match> matches = new ArrayList<Match>();
        try {
            // Prepare the call
            String storedProcedure = "{call get_team_matches(?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Set input parameters
            callableStatement.setString(1, oneTeam.getClub_name());

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    // Get Result
                    int id = resultSet.getInt(1);
                    String division = resultSet.getString(2);
                    Date date = resultSet.getDate(3);
                    Time time = resultSet.getTime(4);
                    String homeTeam = resultSet.getString(5);
                    String awayTeam = resultSet.getString(6);
                    int fullTimeHomeGoals = resultSet.getInt(7);
                    int fullTimeAwayGoals = resultSet.getInt(8);
                    String fullTimeResult = resultSet.getString(9);
                    int halfTimeHomeGoals = resultSet.getInt(10);
                    int halfTimeAwayGoals = resultSet.getInt(11);
                    String halfTimeResult = resultSet.getString(12);
                    String referee = resultSet.getString(13);
                    int hs = resultSet.getInt(14);
                    int as = resultSet.getInt(15);
                    int hst = resultSet.getInt(16);
                    int ast = resultSet.getInt(17);
                    int hf = resultSet.getInt(18);
                    int af = resultSet.getInt(19);
                    int hc = resultSet.getInt(20);
                    int ac = resultSet.getInt(21);
                    int hy = resultSet.getInt(22);
                    int ay = resultSet.getInt(23);
                    int hrc = resultSet.getInt(24);
                    int arc = resultSet.getInt(25);

                    // Create Team
                    Match match;
                    match = new Match(division, date, time, homeTeam, awayTeam, fullTimeHomeGoals, fullTimeAwayGoals, fullTimeResult, halfTimeHomeGoals, halfTimeAwayGoals, halfTimeResult, referee, hs, as, hst, ast, hf, af, hc, ac, hy, ay, hrc, arc);
                    // Add match to list
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return matches;
    }

    // Ex_11 FET
    public int RedCards(Team oneTeam) {
        // Result boolean
        boolean result = false;
        // Goals Number
        int redCards = 0;
        try {
            // Prepare the call
            String storedProcedure = "{call get_team_red_cards(?)}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            callableStatement.setString(1, oneTeam.getClub_name());

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                // Get Data
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    redCards = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return redCards;
    }

    // Ex_12
    public ArrayList<Team> TopRedCards( ) {
        // Result boolean
        boolean result = false;
        // Team
        ArrayList<Team> teams = new ArrayList<Team>();
        try {
            // Prepare the call
            String storedProcedure = "{call get_team_most_red_cards()}";

            // Prepare Call
            CallableStatement callableStatement = dbConnection.prepareCall(storedProcedure);

            // Execute the stored procedure
            result = callableStatement.execute();

            if(result) {
                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    // Get Result
                    String club_name = resultSet.getString(1);
                    String abv = resultSet.getString(2);
                    String hex_code = resultSet.getString(3);
                    String logo_link = resultSet.getString(4);

                    // Create Team
                    Team team;
                    team = new Team(club_name, abv, hex_code, logo_link);
                    // Add match to list
                    teams.add(team);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return
        return teams;
    }
}
