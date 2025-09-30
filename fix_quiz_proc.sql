DROP PROCEDURE IF EXISTS GetQuizQuestions;
DELIMITER //
CREATE PROCEDURE GetQuizQuestions()
BEGIN
    SELECT QuestionID, QuestionText, OptionA, OptionB, OptionC, OptionD, CorrectAnswer
    FROM QuizQuestions;
END //
DELIMITER ; 