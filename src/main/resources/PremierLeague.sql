CREATE TABLE clubs (
    club_name VARCHAR(255) NOT NULL,
    abv VARCHAR(3) NOT NULL,
    hex_code VARCHAR(7) NOT NULL,
    logo_link VARCHAR(255) NOT NULL,
    PRIMARY KEY (abv)
);

CREATE TABLE results (
    Division VARCHAR(255) NOT NULL,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    HomeTeam VARCHAR(255) NOT NULL,
    AwayTeam VARCHAR(255) NOT NULL,
    FTHG INT NOT NULL,
    FTAG INT NOT NULL,
    FTR CHAR(1) NOT NULL,
    HTHG INT NOT NULL,
    HTAG INT NOT NULL,
    HTR CHAR(1) NOT NULL,
    Referee VARCHAR(255) NOT NULL,
    HS INT NOT NULL,
    AWS INT NOT NULL,
    HST INT NOT NULL,
    AST INT NOT NULL,
    HF INT NOT NULL,
    AF INT NOT NULL,
    HC INT NOT NULL,
    AC INT NOT NULL,
    HY INT NOT NULL,
    AY INT NOT NULL,
    HRC INT NOT NULL,
    ARC INT NOT NULL,
    PRIMARY KEY (Date, HomeTeam, AwayTeam)
    FOREIGN KEY (HomeTeam) REFERENCES clubs(abv),
    FOREIGN KEY (AwayTeam) REFERENCES clubs(abv),

);

-- Procedure - Inserts the club.
DELIMITER //
CREATE PROCEDURE insert_club(club_name VARCHAR(255), abv VARCHAR(3), hex_code VARCHAR(7), logo_link VARCHAR(255))

BEGIN
    INSERT INTO clubs (club_name, abv, hex_code, logo_link) VALUES (club_name, abv, hex_code, logo_link);
END //
DELIMITER ;

-- Procedure - Get the team with the teamAbbreviation parameter.
DELIMITER /
CREATE PROCEDURE get_club(teamAbbreviation VARCHAR(255))

BEGIN
    SELECT * FROM clubs WHERE abv = teamAbbreviation;
END //
DELIMITER ;

-- Procedure - Returns the abbreviation of the team associated with the teamName.
DELIMITER //
CREATE PROCEDURE get_club_abv(teamName VARCHAR(255))

BEGIN
    SELECT abv FROM clubs WHERE club_name = teamName;
END //
DELIMITER ;

-- Procedure - Inserts the match parameter into the database.
DELIMITER //
CREATE PROCEDURE insert_match(division VARCHAR(255), matchDate DATE, matchTime TIME, homeTeam VARCHAR(255), awayTeam VARCHAR(255), fullTimeHomeGoals INT, fullTimeAwayGoals INT, fullTimeResult CHAR(1), halfTimeHomeGoals INT, halfTimeAwayGoals INT, halfTimeResult CHAR(1), referee VARCHAR(255), homeShots INT, awayShots INT, homeShotsOnTarget INT, awayShotsOnTarget INT, awayFouls INT, homeCorners INT, awayCorners INT, homeYellowCards INT, awayYellowCards INT, homeRedCards INT, awayRedCards INT)

BEGIN
    INSERT INTO results (Division, Date, Time, HomeTeam, AwayTeam, FTHG, FTAG, FTR, HTHG, HTAG, HTR, Referee, HS, AWS, HST, AST, AF, HC, AC, HY, AY, HRC, ARC) VALUES (division, matchDate, matchTime, homeTeam, awayTeam, fullTimeHomeGoals, fullTimeAwayGoals, fullTimeResult, halfTimeHomeGoals, halfTimeAwayGoals, halfTimeResult, referee, homeShots, awayShots, homeShotsOnTarget, awayShotsOnTarget, awayFouls, homeCorners, awayCorners, homeYellowCards, awayYellowCards, homeRedCards, awayRedCards);
END //
DELIMITER ;

-- Procedure - Returns the match played on matchDay played by the HomeTeam against the AwayTeam.
DELIMITER //
CREATE PROCEDURE get_match(matchDay DATE, homeTeam VARCHAR(255), awayTeam VARCHAR(255))

BEGIN
    SELECT * FROM results WHERE Date = matchDay AND HomeTeam = homeTeam AND AwayTeam = awayTeam;
END //
DELIMITER ;

-- Procedure - Returns the aggregate of goals scored(FTHG) by all the HomeTeam.
DELIMITER //
CREATE PROCEDURE get_home_team_goals()

BEGIN
    SELECT SUM(FTHG) FROM results;
END //
DELIMITER ;

-- Procedure - Returns the aggregate of goals scored(FTHG) by all the AwayTeam.
DELIMITER //
CREATE PROCEDURE get_away_team_goals()

BEGIN
    SELECT SUM(FTAG) FROM results;
END //
DELIMITER ;

-- Procedure - Returns all the matches played by oneTeam.
DELIMITER //
CREATE PROCEDURE get_team_matches(oneTeam VARCHAR(255))

BEGIN
    SELECT * FROM results WHERE HomeTeam = oneTeam OR AwayTeam = oneTeam;
END //
DELIMITER ;

-- Procedure - Returns agregate of red cards of one team.
DELIMITER //
CREATE PROCEDURE get_team_red_cards(oneTeam VARCHAR(255))

BEGIN
    DECLARE homeTeamRedCards INT;
    DECLARE awayTeamRedCards INT;
    DECLARE totalRedCards INT;

    SELECT SUM(HRC) INTO homeTeamRedCards FROM results WHERE HomeTeam = oneTeam;
    SELECT SUM(ARC) INTO awayTeamRedCards FROM results WHERE AwayTeam = oneTeam;

    SET totalRedCards = homeTeamRedCards + awayTeamRedCards;
    SELECT totalRedCards;
END //
DELIMITER ;

-- Procedure - Returns the Team (or teams) with more red cards.
DELIMITER //
CREATE PROCEDURE get_team_most_red_cards()

BEGIN
    SELECT c.club_name, c.abv, c.hex_code, c.logo_link, SUM(r.HRC + r.ARC) AS total_red_cards
    FROM clubs c
    JOIN results r ON c.abv = r.HomeTeam OR c.abv = r.AwayTeam
    GROUP BY c.club_name, c.abv, c.hex_code, c.logo_link
    ORDER BY total_red_cards DESC
    LIMIT 3;
END //
DELIMITER ;