package com.example.androidstudioassignmentsu2024;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    // TODO 7: Write a unit test for ensuring correct behavior of the addFlashcard function you implemented in TODO 2 in FlashcardManagerSingletons
    @Test
    public void add_flashcard_test() {
	FlashcardManagerSingleton flashcardTest = new FlashcardManagerSingleton();
	Flashcard flashcard = new Flashcard("1+1","2");
	Flashcard flashcard2 = new Flashcard("2+2","4");
	flashcardTest.addFlashcard(flashcard);
	flashcardTest.addFlashcard(flashcard2);
	for(int i = 0; i < flashcardTest.getFlashcards(); i++) {
	    System.out.println("Flashcard #" + (i + 1) + ":");
	    System.out.println("Question: " + flashcardTest.getFlashcards().get(i).getQuestion());
	    System.out.println("Answer: " + flashcardTest.getFlashcards().get(i).getAnswer());
	}
    }
}
