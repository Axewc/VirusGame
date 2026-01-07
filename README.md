# Cyber Systems — iOS Card Game

[![Swift Version](https://img.shields.io/badge/Swift-5.9+-orange.svg)](https://swift.org)
[![Platform](https://img.shields.io/badge/Platform-iOS%2017%2B-blue.svg)](https://developer.apple.com/ios/)
[![SwiftUI](https://img.shields.io/badge/UI-SwiftUI-green.svg)](https://developer.apple.com/xcode/swiftui/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-purple.svg)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)

A strategic digital card game for iOS, inspired by the mechanics of "VIRUS!" and reskinned with a cybersecurity/computer systems theme. Build and protect your computer system while deploying malware to sabotage your opponents.

---

## 📖 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Game Rules](#-game-rules)
- [Project Structure](#-project-structure)
- [Setup Instructions](#-setup-instructions)
- [Development Roadmap](#-development-roadmap)
- [Contributors](#-contributors)
- [Documentation](#-documentation)

---

## 🎯 Project Overview

**Cyber Systems** is a multiplayer card game where players compete to build a stable computer system composed of four distinct, healthy modules. Players must strategically defend their systems from malware attacks while attempting to infect and destabilize their opponents' systems.

### Key Features

- **Strategic Gameplay**: Balance between building your system and attacking opponents
- **Multiple Card Types**: System modules, malware, defenses, and system operations
- **AI Opponents**: Heuristic-based bot players for single-player mode
- **Pure Swift Implementation**: Built entirely with Swift and SwiftUI
- **Clean Architecture**: MVVM pattern with clear separation of concerns

---

## 🛠 Tech Stack

### Core Technologies

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Swift | 5.9+ |
| **UI Framework** | SwiftUI | iOS 17+ |
| **Build System** | Swift Package Manager | — |
| **Deployment Target** | iOS | 17.0+ |
| **macOS Support** | macOS | 14.0+ (for development/testing) |

### Frameworks & Libraries

- **Foundation**: Core Swift data types and utilities
- **SwiftUI**: Declarative UI framework
- **XCTest**: Unit testing framework

### External Dependencies

Currently, this project has **no external dependencies**. It is built entirely using native Swift and SwiftUI frameworks to ensure:
- Minimal maintenance overhead
- Maximum compatibility
- Reduced attack surface
- Simplified deployment

---

## 🏗 Architecture

This project follows a **clean MVVM (Model-View-ViewModel) architecture** with additional separation into **Core** and **UI** layers.

### High-Level Architecture

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  ┌────────────────┐  ┌──────────────┐  │
│  │     Views      │  │  Components  │  │
│  │   (SwiftUI)    │  │  (Reusable)  │  │
│  └────────────────┘  └──────────────┘  │
└───────────────┬─────────────────────────┘
                │
                │ Observes State
                ↓
┌─────────────────────────────────────────┐
│             Core Layer                  │
│  ┌──────────┐  ┌───────┐  ┌──────────┐ │
│  │  Models  │←─┤ Rules │←─┤  Engine  │ │
│  │  (Data)  │  │(Logic)│  │(Orchestr)│ │
│  └──────────┘  └───────┘  └──────────┘ │
│                      ↑                  │
│                 ┌────┴────┐            │
│                 │  Bots   │            │
│                 │  (AI)   │            │
│                 └─────────┘            │
└─────────────────────────────────────────┘
```

### Architecture Principles

- **Pure Data Models**: All game state is represented as immutable, value-type structures
- **Deterministic Rules**: Game logic is implemented as pure functions with no side effects
- **Testable Core**: Business logic is independent of UI and fully unit-testable
- **Reactive UI**: SwiftUI views observe state changes and update automatically

For detailed architecture documentation, see [TECHNICAL_ARCHITECTURE.md](TECHNICAL_ARCHITECTURE.md).

---

## 🎮 Game Rules

### Objective

**First player to complete a stable system with 4 healthy modules of different colors wins.**

### Card Types

| Card Type | Description | Variants |
|-----------|-------------|----------|
| **System Modules** | Building blocks of your computer system | Blue, Red, Green, Yellow, Purple (wildcard) |
| **Malware** | Infects matching-color modules in opponent systems | Color-specific infections |
| **Defenses** | Protects modules and cures infections | Protection, immunization, cure |
| **System Operations** | Immediate effects on players/systems | Varies by operation type |

### Win Condition

A player wins when they have:
- **4 modules** of **different colors** in their system
- All modules must be **healthy** (not infected)
- The system must be **stable** (meeting all requirements)

### Game Mechanics

- **Turn-Based**: Players take turns drawing cards and performing actions
- **Strategic Depth**: Balance offensive and defensive plays
- **Color Matching**: Malware can only infect modules of matching colors
- **Protection**: Defend your critical systems from attacks
- **Wildcard**: Purple modules can substitute for any color

---

## 📂 Project Structure

```
VirusGame/
├── Package.swift                    # Swift Package Manager configuration
├── README.md                        # This file
├── TECHNICAL_ARCHITECTURE.md        # Detailed technical documentation
│
├── Sources/
│   ├── CyberSystems.swift          # Main module entry point (placeholder)
│   │
│   ├── Core/                       # Business Logic Layer
│   │   ├── Models/                 # Pure data structures
│   │   │   └── README.md          # Card, Player, GameState models
│   │   │
│   │   ├── Rules/                  # Deterministic game rules
│   │   │   └── README.md          # Move validation, action resolution
│   │   │
│   │   ├── Engine/                 # Game orchestration
│   │   │   └── README.md          # Turn management, game loop
│   │   │
│   │   └── Bots/                   # AI opponents
│   │       └── README.md          # Heuristic-based strategies
│   │
│   └── UI/                         # Presentation Layer
│       ├── Views/                  # SwiftUI screens
│       │   └── README.md          # GameView, CardView, SystemView
│       │
│       └── Components/             # Reusable UI elements
│           └── README.md          # Status indicators, animations
│
├── Tests/
│   └── CyberSystemsTests/
│       └── CyberSystemsTests.swift # Unit tests
│
└── Diagramas/
    ├── VirusClases.drawio.xml      # Class diagram source
    └── VirusClases.svg             # Class diagram visualization
```

### Directory Breakdown

#### **`Sources/Core/`** — Business Logic Layer

All game logic resides here, completely independent of UI:

- **`Models/`**: Pure data structures (structs/enums) conforming to `Codable`
  - Card definitions (color, type, properties)
  - Player state (hand, tableau, system)
  - Complete game state container
  
- **`Rules/`**: Pure, testable functions for game rules
  - Deck generation with proper card distribution
  - Move validation (legal play checking)
  - Action resolution (apply card effects)
  - Win condition detection
  
- **`Engine/`**: Game orchestration and state management
  - Main game loop and turn controller
  - Turn phase management
  - Coordination between rules and state
  
- **`Bots/`**: AI opponent implementation
  - Heuristic-based decision making
  - Strategy priorities (complete system, defend, attack)
  - Uses same API as human players

#### **`Sources/UI/`** — Presentation Layer

SwiftUI-based user interface:

- **`Views/`**: Main game screens
  - GameView (main game screen)
  - CardView (individual card rendering)
  - SystemView (player's 4-module board)
  - HandView (player's hand UI)
  
- **`Components/`**: Reusable UI elements
  - Status indicators (infection, protection, immunity)
  - Animations and transitions
  - Purely presentational (no business logic)

#### **`Tests/`** — Test Suite

Unit tests for all components:
- Model validation tests
- Rules engine tests (move validation, action resolution)
- Win condition tests
- Bot strategy tests

---

## 🚀 Setup Instructions

### Prerequisites

- **macOS**: Ventura (14.0) or later
- **Xcode**: 15.0 or later (for iOS development)
- **Swift**: 5.9+ (included with Xcode)

### Clone the Repository

```bash
git clone https://github.com/Axewc/VirusGame.git
cd VirusGame
```

> **Note**: The repository is named `VirusGame`, but the Swift Package module is called `CyberSystems` (the in-game name for the reskinned version).

### Build the Project

Using Swift Package Manager (command line):

```bash
swift build
```

### Run Tests

```bash
swift test
```

### Open in Xcode

For iOS app development:

```bash
open Package.swift
```

Or drag the `Package.swift` file onto the Xcode icon.

### Alternative: VS Code Development

Install the following extensions:
- Swift Language Support
- Swift Syntax Highlighting

Then open the project folder in VS Code.

---

## 📋 Development Roadmap

The project is organized into **four development phases**:

### ✅ **Phase 0: Project Setup** (Completed)
- [x] Initialize Swift Package
- [x] Define project structure
- [x] Create architectural skeleton
- [x] Set up documentation

### 🚧 **Phase 1: Core Models** (In Progress)
- [ ] Implement `CardColor` enum (Blue, Red, Green, Yellow, Purple, Neutral)
- [ ] Implement `CardType` enum (Module, Malware, Defense, Operation)
- [ ] Create `Card` model with all properties
- [ ] Create `Player` model (hand, system/tableau)
- [ ] Create `GameState` container
- [ ] All models must conform to `Codable`

### 📅 **Phase 2: Game Rules & Engine**
- [ ] Implement `DeckGenerator` (proper card distribution)
- [ ] Implement `MoveValidator` (legal move checking)
- [ ] Implement `ActionResolver` (card effect application)
- [ ] Implement `WinConditionChecker`
- [ ] Create `GameEngine` (main game loop)
- [ ] Implement turn phase management
- [ ] Write comprehensive unit tests

### 📅 **Phase 3: AI Opponents**
- [ ] Implement `BotPlayer` interface
- [ ] Create heuristic-based strategy system
- [ ] Strategy priorities:
  1. Complete own system
  2. Immunize own modules
  3. Attack leading opponent
  4. Prevent opponent win
- [ ] Ensure deterministic behavior (no randomness)

### 📅 **Phase 4: User Interface**
- [ ] Create main `GameView`
- [ ] Design `CardView` (flat vector iconography)
- [ ] Build `SystemView` (4-module board display)
- [ ] Implement `HandView` (player's card hand)
- [ ] Create reusable components
- [ ] Add animations and transitions
- [ ] Implement high-contrast, accessible design
- [ ] Follow Mexico City metro icon style (flat, one centered icon per card)

### Design Constraints for UI

- ✨ **Flat vector iconography** (Mexico City metro style)
- 🚫 **No characters** or anime style
- 🚫 **No text** inside cards
- 🎯 **One centered icon** per card
- 🎨 **Solid background colors**
- ⚡ **High contrast** for accessibility

---

## 👥 Contributors

This project is developed and maintained by:

- **Axel Casas** — Lead Developer
- **Bruno Martínez** — Developer
- **Ignacio Rivera** — Developer

---

## 📄 Documentation

### Primary Documentation

- **[README.md](README.md)** — This file: project overview, setup, and quick start
- **[TECHNICAL_ARCHITECTURE.md](TECHNICAL_ARCHITECTURE.md)** — In-depth architectural documentation

### Additional Resources

- **[Diagramas/VirusClases.svg](Diagramas/VirusClases.svg)** — UML class diagram visualization

---

## 📜 License

_License information to be added._

---

## 🤝 Contributing

This is currently a private development project. For questions or collaboration inquiries, please contact the contributors.

---

## 🔗 Links

- **Repository**: [github.com/Axewc/VirusGame](https://github.com/Axewc/VirusGame)
- **Swift Documentation**: [swift.org/documentation](https://swift.org/documentation/)
- **SwiftUI Documentation**: [developer.apple.com/xcode/swiftui](https://developer.apple.com/xcode/swiftui/)

---

**Built with ❤️ using Swift and SwiftUI**
