# Technical Architecture — Cyber Systems

This document provides a comprehensive technical overview of the Cyber Systems iOS card game architecture, design patterns, and implementation strategy.

## Table of Contents
1. [Architecture Pattern](#architecture-pattern)
2. [Tech Stack](#tech-stack)
3. [Core Components](#core-components)
4. [Data Flow](#data-flow)
5. [State Management](#state-management)
6. [Module Organization](#module-organization)
7. [Design Principles](#design-principles)
8. [Technical Debt & Improvements](#technical-debt--improvements)

---

## Architecture Pattern

### MVVM with SwiftUI

This project implements the **Model-View-ViewModel (MVVM)** architectural pattern, which is the natural fit for SwiftUI applications. The architecture separates concerns into distinct layers:

```
┌─────────────────────────────────────────────────────────┐
│                         Views                           │
│                    (SwiftUI Views)                      │
│  • GameView, CardView, SystemView, HandView            │
│  • Declarative UI, no business logic                   │
└──────────────────┬──────────────────────────────────────┘
                   │ @StateObject
                   │ @Binding
                   │ @State
┌──────────────────▼──────────────────────────────────────┐
│                     ViewModel Layer                     │
│                  (Game Engine/Controller)               │
│  • GameEngine: Orchestrates game flow                  │
│  • Manages @Published state                            │
│  • Coordinates between UI and business logic           │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                      Rules Layer                        │
│                  (Pure Business Logic)                  │
│  • MoveValidator: Validates legal moves                │
│  • ActionResolver: Resolves card effects               │
│  • WinConditionChecker: Detects victory                │
│  • DeckGenerator: Initializes game deck                │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                      Model Layer                        │
│                   (Data Structures)                     │
│  • Card, Player, GameState                             │
│  • Pure structs/enums                                  │
│  • Codable for persistence                             │
└─────────────────────────────────────────────────────────┘
```

### Key Characteristics

- **Unidirectional Data Flow**: Data flows from Models → Rules → ViewModel → Views
- **Reactive Updates**: SwiftUI views automatically update when @Published properties change
- **Testability**: Business logic is isolated in pure functions, making it highly testable
- **Separation of Concerns**: UI, logic, and data are completely decoupled

### Why MVVM for This Project?

1. **SwiftUI Integration**: MVVM is the canonical pattern for SwiftUI applications
2. **Declarative UI**: Views react to state changes automatically
3. **Game State Management**: Complex game state is managed centrally in the ViewModel
4. **Multiplayer Ready**: Clear separation makes it easier to add network synchronization later
5. **AI Integration**: Bot players use the same interfaces as human players

---

## Tech Stack

### Core Technologies

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Language** | Swift | 5.9+ | Modern, safe, type-safe language |
| **UI Framework** | SwiftUI | iOS 17+ | Declarative user interface |
| **Platform** | iOS | 17.0+ | Primary deployment target |
| **Platform** | macOS | 14.0+ | Development and CLI testing |
| **Build System** | Swift Package Manager | 5.9+ | Dependency management and builds |
| **Testing** | XCTest | Built-in | Unit testing framework |

### Native Frameworks Used

- **Foundation**: Core Swift functionality (collections, strings, dates)
- **SwiftUI**: User interface components and layout
- **Combine**: Reactive programming (for future async operations)

### Development Tools

- **Xcode**: 15.0+ (primary IDE for iOS development)
- **VS Code**: Alternative with Swift extensions for lightweight editing
- **Swift CLI**: Command-line builds and testing

### No External Dependencies

This project intentionally avoids external dependencies to:
- Reduce complexity and maintenance burden
- Improve build times and reliability
- Maintain full control over the codebase
- Ensure long-term compatibility

---

## Core Components

### 1. Model Layer (`Sources/Core/Models/`)

Pure data structures representing the game domain. All models are value types (structs or enums) for safety and simplicity.

#### Planned Models:

**CardColor** (Enum)
```swift
enum CardColor: String, Codable, CaseIterable {
    case blue, red, green, yellow  // Regular modules
    case purple                     // Wildcard
    case neutral                    // Operations/special cards
}
```

**CardType** (Enum)
```swift
enum CardType: String, Codable {
    case module      // System building blocks
    case malware     // Attack cards
    case defense     // Protection/cure cards
    case operation   // Special actions
}
```

**Card** (Struct)
```swift
struct Card: Identifiable, Codable {
    let id: UUID
    let type: CardType
    let color: CardColor
    // Additional properties for card effects
}
```

**Player** (Struct)
```swift
struct Player: Identifiable, Codable {
    let id: UUID
    var name: String
    var hand: [Card]
    var system: [Card]        // The 4-module tableau
    var discardPile: [Card]
    // Module states (infected, immunized, etc.)
}
```

**GameState** (Struct)
```swift
struct GameState: Codable {
    var players: [Player]
    var deck: [Card]
    var currentPlayerIndex: Int
    var phase: GamePhase
    var winner: Player?
}
```

#### Design Constraints:
- ✅ All types are structs or enums (value semantics)
- ✅ Conform to `Codable` for persistence/serialization
- ✅ No methods with side effects
- ✅ No game logic in models (pure data containers)

---

### 2. Rules Layer (`Sources/Core/Rules/`)

Pure, deterministic functions that implement game logic. These are the "brain" of the game.

#### Components:

**DeckGenerator**
- Creates the initial game deck with proper card distribution
- Ensures balanced gameplay (correct number of each card type)
- Shuffles deck using deterministic or seeded randomization

**MoveValidator**
- Validates whether a player's action is legal
- Checks preconditions (e.g., can only play malware on opponent modules)
- Returns validation result with error messages

**ActionResolver**
- Resolves the effects of playing a card
- Pure function: `(GameState, Action) -> GameState`
- Handles infection, immunization, curing, etc.

**WinConditionChecker**
- Detects when a player has won
- Checks for 4 healthy modules of different colors
- Validates module states (not infected)

#### Design Principles:
- ✅ Pure functions (no side effects, no I/O)
- ✅ Deterministic (same input → same output)
- ✅ Testable without UI or game engine
- ✅ Composable (small functions combined for complex behavior)

---

### 3. Engine Layer (`Sources/Core/Engine/`)

Orchestrates game flow, manages turns, and coordinates between rules and UI.

**GameEngine** (Class, ObservableObject)
```swift
class GameEngine: ObservableObject {
    @Published var gameState: GameState
    @Published var selectedCard: Card?
    
    func startNewGame(players: [String])
    func playCard(card: Card, target: Target)
    func drawCard()
    func endTurn()
    
    // Coordinates with Rules layer
    private func applyAction(_ action: GameAction) -> Bool
}
```

**GameAction** (Enum)
```swift
enum GameAction {
    case playModule(card: Card, slot: Int)
    case playMalware(card: Card, target: Player, slot: Int)
    case playDefense(card: Card, target: Card)
    case playOperation(card: Card, effect: Effect)
    case drawCard
    case discardCard(card: Card)
}
```

**TurnPhase** (Enum)
```swift
enum TurnPhase: String, Codable {
    case draw
    case play
    case discard
}
```

#### Responsibilities:
- Manage game lifecycle (start, play, end)
- Emit state changes via `@Published` properties
- Validate actions via Rules layer
- Handle turn progression
- Coordinate AI bot decisions

---

### 4. Bots Layer (`Sources/Core/Bots/`)

AI opponents that play using heuristic strategies.

**BotPlayer** (Class)
```swift
class BotPlayer {
    func decideAction(gameState: GameState) -> GameAction
    
    private func evaluateHand(_ hand: [Card]) -> [ScoredAction]
    private func selectBestAction(_ actions: [ScoredAction]) -> GameAction
}
```

**BotStrategy**
Priority-based decision making:
1. Complete own system (highest priority)
2. Immunize vulnerable modules
3. Attack leading opponent
4. Prevent opponent from winning
5. Discard least useful card

#### Constraints:
- ✅ Uses same GameEngine API as human players
- ✅ No randomness (deterministic for testing)
- ✅ No machine learning (heuristic-based)
- ✅ Configurable difficulty (strategy weights)

---

### 5. UI Layer (`Sources/UI/`)

SwiftUI views that present the game state and handle user interactions.

#### Views (`Sources/UI/Views/`)

**GameView** — Main game screen
- Displays all players' systems
- Shows current player's hand
- Renders game board and status
- Handles card selection and targeting

**CardView** — Individual card display
- Renders card icon, color, and state
- Shows infection/immunity status
- Drag-and-drop support for card playing
- Animations for card effects

**SystemView** — 4-module player board
- Grid layout for 4 module slots
- Visual indicators for infected/immunized modules
- Empty slot placeholders
- Color-coded borders

**HandView** — Player's hand UI
- Horizontal scrolling card container
- Card selection handling
- Discard gesture support

#### Components (`Sources/UI/Components/`)

Reusable, presentational-only UI components:
- Status indicators (infected, protected, immunized)
- Card animations and transitions
- Effect overlays
- Button styles and controls

#### Design Constraints:
- ✅ No business logic in views
- ✅ State passed from parent views via `@Binding`
- ✅ Purely presentational
- ✅ Flat vector iconography (Mexico City metro style)
- ✅ High contrast, solid colors

---

## Data Flow

### Game Action Flow

```
1. User Interaction (tap, drag)
        ↓
2. View captures event
        ↓
3. View calls GameEngine method
        ↓
4. GameEngine validates via Rules layer
        ↓
5. Rules return validation result
        ↓
6. If valid: GameEngine updates @Published gameState
        ↓
7. SwiftUI automatically re-renders affected views
```

### Example: Playing a Malware Card

```swift
// 1. User taps malware card in hand, then taps opponent module
HandView → CardView (selection)
GameView → SystemView (targeting)

// 2. View calls GameEngine
gameEngine.playCard(malwareCard, target: opponentModule)

// 3. GameEngine validates
let isValid = MoveValidator.canPlayMalware(
    card: malwareCard,
    target: opponentModule,
    gameState: gameState
)

// 4. If valid, resolve action
if isValid {
    gameState = ActionResolver.applyMalware(
        card: malwareCard,
        target: opponentModule,
        state: gameState
    )
    // SwiftUI views update automatically
}
```

---

## State Management

### SwiftUI Property Wrappers

This project uses SwiftUI's native state management:

| Wrapper | Purpose | Used For |
|---------|---------|----------|
| **@State** | View-local state | Card selection, animations, UI-only state |
| **@StateObject** | ViewModel lifecycle | GameEngine instance ownership |
| **@ObservedObject** | ViewModel reference | Observing GameEngine in child views |
| **@Binding** | Two-way binding | Passing editable state to child views |
| **@Published** | Observable property | GameState, player hands, turn phase |

### State Ownership

```swift
// Root view owns the GameEngine
struct RootView: View {
    @StateObject private var gameEngine = GameEngine()
    
    var body: some View {
        GameView(gameEngine: gameEngine)
    }
}

// Child views observe the GameEngine
struct GameView: View {
    @ObservedObject var gameEngine: GameEngine
    
    var body: some View {
        VStack {
            SystemView(system: $gameEngine.gameState.players[0].system)
            HandView(hand: gameEngine.currentPlayer.hand, 
                     onCardPlayed: gameEngine.playCard)
        }
    }
}

// Leaf components receive bindings
struct CardView: View {
    let card: Card
    @Binding var isSelected: Bool
    
    var body: some View {
        // Render card
    }
}
```

### State Persistence

Game state can be persisted using `Codable`:

```swift
// Save game
let encoder = JSONEncoder()
let data = try encoder.encode(gameEngine.gameState)
UserDefaults.standard.set(data, forKey: "savedGame")

// Load game
let decoder = JSONDecoder()
let data = UserDefaults.standard.data(forKey: "savedGame")
gameEngine.gameState = try decoder.decode(GameState.self, from: data)
```

---

## Module Organization

### Package Structure

```
Package.swift                 # SPM configuration, iOS 17+ / macOS 14+
├── Products
│   └── CyberSystems (library)
└── Targets
    ├── CyberSystems (main target)
    └── CyberSystemsTests (test target)
```

### Source Organization

```
Sources/
├── CyberSystems.swift        # Module entry point (placeholder)
├── Core/                     # Business logic (platform-agnostic)
│   ├── Models/              # Data structures
│   ├── Rules/               # Game logic
│   ├── Engine/              # Game orchestration
│   └── Bots/                # AI players
└── UI/                       # SwiftUI views (iOS/macOS)
    ├── Views/               # Full screens
    └── Components/          # Reusable UI elements
```

### Dependency Direction

```
UI → Engine → Rules → Models
     ↓
   Bots → Rules → Models
```

- UI depends on Engine (not directly on Rules or Models)
- Engine depends on Rules and Models
- Bots depend on Rules and Models (use same interfaces as human players)
- Rules depend only on Models
- Models have no dependencies (pure data)

---

## Design Principles

### 1. Separation of Concerns
- Models: Data only, no logic
- Rules: Logic only, no UI or I/O
- Engine: Orchestration, no UI
- Views: Presentation only, no business logic

### 2. Pure Functional Core
- Rules layer consists of pure functions
- Deterministic behavior (testable, predictable)
- No side effects in business logic

### 3. Value Semantics
- Prefer structs over classes
- Immutable by default (let > var)
- Copy-on-write for large structures

### 4. Protocol-Oriented Programming
- Define interfaces before implementations
- Use protocols for testability (e.g., BotPlayer)
- Conform to standard protocols (Identifiable, Codable, Equatable)

### 5. Testability First
- Business logic is 100% testable without UI
- Pure functions can be tested in isolation
- No mocking required for Rules layer

### 6. Progressive Enhancement
- Build game in phases (Models → Rules → Engine → UI → Bots)
- Each phase is functional and testable
- Avoid big-bang integration

---

## Technical Debt & Improvements

### Current Status (Early Development)

The project is currently in **Phase 0** (project setup) with only placeholder code. All game functionality is planned but not yet implemented.

### Known Limitations

#### 1. **No Actual Game Implementation Yet**
- **Issue**: Only project structure and placeholder code exist
- **Impact**: Cannot play the game yet
- **Plan**: Implement in phases as outlined in component README files:
  - Phase 1: Core Models (Card, Player, GameState)
  - Phase 2: Rules Engine (validation, resolution)
  - Phase 3: Game Engine and Bots
  - Phase 4: SwiftUI UI Layer
- **Timeline**: See individual component README files in `Sources/Core/` and `Sources/UI/` subdirectories

#### 2. **Component README Files Generate Build Warnings**
- **Issue**: Swift Package Manager warns about unhandled README.md files in component subdirectories (`Sources/Core/Models/`, `Sources/Core/Rules/`, etc.)
- **Current Warning**:
  ```
  found 6 file(s) which are unhandled; explicitly declare them as resources or exclude from the target
  ```
- **Impact**: Build warnings (non-breaking)
- **Solution**: Either:
  - Declare them as resources in `Package.swift`: `.target(name: "CyberSystems", resources: [.copy("Core/Models/README.md"), ...])`
  - Move component documentation to a separate `Docs/` directory outside of `Sources/`
- **Priority**: Low (cosmetic issue, does not affect functionality)

#### 3. **No Persistence Layer**
- **Issue**: No game state saving/loading implemented
- **Impact**: Games cannot be resumed after closing app
- **Plan**: Add in Phase 5 using:
  - `UserDefaults` for simple persistence
  - `SwiftData` or Core Data for complex game history
- **Priority**: Medium (nice-to-have for MVP)

#### 4. **No Multiplayer Networking**
- **Issue**: Only local multiplayer planned initially
- **Impact**: Cannot play against remote opponents
- **Plan**: Future enhancement after Phase 4 completion
  - Consider: GameCenter, CloudKit, or custom server
- **Priority**: Low (post-MVP)

#### 5. **No Sound or Haptics**
- **Issue**: No audio feedback or haptic effects
- **Impact**: Less engaging user experience
- **Plan**: Add in Phase 4-5:
  - AVFoundation for sound effects
  - UIFeedbackGenerator for haptics
- **Priority**: Low (polish feature)

#### 6. **Limited Accessibility**
- **Issue**: No VoiceOver support planned yet
- **Impact**: Not accessible to visually impaired users
- **Plan**: Add in Phase 4:
  - Accessibility labels on all interactive elements
  - VoiceOver support for game state
  - Dynamic Type support
- **Priority**: Medium (required for App Store)

#### 7. **No Animations or Transitions**
- **Issue**: Static UI planned for MVP
- **Impact**: Less polished user experience
- **Plan**: Add in Phase 4:
  - Card dealing animations
  - Infection/cure effect animations
  - Victory celebration animations
- **Priority**: Medium (enhances UX)

#### 8. **No Error Recovery**
- **Issue**: No handling for invalid game states
- **Impact**: Could crash or deadlock in edge cases
- **Plan**: Add defensive programming in Phase 2-3:
  - State validation on transitions
  - Graceful error handling
  - Undo/redo system
- **Priority**: High (stability)

### Architectural Improvements

#### 1. **Consider Composable Architecture (TCA)**
- **Current**: Custom MVVM implementation
- **Alternative**: Point-Free's Composable Architecture
- **Pros**: Better testability, time-travel debugging, built-in effects system
- **Cons**: Steeper learning curve, additional dependency
- **Decision**: Keep MVVM for now, re-evaluate if complexity grows

#### 2. **Use Protocols for Dependency Injection**
- **Current**: Direct dependencies on concrete types
- **Improvement**: Define protocols for major components
- **Example**:
  ```swift
  protocol GameRulesProtocol {
      func validateMove(...) -> Bool
      func resolveAction(...) -> GameState
  }
  ```
- **Benefit**: Easier testing with mock implementations
- **Priority**: Medium (improve testability)

#### 3. **Add Logging and Analytics**
- **Current**: No logging system
- **Improvement**: Use `os.Logger` for structured logging
- **Use Cases**: Debug gameplay issues, track user behavior, monitor crashes
- **Priority**: Medium (helpful for debugging)

#### 4. **Document Card Effects DSL**
- **Future**: Card effects are currently implicit in code
- **Improvement**: Consider a domain-specific language for card effects
- **Example**:
  ```swift
  struct CardEffect {
      let targets: TargetSelector
      let action: ActionType
      let condition: Condition?
  }
  ```
- **Benefit**: Easier to add new card types
- **Priority**: Low (optimization for extensibility)

### Code Quality Improvements

#### 1. **Add SwiftLint**
- **Status**: No linter configured
- **Benefit**: Enforce consistent code style
- **Priority**: Medium (code quality)

#### 2. **Increase Test Coverage**
- **Current**: Only placeholder test
- **Target**: 80%+ coverage for Rules and Engine layers
- **Priority**: High (critical for correctness)

#### 3. **Add CI/CD Pipeline**
- **Current**: No automated testing
- **Plan**: GitHub Actions for:
  - Automated testing on push
  - Build validation
  - Test coverage reporting
- **Priority**: Medium (development efficiency)

#### 4. **Performance Profiling**
- **Current**: No performance testing
- **Future**: Profile with Instruments after Phase 4
  - Optimize rendering for 60 FPS
  - Reduce memory allocations
  - Test on older devices (iPhone SE 3rd gen)
- **Priority**: Low (post-MVP optimization)

### Documentation Improvements

✅ **Completed**:
- Comprehensive README.md
- Detailed TECHNICAL_ARCHITECTURE.md

🚧 **Remaining**:
- API documentation (DocC comments)
- Tutorial for contributing developers
- Game rules document for players
- UX/UI design guidelines

---

## Conclusion

Cyber Systems is architected as a modern SwiftUI application following MVVM principles with a strong emphasis on testability, maintainability, and clean separation of concerns. The phased development approach ensures each layer is solid before building the next.

The architecture is designed to be:
- ✅ **Testable**: Pure functions and isolated components
- ✅ **Maintainable**: Clear separation of concerns
- ✅ **Extensible**: Easy to add new card types and rules
- ✅ **Scalable**: Ready for multiplayer and persistence
- ✅ **Type-Safe**: Leverages Swift's strong type system

For questions or contributions, please refer to the main [README.md](README.md) or contact the development team.
