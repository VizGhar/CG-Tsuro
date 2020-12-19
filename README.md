# Tsuro

This is my first CodinGame Multiplayer contribution. I'm trying to simulate board game of Tsuro.
It's turn based game for 2-8 players where you place 35 square tiles on 6x6 square board. Tiles
contains roads, you have to follow. You lose if you leave the board, or hit other player.

Be the last one standing!!!

## The Turn by Turn mode

Tsuro is turn based game like chess/tic-tac-toe. Therefor these rules apply (copy paste from
skeleton project):

```kotlin
val player = gameManager.getPlayer(turn % playerCount)
player.sendInputLine(input)
player.execute()
try {
    val outputs = player.getOutputs();
    // Check validity of the player output and compute the new game state
} catch (e: TimeoutException) {
    player.deactivate(String.format("$%d timeout!", player.getIndex()));
    player.setScore(-1);
    gameManager.endGame();
}

// Check if there is a win / lose situation and call gameManager.endGame(); when game is finished
```