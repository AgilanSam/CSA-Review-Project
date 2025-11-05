class ReviewRunner {

  // Calculates total sentiment score of a given text
  public static float computeMoodScore(String textBlock) {
    float runningTotal = 0.0f;
    String[] tokenList = textBlock.split(" ");

    for (String token : tokenList) {
      token = Review.removePunctuation(token.toLowerCase());
      runningTotal += Review.sentimentVal(token);
    }

    return runningTotal;
  }

  public static void main(String[] args) {

    String fileContents = Review.textToString("data.txt");
    String[] reviewChunks = fileContents.split("(?=\\d{1,2}/10)"); // detect ratings like 8/10, 6/10, etc.

    int reviewCounter = 0;
    float totalMoodScore = 0.0f;

    for (int idx = 0; idx < reviewChunks.length; idx++) {
      String currentChunk = reviewChunks[idx].trim();

      // ignore useless blank sections
      if (currentChunk.length() < 5) continue;

      if (currentChunk.contains("\"")) {
        int openQuote = currentChunk.indexOf("\"");
        int closeQuote = currentChunk.lastIndexOf("\"");

        if (openQuote >= 0 && closeQuote > openQuote) {
          String extractedReview = currentChunk.substring(openQuote + 1, closeQuote);

          float moodValue = computeMoodScore(extractedReview);
          System.out.println("Review #" + reviewCounter + ": " + extractedReview + "\n");

          totalMoodScore += moodValue;
          reviewCounter++;

        } else {
          System.out.println("Review #" + idx + " didn't contain valid quoted text. Ignored.\n");
        }

      } else {
        System.out.println("Review #" + idx + " had no quote marks. Skipping...\n");
      }
    }

    if (reviewCounter > 0) {
      float averageScore = totalMoodScore / reviewCounter;

      System.out.println("Average sentiment score across reviews: " + averageScore + ".");

      if (averageScore > 4.0f) {
        System.out.println("Overall: Highly positive feedback — people loved it!");
      } else if (averageScore > 3.0f) {
        System.out.println("Overall: Generally positive — viewers enjoyed it.");
      } else {
        System.out.println("Overall: Mostly negative — reviewers were not impressed.");
      }

    } else {
      System.out.println("No usable reviews — sentiment analysis not possible.");
    }
  }
}
