package cz.muni.fi.pb162.hw01;

import cz.muni.fi.pb162.hw01.cmd.Messages;
import cz.muni.fi.pb162.hw01.impl.Application;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrAndOutNormalized;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;

public class ApplicationTest {
    @Test()
    @DisplayName("3x3 board ends on full")
    void  basic3x3scenarioFullBoard() throws Exception {
        var moves = new String[] { "0 0", "0 1", "0 2", "1 0",  "1 1", "1 2", "2 1", "2 0", "2 2" };
        var args = new String[]{};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(9));
        });
    }

    @Test()
    @DisplayName("4x4 board where x wins with 3 in row")
    void  basic4x4scenarioFirstWins() throws Exception {
        var moves = new String[] { "0 0", "0 1", "1 1", "1 0",  "1 2", "0 2", "1 3" };
        var args = new String[]{"-s", "4", "-w", "3", "-h", "1"};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(7));
            softly.assertThat(output).contains(Messages.GAME_WINNER.formatted("x"));
        });
    }

    @Test()
    @DisplayName("4x4 board where @ wins with 3 in column")
    void  basic4x4scenarioSecondWins() throws Exception {
        var moves = new String[] { "0 1", "1 1", "1 0", "2 1",  "0 3", "3 1" };
        var args = new String[]{"-s", "4", "-w", "3", "-h", "1", "-p", "#@"};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(6));
            softly.assertThat(output).contains(Messages.GAME_WINNER.formatted("@"));
        });
    }

    @Test()
    @DisplayName("3x3 board where * wins with 3 in row")
    void  basic3x3scenarioFirstWins() throws Exception {
        var moves = new String[] { "0 0", "1 0", "0 1", "1 1", "0 2" };
        var args = new String[]{"-s", "3", "-w", "3", "-p", "*#"};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(5));
            softly.assertThat(output).contains(Messages.GAME_WINNER.formatted("*"));
        });
    }

    @Test()
    @DisplayName("3x3 board where o wins with 3 in column")
    void  basic3x3scenarioSecondWins() throws Exception {
        var moves = new String[] { "0 0", "0 1", "1 0", "1 1", "0 2", "2 1"};
        var args = new String[]{"-s", "3", "-w", "3"};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(6));
            softly.assertThat(output).contains(Messages.GAME_WINNER.formatted("o"));
        });
    }

    @Test
    @DisplayName("Game quit right after start")
    void quitGameAfterStart() throws Exception {
        var moves = new String[] {":q"};
        var args = new String[] {};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(0));
        });
    }

    @Test
    @DisplayName("Game quit right after 3 turns")
    void quitGameAfterTurns() throws Exception {
        var moves = new String[] {"1 1", "2 1", "0 2", ":q"};
        var args = new String[] {};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(3));
        });
    }

    @Test()
    @DisplayName("3x3 board where x wins with 3 in row after rewinding")
    void  timeTravel3x3scenarioFirstWinsAfterRewind() throws Exception {
        var moves = new String[] { "0 0", "1 1", "0 1", "1 2", "1 0", "2 0", "<<2", };
        var args = new String[]{"-s", "3", "-w", "3"};
        var output = playGame(args, moves);

        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat(output).contains(Messages.GAME_OVER.formatted(5));
            softly.assertThat(output).contains(Messages.GAME_WINNER.formatted("*"));
        });
    }

    private String playGame(String[] args, String[] moves) throws Exception {
        return tapSystemErrAndOutNormalized(() -> withTextFromSystemIn(moves).execute(() -> {
            Application.main(args);
        }));
    }

}
