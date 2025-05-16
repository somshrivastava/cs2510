import tester.*;

// represents an exam question
interface IExamQuestion {
  // determines if the given answer is correct
  boolean isCorrect(String givenAnswer);

  // determines if two questions are the same
  boolean sameQuestion(IExamQuestion givenQuestion);

  // determines if the question is TrueFalse
  boolean sameTrueFalse(TrueFalse givenQuestion);

  // determines if the question is FourChoice
  boolean sameFourChoice(FourChoice givenQuestion);

  // determines if the question is FreeResponse
  boolean sameFreeResponse(FreeResponse givenQuestion);
}

// represents a true or false question
class TrueFalse {
  String question;
  String answer;
  int points;

  TrueFalse(String question, String answer, int points) {
    this.question = question;
    this.answer = answer;
    this.points = points;
  }

  /*
   * Fields: 
   * this.question -> String 
   * this.answer -> String 
   * this.points -> int
   */

  // determines if the given answer is correct
  public boolean isCorrect(String givenAnswer) {
    return this.answer.equals(givenAnswer);
  }

  // determines if two questions are the same
  boolean sameQuestion(IExamQuestion givenQuestion) {
    return givenQuestion.sameTrueFalse(this);
  }

  // determines if the question is TrueFalse
  boolean sameTrueFalse(TrueFalse givenQuestion) {
    return this.question.equals(givenQuestion.question) && this.answer.equals(givenQuestion.answer)
        && this.points == givenQuestion.points;
  }

  // determines if the question is FourChoice
  boolean sameFourChoice(FourChoice givenQuestion) {
    return false;
  }

  // determines if the question is FreeResponse
  boolean sameFreeResponse(FreeResponse givenQuestion) {
    return false;
  }
}

// represents a four choice question
class FourChoice {
  String question;
  String answer;
  String choiceA;
  String choiceB;
  String choiceC;
  String choiceD;
  int points;

  FourChoice(String question, String answer, String choiceA, String choiceB, String choiceC,
      String choiceD, int points) {
    this.question = question;
    this.answer = answer;
    this.choiceA = choiceA;
    this.choiceB = choiceB;
    this.choiceC = choiceC;
    this.choiceD = choiceD;
    this.points = points;
  }

  /*
   * Fields: 
   * this.answer -> String 
   * this.choiceA -> String 
   * this.choiceB -> String
   * this.choiceC -> String 
   * this.choiceD -> String 
   * this.points -> int
   */

  // determines if the given answer is correct
  public boolean isCorrect(String givenAnswer) {
    return this.answer.equals(givenAnswer);
  }

  // determines if two questions are the same
  boolean sameQuestion(IExamQuestion givenQuestion) {
    return givenQuestion.sameFourChoice(this);
  }

  // determines if the question is TrueFalse
  boolean sameTrueFalse(TrueFalse givenQuestion) {
    return false;
  }

  // determines if the question is FourChoice
  boolean sameFourChoice(FourChoice givenQuestion) {
    return this.question.equals(givenQuestion.question)
        && this.choiceA.equals(givenQuestion.choiceA) && this.choiceB.equals(givenQuestion.choiceB)
        && this.choiceC.equals(givenQuestion.choiceC) && this.choiceD.equals(givenQuestion.choiceD)
        && this.answer.equals(givenQuestion.answer) && this.points == givenQuestion.points;
  }

  // determines if the question is FreeResponse
  boolean sameFreeResponse(FreeResponse givenQuestion) {
    return false;
  }
}

// represents a free response question
class FreeResponse {
  String question;
  String answer;
  int points;

  /*
   * Fields: 
   * this.question -> String 
   * this.answer -> String 
   * this.points -> int
   */

  FreeResponse(String question, String answer, int points) {
    this.question = question;
    this.answer = answer;
    this.points = points;
  }

  // determines if the given answer is correct
  public boolean isCorrect(String givenAnswer) {
    return this.answer.equals(givenAnswer);
  }
  
//determines if two questions are the same
  boolean sameQuestion(IExamQuestion givenQuestion) {
    return givenQuestion.sameFreeResponse(this);
  }

  // determines if the question is TrueFalse
  boolean sameTrueFalse(TrueFalse givenQuestion) {
    return false;
  }

  // determines if the question is FourChoice
  boolean sameFourChoice(FourChoice givenQuestion) {
    return false;
  }

  // determines if the question is FreeResponse
  boolean sameFreeResponse(FreeResponse givenQuestion) {
    return this.question.equals(givenQuestion.question) && this.answer.equals(givenQuestion.answer)
        && this.points == givenQuestion.points;
  }
}

// examples and tests for exam questions
class ExampleExamQuestions {
  
}