# Cyber Systems — iOS Card Game

A digital card game inspired by the mechanics of "VIRUS!", reskinned with a cybersecurity/computer systems theme. Built with SwiftUI and following modern iOS development best practices.

## 📱 Platform & Requirements

- **iOS Deployment Target**: iOS 17.0+
- **macOS Support**: macOS 14.0+ (for command-line testing during development)
- **Swift Version**: 5.9+
- **Package Manager**: Swift Package Manager (SPM)
- **Development Tools**: Xcode 15.0+ or VS Code with Swift extensions

## 🎮 Game Overview

**Cyber Systems** is a strategic multiplayer card game where players compete to build stable computer systems while defending against malware and disrupting their opponents' systems.

### Objective
Be the first player to complete a stable system with **4 healthy modules** of different colors.

### Card Types
- **System Modules** (Blue, Red, Green, Yellow) — Core components of your system
- **Purple Wildcard Modules** — Can substitute for any color
- **Malware** — Infects matching-color modules on opponent systems
- **Defenses** — Protects and cures modules (immunization and remediation)
- **System Operations** — Special action cards with immediate effects

### Game Mechanics
- Players draw and play cards to build their system (4 modules)
- Modules can be infected by malware, requiring defense cards to cure
- Immunized modules cannot be infected
- First player to complete 4 healthy modules of different colors wins

## 🏗️ Architecture

This project follows the **MVVM (Model-View-ViewModel)** pattern with SwiftUI:

- **Models**: Pure data structures (structs/enums) conforming to `Codable`
- **Views**: SwiftUI views for the user interface
- **Logic Layer**: Deterministic game rules and engine
- **State Management**: SwiftUI property wrappers (`@State`, `@StateObject`, `@Binding`)

For detailed architecture documentation, see [`TECHNICAL_ARCHITECTURE.md`](TECHNICAL_ARCHITECTURE.md).

## 📂 Project Structure

```
VirusGame/
├── Package.swift                           # Swift Package Manager configuration
├── README.md                               # This file
├── TECHNICAL_ARCHITECTURE.md               # Detailed architecture documentation
├── Diagramas/                              # UML class diagrams
│   ├── VirusClases.drawio.xml             # Editable diagram source
│   └── VirusClases.svg                    # Class diagram visualization
├── Sources/
│   ├── CyberSystems.swift                 # Main module entry point
│   ├── Core/
│   │   ├── Models/                        # Data structures (Card, Player, GameState)
│   │   ├── Rules/                         # Game rule logic (validation, resolution)
│   │   ├── Engine/                        # Game orchestration and turn management
│   │   └── Bots/                          # AI opponent implementations
│   └── UI/
│       ├── Views/                         # SwiftUI game screens
│       └── Components/                    # Reusable UI components
└── Tests/
    └── CyberSystemsTests/                 # Unit tests
```

## 🚀 Getting Started

### Prerequisites
- macOS 14.0+ with Xcode 15.0+ installed, or
- Swift toolchain 5.9+ for command-line development

### Clone the Repository
```bash
git clone https://github.com/Axewc/VirusGame.git
cd VirusGame
```

### Build the Project
```bash
swift build
```

### Run Tests
```bash
swift test
```

### Open in Xcode
```bash
open Package.swift
```

Xcode will automatically resolve dependencies and prepare the project for development.

### Development Workflow
1. Make changes to source files in `Sources/`
2. Add corresponding tests in `Tests/CyberSystemsTests/`
3. Run `swift test` to verify your changes
4. Build for iOS using Xcode to test on simulators or devices

## 🎨 Design Philosophy

The game follows a **flat vector iconography** design inspired by Mexico City metro style:
- No characters or anime-style graphics
- One centered icon per card
- Solid background colors with high contrast
- No text inside card graphics
- Clean, minimalist visual language

## 📋 Development Status

This project is currently in **early development phase**. The codebase includes:

✅ **Completed:**
- Project structure and Swift Package setup
- Architecture planning and documentation
- Core directory organization

🚧 **In Progress:**
- Phase 1: Core Models (Card, Player, GameState)
- Phase 2: Game Rules Engine
- Phase 3: AI Bot Implementation
- Phase 4: SwiftUI Interface

📅 **Planned:**
- Multiplayer networking
- Persistent game state
- Sound effects and animations
- Accessibility features

## 🧪 Testing

The project uses **XCTest** for unit testing. Tests are located in the `Tests/` directory and follow these principles:

- Pure function testing for game rules (deterministic, no side effects)
- Model validation tests
- Engine behavior verification
- No UI testing in Phase 1-3 (will be added in Phase 4)

## 📦 Dependencies

Currently, this project has **no external dependencies**. It uses only native frameworks:

- **Foundation** — Core Swift functionality
- **SwiftUI** — User interface (iOS/macOS)
- **Combine** — Reactive state management (future use)
- **XCTest** — Testing framework

External dependencies will be managed via Swift Package Manager if added in future phases.

## 🤝 Contributing

### Core Development Team
- Axel Casas
- Bruno Martínez
- Ignacio Rivera

### Development Guidelines
- All models must be pure structs/enums conforming to `Codable`
- Game logic must be deterministic and testable
- No business logic in UI components
- Follow Swift API Design Guidelines
- Add tests for new functionality

## 📄 Additional Documentation

- [`TECHNICAL_ARCHITECTURE.md`](TECHNICAL_ARCHITECTURE.md) — Detailed architecture and design patterns
- [`Diagramas/VirusClases.svg`](Diagramas/VirusClases.svg) — UML class diagram

## 📜 License

This project is developed as part of an academic/personal project. Please contact the contributors for licensing information.

## 🔗 Repository

GitHub: [Axewc/VirusGame](https://github.com/Axewc/VirusGame)
