# Rules

Pure, deterministic, testable game rule logic.

## To implement (Phase 2):
- `DeckGenerator.swift` - Initialize game deck with proper card distribution
- `MoveValidator.swift` - Validate legal moves
- `ActionResolver.swift` - Resolve card actions (infect, defend, etc.)
- `WinConditionChecker.swift` - Detect victory conditions

## Requirements:
- Must be pure functions (no side effects)
- Must be deterministic
- Must be testable without UI
- No randomness except card draw
