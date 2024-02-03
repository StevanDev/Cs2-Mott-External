DROP
DATABASE IF EXISTS luckySix;
     CREATE
DATABASE luckySix;
            USE
luckySix;

CREATE TABLE combination
(
    combinationID INT PRIMARY KEY AUTO_INCREMENT,
    number1       INT NOT NULL,
    number2       INT NOT NULL,
    number3       INT NOT NULL,
    number4       INT NOT NULL,
    number5       INT NOT NULL,
    number6       INT NOT NULL
);

CREATE TABLE ticket
(
    ticketID     INT PRIMARY KEY AUTO_INCREMENT,
    round        INT      NOT NULL,
    combination1 INT      NOT NULL,
    combination2 INT      NOT NULL,
    combination3 INT      NOT NULL,
    combination4 INT      NOT NULL,
    combination5 INT      NOT NULL,
    timeCreated  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE ticket
    ADD FOREIGN KEY (combination1) REFERENCES combination (combinationID);
ALTER TABLE ticket
    ADD FOREIGN KEY (combination2) REFERENCES combination (combinationID);
ALTER TABLE ticket
    ADD FOREIGN KEY (combination3) REFERENCES combination (combinationID);
ALTER TABLE ticket
    ADD FOREIGN KEY (combination4) REFERENCES combination (combinationID);
ALTER TABLE ticket
    ADD FOREIGN KEY (combination5) REFERENCES combination (combinationID);

CREATE TABLE result
(
    resultID  INT PRIMARY KEY AUTO_INCREMENT,
    round     INT          NOT NULL,
    numbers   VARCHAR(200) NOT NULL,
    timeEnded DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE win
(
    winID         INT PRIMARY KEY AUTO_INCREMENT,
    combinationID INT NOT NULL,
    resultID      INT NOT NULL
);

ALTER TABLE win
    ADD FOREIGN KEY (combinationID) REFERENCES combination (combinationID);
ALTER TABLE win
    ADD FOREIGN KEY (resultID) REFERENCES result (resultID);

CREATE TABLE draw
(
    drawID      INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(200) NOT NULL,
    description VARCHAR(200) NOT NULL,
    drawTime    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO combination
VALUES (1, 1, 2, 3, 4, 5, 6),
       (2, 7, 8, 9, 10, 11, 12),
       (3, 13, 14, 15, 16, 17, 18),
       (4, 19, 20, 21, 22, 23, 24),
       (5, 25, 26, 27, 28, 29, 30),
       (6, 31, 32, 33, 34, 35, 1);

INSERT INTO ticket
VALUES (1, 1, 2, 3, 4, 5, 6, DEFAULT),
       (2, 1, 2, 3, 4, 5, 6, DEFAULT),
       (3, 1, 2, 3, 4, 5, 6, DEFAULT),
       (4, 1, 2, 3, 4, 5, 6, DEFAULT),
       (5, 1, 2, 3, 4, 5, 6, DEFAULT),
       (6, 1, 2, 3, 4, 5, 6, DEFAULT);

INSERT INTO result
VALUES (1, 1, '1,2,3,4,5,6', DEFAULT),
       (2, 1, '1,2,3,4,5,6', DEFAULT),
       (3, 1, '1,2,3,4,5,6', DEFAULT),
       (4, 1, '1,2,3,4,5,6', DEFAULT),
       (5, 1, '1,2,3,4,5,6', DEFAULT),
       (6, 1, '1,2,3,4,5,6', DEFAULT);

INSERT INTO win
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 4),
       (5, 5, 5),
       (6, 6, 6);

INSERT INTO draw
VALUES (1, 'Draw 1', 'Draw 1', DEFAULT),
       (2, 'Draw 2', 'Draw 2', DEFAULT),
       (3, 'Draw 3', 'Draw 3', DEFAULT),
       (4, 'Draw 4', 'Draw 4', DEFAULT),
       (5, 'Draw 5', 'Draw 5', DEFAULT),
       (6, 'Draw 6', 'Draw 6', DEFAULT);

-- QUERY GET LAST ROUND
-- SELECT *
-- FROM result
-- ORDER BY resultID DESC LIMIT 1

-- QUERY WRITE COMBINATION
-- INSERT
-- INTO combination(number1, number2, number3, number4, number5, number6)
-- VALUES (?, ?, ?, ?, ?, ?);

-- QUERY WRITE TICKET
-- INSERT
-- INTO ticket(round, combination1, combination2, combination3, combination4, combination5)
-- VALUES (?,?, ?, ?, ?, ?);

-- QUERY WRITE RESULT
-- INSERT
-- INTO result(round, numbers)
-- VALUES (?, ?);

-- QUERY WRITE WIN
-- INSERT INTO win(combinationID, ticketID)
-- VALUES (?, ?)

-- QUERY WRITE DRAW
-- INSERT INTO draw(name, description)
-- VALUES (?, ?)