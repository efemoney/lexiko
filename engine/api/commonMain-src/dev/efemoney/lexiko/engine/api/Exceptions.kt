package dev.efemoney.lexiko.engine.api

/**
 * Base class for all exceptions within the Lexiko game engine.
 */
sealed class EngineException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

// region Game State Exceptions
/**
 * Exceptions related to the overall game state.
 */
sealed class GameStateException(message: String? = null, cause: Throwable? = null) :
  EngineException(message, cause)

/**
 * Thrown when an action requires a game in progress, but the game hasn't been initialized.
 */
class GameNotStartedException : GameStateException("Game has not been started.")

/**
 * Thrown when attempting to start a game that is already in progress.
 */
class GameAlreadyStartedException : GameStateException("Game is already in progress.")

/**
 * Thrown when an action is attempted after the game has concluded.
 */
class GameEndedException : GameStateException("Game has already ended.")

/**
 * Thrown during game initialization if the number of players is outside the allowed range.
 * @param count The invalid number of players.
 */
class InvalidPlayerCountException(count: Int) : GameStateException("Invalid number of players: $count")

/**
 * Thrown if a player attempts an action when it's not their turn.
 * @param playerId The ID of the player who attempted the action.
 */
class InvalidTurnException(playerId: Int) : GameStateException("Not player $playerId's turn.")
// endregion

// region Board Exceptions
/**
 * Exceptions related to interactions with the game board.
 */
sealed class BoardException(message: String? = null, cause: Throwable? = null) : EngineException(message, cause)

/**
 * Thrown when trying to access a TileSlot using a TilePosition with out-of-bounds coordinates.
 * @param position The invalid TilePosition.
 */
class PositionOutOfBoundsException(position: TilePosition) : BoardException("Invalid $position is out of bounds.")

/**
 * Thrown when attempting to place a tile on a TileSlot that already contains a tile.
 * @param position The TilePosition of the occupied slot.
 */
class PlacementPositionNotEmptyException(attempted: Tile, found: Tile, at: TilePosition) :
  BoardException("Attempted to place $attempted at $at, but found $found instead.")

// endregion

// region Invalid Placement Exceptions
/**
 * Base class for exceptions related to invalid tile placement.
 */
sealed class InvalidPlacementException(message: String? = null) : BoardException(message)

/**
 * Thrown if a played word is not connected to existing tiles (except for the first play).
 */
class DisconnectedPlacementException : InvalidPlacementException("Word is not connected to existing tiles.")

/**
 * Thrown if the arrangement of tiles does not form a valid word (dictionary lookup failed).
 * @param word The invalid word.
 */
class NotAWordException(word: String) : InvalidPlacementException("$word is not a valid word.")

/**
 * Thrown if the placement of a word violates Scrabble rules (e.g., diagonal placement, gaps).
 * @param message Specific details about the invalid placement.
 */
class InvalidWordPlacementException(message: String) : InvalidPlacementException(message)

/**
 * Thrown if a word exceeds the maximum number of tiles that can be placed in a row or column.
 */
class WordTooLongException : InvalidPlacementException("Word exceeds the maximum length.")
// endregion

// region Tile Exceptions
/**
 * Exceptions related to tiles and the bag of tiles.
 */
sealed class TileException(message: String? = null, cause: Throwable? = null) : EngineException(message, cause)

/**
 * Thrown when attempting to draw tiles from an empty bag.
 */
class EmptyBagException : TileException("Tile bag is empty.")

/**
 * Thrown when a tile with invalid characteristics is encountered.
 * @param message Details about the invalid tile.
 */
class InvalidTileException(message: String) : TileException("Invalid tile: $message")

/**
 * Thrown when attempting to return a tile to the bag that wasn't originally drawn from it.
 * @param tile The tile that was not in the bag.
 */
class TileNotInBagException(tile: Tile) : TileException("Tile $tile was not drawn from this bag.")
// endregion

/**
 * Thrown when a player attempts to play a word but lacks the necessary tiles in their rack.
 */
class PlayerOutOfTilesException : EngineException("Player does not have the required tiles.")

/**
 * Thrown if a score calculation results in an invalid value (e.g., negative score).
 */
class InvalidScoreException : EngineException("Invalid score calculation.")
