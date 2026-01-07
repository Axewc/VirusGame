# Technical Architecture Documentation

**Cyber Systems — iOS Card Game**

> **Document Version**: 1.0  
> **Last Updated**: January 2026  
> **Swift Version**: 5.9+  
> **iOS Target**: 17.0+

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [System Architecture Overview](#system-architecture-overview)
3. [Design Pattern: MVVM](#design-pattern-mvvm)
4. [Core Components](#core-components)
5. [Data Flow](#data-flow)
6. [Module Details](#module-details)
7. [State Management](#state-management)
8. [Testing Strategy](#testing-strategy)
9. [Development Phases](#development-phases)
10. [Technical Debt & Improvements](#technical-debt--improvements)
11. [Performance Considerations](#performance-considerations)
12. [Security Considerations](#security-considerations)

---

## Executive Summary

**Cyber Systems** is a native iOS card game built using **Swift 5.9+** and **SwiftUI**, targeting iOS 17.0 and above. The application implements a **clean MVVM (Model-View-ViewModel) architecture** with strict separation between business logic and presentation layers.

### Key Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| **MVVM Pattern** | Clean separation of concerns, testable business logic, reactive UI updates |
| **Pure Swift Implementation** | No external dependencies, reduced maintenance, improved security |
| **Value Types (Structs/Enums)** | Immutability by default, thread safety, predictable state |
| **SwiftUI** | Declarative UI, automatic state management, reduced boilerplate |
| **Swift Package Manager** | Native build system, Xcode integration, no external tools |
| **Protocol-Oriented Design** | Flexibility, testability, bot/human player abstraction |

---

## System Architecture Overview

The application is structured into two primary layers: **Core** (business logic) and **UI** (presentation).

```
╔═══════════════════════════════════════════════════════════╗
║                     Application Layer                      ║
║  ┌─────────────────────────────────────────────────────┐  ║
║  │              SwiftUI App Entry Point                │  ║
║  │           (CyberSystemsApp.swift - Future)          │  ║
║  └─────────────────────────────────────────────────────┘  ║
╚═══════════════╦═══════════════════════════════════════════╝
                ║
    ┌───────────╨───────────┐
    │                       │
    ▼                       ▼
╔═══════════════════╗   ╔══════════════════════════╗
║    UI Layer       ║   ║     Core Layer          ║
║                   ║   ║                          ║
║ ┌───────────────┐ ║   ║ ┌──────────────────┐   ║
║ │     Views     │ ║   ║ │     Models       │   ║
║ │   (SwiftUI)   │ ║   ║ │  (Data Structs)  │   ║
║ └───────┬───────┘ ║   ║ └────────▲─────────┘   ║
║         │         ║   ║          │              ║
║ ┌───────▼───────┐ ║   ║ ┌────────┴─────────┐   ║
║ │  Components   │ ║   ║ │      Rules       │   ║
║ │  (Reusable)   │ ║   ║ │   (Pure Logic)   │   ║
║ └───────────────┘ ║   ║ └────────▲─────────┘   ║
║         │         ║   ║          │              ║
║         │ Observe ║   ║ ┌────────┴─────────┐   ║
║         └─────────╫───╫→│     Engine       │   ║
║                   ║   ║ │  (Orchestrator)  │   ║
║                   ║   ║ └────────▲─────────┘   ║
║                   ║   ║          │              ║
║                   ║   ║ ┌────────┴─────────┐   ║
║                   ║   ║ │      Bots        │   ║
║                   ║   ║ │   (AI Players)   │   ║
║                   ║   ║ └──────────────────┘   ║
╚═══════════════════╝   ╚══════════════════════════╝
```

### Layer Responsibilities

#### **Core Layer** (Business Logic)
- **No UI dependencies**: Can be tested without SwiftUI
- **Pure functions**: Deterministic, side-effect free
- **Value types**: Immutable data structures
- **Platform-agnostic**: Core logic is reusable

#### **UI Layer** (Presentation)
- **SwiftUI views**: Declarative UI components
- **Observe state**: React to changes in Core layer
- **No business logic**: Pure presentation
- **Accessibility**: VoiceOver, Dynamic Type support

---

## Design Pattern: MVVM

This project implements a **clean MVVM (Model-View-ViewModel)** architecture with SwiftUI's reactive programming model.

### Pattern Structure

```
┌─────────────────────────────────────────────────────────┐
│                          View                           │
│                       (SwiftUI)                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │  GameView   │  │  CardView   │  │ SystemView  │   │
│  └─────────────┘  └─────────────┘  └─────────────┘   │
└───────────────────────┬─────────────────────────────────┘
                        │
                        │ @StateObject / @ObservedObject
                        │ (Reactive Binding)
                        ▼
┌─────────────────────────────────────────────────────────┐
│                      ViewModel                          │
│                  (GameViewModel - Future)               │
│  ┌──────────────────────────────────────────────────┐  │
│  │  @Published var gameState: GameState             │  │
│  │  func playCard(_ card: Card, to target: Target)  │  │
│  │  func drawCard()                                 │  │
│  │  func endTurn()                                  │  │
│  └──────────────────────────────────────────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │
                        │ Calls
                        ▼
┌─────────────────────────────────────────────────────────┐
│                        Model                            │
│                     (Core Layer)                        │
│  ┌────────────┐  ┌──────────┐  ┌──────────────────┐   │
│  │ GameState  │  │  Rules   │  │  GameEngine      │   │
│  │  (Data)    │  │ (Logic)  │  │ (Orchestration)  │   │
│  └────────────┘  └──────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### MVVM Components

#### **Model** — Data & Business Logic
- **Location**: `Sources/Core/`
- **Types**: Structs, Enums (value types)
- **Responsibilities**:
  - Define game state structures (`GameState`, `Player`, `Card`)
  - Implement game rules (`MoveValidator`, `ActionResolver`)
  - Manage game flow (`GameEngine`)
  - No UI dependencies

> **📝 Note on Code Examples**: Throughout this document, code examples are marked with `(Future)` to indicate files that will be created in upcoming development phases. These examples demonstrate the planned architecture and serve as implementation blueprints. The current codebase contains only placeholder files and directory structure.

**Example Model Structure**:
```swift
// Sources/Core/Models/GameState.swift (Future)
struct GameState: Codable {
    var players: [Player]
    var currentPlayerIndex: Int
    var deck: [Card]
    var discardPile: [Card]
    var phase: TurnPhase
    var winner: Player?
}

struct Player: Codable, Identifiable {
    let id: UUID
    var name: String
    var hand: [Card]
    var system: [Card]  // The 4-module board
    var tableauCards: [Card]  // Other cards on player's area
}

struct Card: Codable, Identifiable {
    let id: UUID
    let color: CardColor
    let type: CardType
    var modifiers: Set<CardModifier>  // infected, protected, immune
}
```

#### **View** — SwiftUI UI Components
- **Location**: `Sources/UI/Views/`, `Sources/UI/Components/`
- **Type**: SwiftUI `View` protocol
- **Responsibilities**:
  - Display game state
  - Capture user input
  - Delegate actions to ViewModel
  - No business logic

**Example View Structure**:
```swift
// Sources/UI/Views/GameView.swift (Future)
struct GameView: View {
    @StateObject private var viewModel: GameViewModel
    
    var body: some View {
        VStack {
            // Opponent's system
            SystemView(player: viewModel.opponent)
            
            // Game board / play area
            PlayAreaView(gameState: viewModel.gameState)
            
            // Current player's system
            SystemView(player: viewModel.currentPlayer)
            
            // Current player's hand
            HandView(
                cards: viewModel.currentPlayer.hand,
                onCardTapped: viewModel.playCard
            )
        }
    }
}
```

#### **ViewModel** — State Management & Coordination
- **Location**: `Sources/UI/ViewModels/` (Future)
- **Type**: `ObservableObject` class
- **Responsibilities**:
  - Hold `@Published` game state
  - Expose user actions as methods
  - Coordinate between View and Model
  - Transform Model data for View consumption

**Example ViewModel Structure**:
```swift
// Sources/UI/ViewModels/GameViewModel.swift (Future)
@MainActor  // Required: Updates @Published properties which must happen on main thread
class GameViewModel: ObservableObject {
    @Published private(set) var gameState: GameState
    private let gameEngine: GameEngine  // GameEngine itself doesn't need @MainActor
    
    init(gameEngine: GameEngine) {
        self.gameEngine = gameEngine
        self.gameState = gameEngine.currentState
    }
    
    func playCard(_ card: Card, to target: Target) {
        let action = GameAction.playCard(card, target: target)
        gameState = gameEngine.process(action)
    }
    
    func drawCard() {
        gameState = gameEngine.process(.drawCard)
    }
    
    var currentPlayer: Player {
        gameState.players[gameState.currentPlayerIndex]
    }
}

// Threading Note:
// - GameEngine: Pure business logic, no @MainActor needed (can run on any thread)
// - GameViewModel: Must use @MainActor because it updates @Published properties
//   which SwiftUI observes on the main thread
// This separation allows GameEngine to be tested without main thread constraints
```

---

## Core Components

### 1. Models (`Sources/Core/Models/`)

Pure data structures representing the game state.

#### **Planned Models**

| Model | Type | Purpose |
|-------|------|---------|
| `CardColor` | `enum` | Blue, Red, Green, Yellow, Purple, Neutral |
| `CardType` | `enum` | Module, Malware, Defense, Operation |
| `CardModifier` | `enum` | Infected, Protected, Immune |
| `Card` | `struct` | Complete card definition with color, type, and modifiers |
| `Player` | `struct` | Player state: hand, system, tableau |
| `GameState` | `struct` | Complete game state container |
| `TurnPhase` | `enum` | Draw, Play, Discard phases |

#### **Design Principles**
- ✅ **Value types** (struct/enum) for immutability
- ✅ **Codable** conformance for persistence
- ✅ **Identifiable** for SwiftUI list rendering
- ❌ **No methods** (data-only structures)
- ❌ **No UI concerns** (platform-agnostic)

**Example Implementation**:
```swift
enum CardColor: String, Codable, CaseIterable {
    case blue
    case red
    case green
    case yellow
    case purple  // Wildcard
    case neutral
}

enum CardType: String, Codable {
    case module
    case malware
    case defense
    case operation
}

enum CardModifier: String, Codable, Hashable {
    case infected
    case protected
    case immune
}
```

---

### 2. Rules (`Sources/Core/Rules/`)

Pure, deterministic functions implementing game rules.

#### **Planned Rules Components**

| Component | Purpose | Pure Function |
|-----------|---------|---------------|
| `DeckGenerator` | Generate initial deck with proper distribution | ✅ |
| `MoveValidator` | Validate legal moves | ✅ |
| `ActionResolver` | Apply card effects to game state | ✅ |
| `WinConditionChecker` | Detect victory conditions | ✅ |

#### **Design Principles**
- ✅ **Pure functions**: No side effects
- ✅ **Deterministic**: Same input → Same output
- ✅ **Testable**: No dependencies on external state
- ❌ **No randomness** (except controlled shuffle)

**Example Rule Implementation**:
```swift
// Sources/Core/Rules/MoveValidator.swift (Future)
enum MoveValidator {
    static func canPlayCard(
        _ card: Card,
        on target: Target,
        in gameState: GameState
    ) -> Result<Void, MoveError> {
        // Validation logic
        switch card.type {
        case .module:
            return canPlaceModule(card, on: target, in: gameState)
        case .malware:
            return canInfectModule(card, on: target, in: gameState)
        case .defense:
            return canDefendModule(card, on: target, in: gameState)
        case .operation:
            return canApplyOperation(card, in: gameState)
        }
    }
    
    private static func canPlaceModule(
        _ card: Card,
        on target: Target,
        in gameState: GameState
    ) -> Result<Void, MoveError> {
        let player = gameState.currentPlayer
        
        // Check: player has less than 4 modules
        guard player.system.count < 4 else {
            return .failure(.systemFull)
        }
        
        // Check: no duplicate colors (unless wildcard)
        let systemColors = player.system.map(\.color)
        if card.color != .purple && systemColors.contains(card.color) {
            return .failure(.duplicateColor)
        }
        
        return .success(())
    }
}
```

---

### 3. Engine (`Sources/Core/Engine/`)

Orchestrates game flow and coordinates rules.

#### **Planned Engine Components**

| Component | Purpose |
|-----------|---------|
| `GameEngine` | Main game loop, turn controller |
| `GameAction` | Enum of all possible player actions |
| `TurnPhase` | Turn phase management (draw, play, discard) |

#### **Responsibilities**
- Coordinate turn flow
- Apply rules to validate and process actions
- Emit state changes
- Manage player queue (human and bot players)

**Example Engine Implementation**:
```swift
// Sources/Core/Engine/GameEngine.swift (Future)
enum GameAction {
    case drawCard
    case playCard(Card, target: Target)
    case discardCard(Card)
    case endTurn
}

class GameEngine {
    private(set) var currentState: GameState
    
    init(players: [Player], deck: [Card]) {
        self.currentState = GameState(
            players: players,
            currentPlayerIndex: 0,
            deck: deck,
            discardPile: [],
            phase: .draw,
            winner: nil
        )
    }
    
    func process(_ action: GameAction) -> GameState {
        // Validate action
        switch MoveValidator.validateAction(action, in: currentState) {
        case .success:
            // Apply action
            let newState = ActionResolver.resolve(action, in: currentState)
            
            // Check win condition
            if let winner = WinConditionChecker.checkWinner(in: newState) {
                return newState.settingWinner(winner)
            }
            
            currentState = newState
            return currentState
            
        case .failure(let error):
            // Handle invalid move
            print("Invalid move: \(error)")
            return currentState
        }
    }
}
```

---

### 4. Bots (`Sources/Core/Bots/`)

AI opponents using heuristic-based strategies.

#### **Planned Bot Components**

| Component | Purpose |
|-----------|---------|
| `BotPlayer` | AI player implementation |
| `BotStrategy` | Decision-making heuristics |

#### **Strategy Priority** (Heuristic-Based)
1. **Complete own system** — Play modules to reach 4 different colors
2. **Immunize own modules** — Protect critical system components
3. **Attack leading opponent** — Infect opponent's modules
4. **Prevent opponent win** — Block opponent from completing system

#### **Design Constraints**
- ✅ Use same `GameEngine` API as human players
- ✅ Deterministic (no randomness)
- ❌ No machine learning
- ❌ No external data

**Example Bot Implementation**:
```swift
// Sources/Core/Bots/BotPlayer.swift (Future)
struct BotPlayer {
    let id: UUID
    let strategy: BotStrategy
    
    func chooseAction(in gameState: GameState) -> GameAction {
        let hand = gameState.currentPlayer.hand
        
        // Priority 1: Can I complete my system?
        if let completionMove = strategy.findSystemCompletionMove(hand, gameState) {
            return completionMove
        }
        
        // Priority 2: Should I immunize a module?
        if let immunizeMove = strategy.findImmunizationMove(hand, gameState) {
            return immunizeMove
        }
        
        // Priority 3: Can I attack leading opponent?
        if let attackMove = strategy.findAttackMove(hand, gameState) {
            return attackMove
        }
        
        // Fallback: Draw or discard
        return .drawCard
    }
}
```

---

### 5. Views (`Sources/UI/Views/`)

SwiftUI views for game screens.

#### **Planned Views**

| View | Purpose |
|------|---------|
| `GameView` | Main game screen, orchestrates all UI |
| `CardView` | Individual card rendering |
| `SystemView` | Player's 4-module system board |
| `HandView` | Player's card hand UI |
| `PlayAreaView` | Central play area |

#### **Design Constraints**
- 🎨 **Flat vector iconography** (Mexico City metro style)
- 🚫 **No characters** or anime style
- 🚫 **No text inside cards**
- 🎯 **One centered icon** per card
- 🎨 **Solid background colors**
- ⚡ **High contrast** for accessibility

**Example View Implementation**:
```swift
// Sources/UI/Views/CardView.swift (Future)
struct CardView: View {
    let card: Card
    
    var body: some View {
        ZStack {
            // Background color
            RoundedRectangle(cornerRadius: 12)
                .fill(card.color.backgroundColor)
            
            // Single centered icon
            Image(systemName: card.iconName)
                .font(.system(size: 48, weight: .regular))
                .foregroundColor(card.color.iconColor)
            
            // Status indicators (overlays)
            if card.modifiers.contains(.infected) {
                InfectionIndicatorView()
            }
            if card.modifiers.contains(.protected) {
                ProtectionIndicatorView()
            }
        }
        .frame(width: 80, height: 120)
        .shadow(radius: 4)
    }
}

extension CardColor {
    var backgroundColor: Color {
        switch self {
        case .blue: return .blue
        case .red: return .red
        case .green: return .green
        case .yellow: return .yellow
        case .purple: return .purple
        case .neutral: return .gray
        }
    }
    
    var iconColor: Color {
        return .white  // High contrast
    }
}
```

---

### 6. Components (`Sources/UI/Components/`)

Reusable UI elements.

#### **Planned Components**

- Status indicators (infection, protection, immunity)
- Animations and transitions
- Button styles and controls

#### **Design Principles**
- ✅ Purely presentational (no business logic)
- ✅ Reusable across views
- ✅ Accept state as parameters
- ❌ No direct state management

---

## Data Flow

### Unidirectional Data Flow

The application follows a **unidirectional data flow** pattern, ensuring predictable state management.

```
┌─────────────────────────────────────────────────────────┐
│                      User Action                        │
│              (Tap card, drag to target)                 │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│                   SwiftUI View                          │
│         Calls ViewModel method: playCard()              │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│                     ViewModel                           │
│   Translates to GameAction, calls GameEngine.process()  │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│                   GameEngine                            │
│   1. Validates action via MoveValidator                 │
│   2. Applies action via ActionResolver                  │
│   3. Checks win condition via WinConditionChecker       │
│   4. Returns new GameState                              │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│                    ViewModel                            │
│   Updates @Published var gameState = newState           │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│                   SwiftUI View                          │
│   Re-renders automatically (reactive binding)           │
└─────────────────────────────────────────────────────────┘
```

### Key Characteristics

1. **Single Source of Truth**: `GameState` is the single source of truth
2. **Immutable State**: State is never mutated, only replaced with new copies
3. **Reactive Updates**: SwiftUI automatically re-renders on state changes
4. **Testable**: Each layer can be tested independently

---

## State Management

### SwiftUI Property Wrappers

The application uses SwiftUI's built-in property wrappers for state management.

| Property Wrapper | Usage | Location |
|------------------|-------|----------|
| `@State` | Local view state (UI-only) | Within views for transient state |
| `@StateObject` | Create and own ViewModel | At view root (e.g., `GameView`) |
| `@ObservedObject` | Observe external ViewModel | Child views receiving ViewModel |
| `@Published` | Publish state changes | ViewModel properties |
| `@Environment` | Shared environment values | Theming, accessibility settings |

### State Management Example

```swift
// GameView owns the ViewModel
struct GameView: View {
    @StateObject private var viewModel = GameViewModel(
        gameEngine: GameEngine(players: [...], deck: [...])
    )
    
    var body: some View {
        VStack {
            // Pass ViewModel to child views
            SystemView(player: viewModel.currentPlayer)
                .environmentObject(viewModel)  // For deep children
            
            HandView(
                cards: viewModel.currentPlayer.hand,
                onCardTapped: viewModel.playCard
            )
        }
    }
}

// ViewModel publishes state changes
@MainActor
class GameViewModel: ObservableObject {
    @Published private(set) var gameState: GameState
    @Published var selectedCard: Card?
    
    // Methods that modify @Published properties trigger UI updates
    func selectCard(_ card: Card) {
        selectedCard = card
    }
}

// Child view observes ViewModel
struct HandView: View {
    @ObservedObject var viewModel: GameViewModel
    let cards: [Card]
    
    var body: some View {
        HStack {
            ForEach(cards) { card in
                CardView(card: card)
                    .scaleEffect(
                        viewModel.selectedCard?.id == card.id ? 1.1 : 1.0
                    )
                    .onTapGesture {
                        viewModel.selectCard(card)
                    }
            }
        }
    }
}
```

---

## Testing Strategy

### Test Structure

```
Tests/
└── CyberSystemsTests/
    ├── CyberSystemsTests.swift        # Main test file (placeholder)
    │
    # Future test organization (Phase 2+):
    ├── ModelTests.swift               # Model validation tests
    ├── MoveValidatorTests.swift       # Move validation tests
    ├── ActionResolverTests.swift      # Action resolution tests
    ├── WinConditionTests.swift        # Win condition tests
    ├── GameEngineTests.swift          # Engine integration tests
    └── BotStrategyTests.swift         # Bot strategy tests
```

> **Note**: The current `Package.swift` points to a flat `Tests/CyberSystemsTests` directory. As development progresses, the placeholder `CyberSystemsTests.swift` will be expanded with additional test files (not subdirectories) to maintain consistency with the Swift Package Manager structure.

### Testing Approach

#### **Unit Tests** — Pure Logic Testing
- **Target**: Models, Rules, Engine
- **Framework**: XCTest
- **Coverage Goal**: >90% for Core layer

**Example Test**:
```swift
import XCTest
@testable import CyberSystems

final class MoveValidatorTests: XCTestCase {
    func testCannotPlaceDuplicateColorModule() throws {
        // Given: Player has blue module in system
        var gameState = GameState.empty()
        gameState.currentPlayer.system = [
            Card(id: UUID(), color: .blue, type: .module, modifiers: [])
        ]
        
        // When: Player tries to place another blue module
        let card = Card(id: UUID(), color: .blue, type: .module, modifiers: [])
        let result = MoveValidator.canPlayCard(card, on: .ownSystem, in: gameState)
        
        // Then: Move should be invalid
        XCTAssertThrowsError(try result.get()) { error in
            XCTAssertEqual(error as? MoveError, .duplicateColor)
        }
    }
    
    func testWildcardCanBePlayedRegardlessOfColor() throws {
        // Given: Player has 3 modules (blue, red, green)
        var gameState = GameState.empty()
        gameState.currentPlayer.system = [
            Card(id: UUID(), color: .blue, type: .module, modifiers: []),
            Card(id: UUID(), color: .red, type: .module, modifiers: []),
            Card(id: UUID(), color: .green, type: .module, modifiers: [])
        ]
        
        // When: Player plays purple wildcard
        let wildcard = Card(id: UUID(), color: .purple, type: .module, modifiers: [])
        let result = MoveValidator.canPlayCard(wildcard, on: .ownSystem, in: gameState)
        
        // Then: Move should be valid
        XCTAssertNoThrow(try result.get())
    }
}
```

#### **Integration Tests** — Engine Testing
- Test full game loops
- Test AI vs AI games
- Ensure rules are correctly coordinated

#### **UI Tests** (Future)
- SwiftUI preview-based testing
- Manual QA for UI/UX

---

## Development Phases

### Phase 0: Project Setup ✅
- [x] Initialize Swift Package
- [x] Define folder structure
- [x] Create architectural skeleton
- [x] Document architecture

### Phase 1: Core Models 🚧
**Goal**: Define all game data structures

**Deliverables**:
- `CardColor.swift` — Enum for card colors
- `CardType.swift` — Enum for card types
- `CardModifier.swift` — Enum for card states
- `Card.swift` — Core card model
- `Player.swift` — Player state model
- `GameState.swift` — Complete game state container
- Unit tests for all models

**Acceptance Criteria**:
- All models conform to `Codable`
- All models are value types (struct/enum)
- Models can be serialized/deserialized

### Phase 2: Game Rules & Engine 📅
**Goal**: Implement deterministic game logic

**Deliverables**:
- `DeckGenerator.swift` — Generate valid decks
- `MoveValidator.swift` — Validate legal moves
- `ActionResolver.swift` — Apply card effects
- `WinConditionChecker.swift` — Detect winners
- `GameEngine.swift` — Main game loop
- `GameAction.swift` — Action definitions
- Comprehensive unit tests (>90% coverage)

**Acceptance Criteria**:
- All rules are pure functions
- All tests pass
- Full game can be played programmatically (no UI)

### Phase 3: AI Opponents 📅
**Goal**: Implement heuristic-based bots

**Deliverables**:
- `BotPlayer.swift` — AI player implementation
- `BotStrategy.swift` — Decision heuristics
- Bot strategy tests

**Acceptance Criteria**:
- Bots use same API as human players
- Bots are deterministic (no randomness)
- Bots can play full games

### Phase 4: User Interface 📅
**Goal**: Build SwiftUI interface

**Deliverables**:
- `GameView.swift` — Main game screen
- `CardView.swift` — Card rendering
- `SystemView.swift` — System board
- `HandView.swift` — Hand UI
- Reusable components
- Animations and transitions

**Acceptance Criteria**:
- Follows design constraints (flat icons, no text, high contrast)
- Accessible (VoiceOver, Dynamic Type)
- Smooth animations

---

## Technical Debt & Improvements

### Current Technical Debt

| Issue | Impact | Priority | Resolution |
|-------|--------|----------|------------|
| **No ViewModel Layer** | Views will directly observe GameEngine, coupling UI to Core | High | Introduce `GameViewModel` in Phase 4 |
| **No Persistence** | Game state is lost on app close | Medium | Add `Codable` persistence in Phase 5 |
| **No Networking** | Single-device only, no multiplayer | Low | Consider GameCenter integration in future |
| **Limited Error Handling** | Errors are printed, not shown to user | Medium | Add user-facing error messages in Phase 4 |

### Proposed Improvements

#### **Phase 5: Persistence** (Future)
- Implement `Codable` serialization for `GameState`
- Save/load games using `FileManager` or `UserDefaults`
- Add game history tracking

#### **Phase 6: Animations & Polish** (Future)
- Card dealing animations
- Infection/protection visual effects
- Victory celebration screens
- Sound effects

#### **Phase 7: Multiplayer** (Future)
- Local multiplayer (pass-and-play)
- Online multiplayer (GameCenter)
- Matchmaking system

#### **Code Quality Improvements**
- Add SwiftLint for code style enforcement
- Implement code documentation (DocC)
- Add CI/CD pipeline (GitHub Actions)

---

## Performance Considerations

### Current Optimizations

- **Value Types**: All models are structs/enums for stack allocation
- **Pure Functions**: Rules are stateless and can be easily optimized
- **Lazy Evaluation**: SwiftUI views only update when necessary

### Future Optimizations

- **Diffing Algorithm**: Only update changed cards in UI
- **Asset Preloading**: Preload card icons on app launch
- **State Diffing**: Use `Equatable` to minimize SwiftUI re-renders

---

## Security Considerations

### Current Security Posture

✅ **Strengths**:
- No external dependencies (reduced attack surface)
- No network communication (no data exfiltration risk)
- Value types prevent shared mutable state bugs

⚠️ **Considerations**:
- No input sanitization (not needed for single-device game)
- No encryption (no sensitive data)

### Future Security Concerns

If networking is added:
- **Input Validation**: Validate all network messages
- **Authentication**: Use GameCenter authentication
- **Encryption**: Use TLS for network communication
- **Rate Limiting**: Prevent spam/abuse

---

## Conclusion

This architecture provides a **clean, maintainable, and testable** foundation for the Cyber Systems iOS card game. The strict separation between Core and UI layers ensures:

1. **Testability**: Business logic can be tested without UI
2. **Reusability**: Core logic can be reused across platforms
3. **Maintainability**: Clear responsibilities for each module
4. **Scalability**: Easy to add features (persistence, multiplayer)

The **MVVM pattern with SwiftUI** enables reactive, declarative UI development while keeping business logic independent and testable.

---

**Document Maintained By**: Development Team  
**Next Review**: After Phase 2 Completion  
**Questions/Feedback**: Contact contributors listed in README.md
