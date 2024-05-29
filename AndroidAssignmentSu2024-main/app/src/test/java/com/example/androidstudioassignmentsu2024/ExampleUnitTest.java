package com.example.androidstudioassignmentsu2024;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	private FlashcardManagerSingleton flashcards;
    private Flashcard flashcard1;
    private Flashcard flashcard2;
    private Flashcard flashcard3;
    @Before
    public void setup() {
		flashcards = FlashcardManagerSingleton.getInstance();
		flashcard1 = new Flashcard("1", "2");
    	flashcard2 = new Flashcard("3", "4");
		flashcard3 = new Flashcard("5", "6");
    } 
    // TODO 7: Write a unit test for ensuring correct behavior of the addFlashcard function you implemented in TODO 2 in FlashcardManagerSingletons
    @Test
    public void add_flashcard_test() {
        flashcards.addFlashcard(flashcard1);
		flashcards.addFlashcard(flashcard2);
		flashcards.addFlashcard(flashcard3);

		assertEquals(3, flashcards.getFlashcards().size());
    }
}
