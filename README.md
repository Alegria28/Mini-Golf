# 🏌️ Mini Golf Game

A multiplayer mini-golf game developed in Java using the LibGDX framework and the Box2D physics engine. It features realistic physics, multiple levels, a turn-based system, and immersive audio effects.

<div align="center">
  <img src="proyectoGradle/assets/logoMiniGolf.png" alt="Mini Golf Logo" width="200"/>
</div>

## 📸 Screenshots

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/images/menu_principal.png" alt="Main Menu" width="300"/>
        <br><b>Main Menu</b>
      </td>
      <td align="center">
        <img src="docs/images/gameplay.png" alt="Gameplay" width="300"/>
        <br><b>Gameplay</b>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="docs/images/seleccion_jugadores.png" alt="Player Selection" width="300"/>
        <br><b>Color Selection</b>
      </td>
      <td align="center">
        <img src="docs/images/tabla_puntuaciones.png" alt="Scoreboard" width="300"/>
        <br><b>Scoreboard</b>
      </td>
    </tr>
  </table>
</div>

## 📋 Description

Mini Golf Game is a miniature golf game that allows multiple players to compete on various levels with unique obstacles. The game features realistic physics, immersive sound effects, and an intuitive user interface.

## 📖 User Guide

### Starting the Game
1. **Select Players**: Choose the number of players (1-4) and assign unique colors
2. **Start Game**: The game will start on level 1 of 18 available holes

### Basic Controls
- **Left Click**: Place the ball at the starting point
- **Left/Right Arrows**: Adjust shot direction
- **Up/Down Arrows**: Adjust shot power (visual indicator)
- **Spacebar**: Execute the shot (only when the ball is stationary)

### Gameplay System
- **Turns**: Players alternate turns until completing each hole
- **Objective**: Get the ball into the hole in the fewest possible strokes
- **Par**: Each hole has a suggested number of strokes (par)
- **Progression**: Complete the 18 levels sequentially
- **Scoring**: The player with the lowest total strokes wins

## 🚀 Quick Start

### 📋 System Requirements
- **Java Development Kit (JDK)**: 8 or higher
- **RAM**: Minimum 512MB, recommended 1GB
- **Disk Space**: 100MB for installation
- **Operating System**: Windows, Linux, macOS
- **OpenGL**: Support for OpenGL 2.0+

### 🔧 Detailed Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Alegria28/Mini-Golf.git
   ```

2. **Run the application**
   - **Using VS Code Task**: For an easier setup, you can run the predefined task. Open the Command Palette (`Ctrl+Shift+P`), select `Tasks: Run Task`, and choose `gradle: Run LWJGL3`.
   - **Manual Execution**: If you don't have the task configured, you can run the project from the terminal. First, navigate into the project folder and then into the `proyectoGradle` directory:
     ```bash
     cd Mini-Golf/proyectoGradle
     ```
     Then, run the appropriate command for your operating system:
     - On **Windows**:
       ```bash
       gradlew.bat lwjgl3:run
       ```
     - On **Linux/macOS**:
       ```bash
       ./gradlew lwjgl3:run
       ```

## 🙏 Acknowledgments

- To the **LibGDX** and **Box2D** development teams for their excellent tools.
- To the open-source community for their constant inspiration.