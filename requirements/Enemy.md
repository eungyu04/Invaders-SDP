# Player and Enemy Ship Variety Team

## Team Introduction
We are the Player and Enemy Ship Variety Team. We aim to enhance gameplay diversity by incorporating growth elements for players and increasing the variety of enemy ships. We are always ready to communicate and collaborate, so feel free to reach out at any time!


## Goal of the Team
Increasing player gameplay variety and enhancing the diversity of enemy ships based on game progression.

## Vision of the Team
Creating a unique gaming experience by balancing player growth with challenging and varied enemies.

## Team Members
| Name        | Feature                  | Responsibilities                                                                                     |
|-------------|--------------------------|-------------------------------------------------------------------------------------------------------|
| Gyeongju Kim (Team Leader) | Piercing Bullet & Boss  | Overall management of player and enemy ship diversity, development of piercing bullet, and boss enemy. |
| Geonyeop Na | Explosive Enemy Ship      | Developed explosive enemy ship, fixing bugs with explosive chain reactions.                            |
| Jaemin Kwak | Item Drop System          | Applied item drop system using ItemManager, determining which enemies drop items.                      |
| Jimin Jang  | Player Status Development | Developed initial player ship status (movement speed, attack speed, bullet speed) and status upgrade logic.|
| Jeongwoo Kim | ItemManager & ItemPool   | Developed ItemManager and ItemPool classes for item drop and collection, and optimized item recycling.  |
| SengHyun Hyun | Enemy HP & Explosive Logic | Added HP function for enemy ships and implemented asynchronous damage logic for adjacent ships when an explosive mob detonates. |
| Yujun Kim   | Health-based Enemies      | Implementation of real-time color changes for health-based enemies, bug monitoring and fixing.         |


## Team Requirements

### Player Aspect
- Increase in stats such as health and attack speed based on gameplay progression.
- Unique functions like piercing or spread shots based on playstyle.

### Enemy Ship Aspect
- Health-based enemies (type 1, 2, 3).
- Mechanic-based enemies that die in a line.
- Item-carrying mechanic enemies.
- Stage for boss enemy.

## Dependencies on Other Teams
1. Sound for different enemy types (tanker, bomber, item enemy, boss) – Sound dependency.
2. Currency system needed for player growth – Currency dependency.
3. HUD display for player health and status – HUD dependency.
