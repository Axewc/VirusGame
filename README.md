# Cyber Systems — iOS Card Game

A digital card game inspired by the mechanics of "VIRUS!", reskinned with a cybersecurity/computer systems theme.

## 📱 Platform
- **Primary**: iOS 17+
- **Architecture**: MVVM with SwiftUI
- **Language**: Swift 5.9+

## 🎮 Game Overview

Build stable computer systems composed of 4 different modules while defending against malware and attacking opponents.

### Win Condition
First player to complete a stable system with 4 healthy modules of different colors wins.

### Card Types
- **System Modules** (Blue, Red, Green, Yellow + Purple wildcard)
- **Malware** (Infects matching-color modules)
- **Defenses** (Protects and cures modules)
- **System Operations** (Immediate effects on players/systems)

## 📂 Project Structure

```
CyberSystems/
├── Core/
│   ├── Models/       # Pure data structures
│   ├── Rules/        # Deterministic game logic
│   ├── Engine/       # Game orchestration
│   └── Bots/         # AI opponents
├── UI/
│   ├── Views/        # SwiftUI screens
│   └── Components/   # Reusable UI elements
└── Tests/            # Unit tests
```

## 🚀 Getting Started

This project uses Swift Package Manager:

```bash
cd CyberSystems
swift build
swift test
```

For iOS development, open in Xcode or use VS Code with Swift extensions.

## 📋 Development Status

See [`TASK.md`](TASK.md) for the current development roadmap.

## 👥 Contributors
- Axel Casas
- Bruno Martínez
- Ignacio Rivera

## 📄 Documentation
- [`CLAUDE.md`](CLAUDE.md) — AI agent operating context
- [`CONTEXT.md`](CONTEXT.md) — Project technical context
- [`TASK.md`](TASK.md) — Development backlog
