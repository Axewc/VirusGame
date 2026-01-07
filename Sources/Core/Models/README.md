# Models

Pure data structures for the Cyber Systems game.

## Next to implement (Phase 1):
- `CardColor.swift` - Enum for card colors (Blue, Red, Green, Yellow, Purple, Neutral)
- `CardType.swift` - Enum for card types (Module, Malware, Defense, Operation)
- `Card.swift` - Core card model
- `Player.swift` - Player model with hand and system/tableau
- `GameState.swift` - Complete game state container

## Rules:
- No game logic in models
- All types should be `struct` or `enum`
- Must conform to `Codable` for persistence
