# Overview for AI Agents

## Xchange Rate Checker

This is a Spring Boot application designed to monitor the EUR/USD exchange rate across multiple providers. It calculates the spread against the official mid-market rate and sends a notification via a Telegram bot when significant improvements occur.

## Package Structure

- `com.rw.apps.xchange.ratechecker`: Root package containing the main app, core services, and config.
  - `provider`: Contains `ExchangeRateProvider` interface and subpackages for each provider implementation.
  - `db`: Handles state persistence:
    - `FileDb`: Persists snapshots of rates to a file in JSON format (`file.db`).
    - `StatesDb`: Manages in-memory state of provider spreads and reference points.
  - `model`: Simple POJOs representing currencies and individual rates.
  - `graph`: Logic for generating visual representations of rate trends using JFreeChart.
  - `util`: Helpers for date/time formatting and API calls.

## Key Components

- **`RateCheckerService`**: Coordinates the fetching of rates from all providers, compares them, and decides whether to persist a record and trigger a notification.
- **`FxImprovementDetector`**: Contains the logic to determine if an update is "significant" enough to warrant a notification. It uses a **dynamic threshold** that decays over time if no notifications have been sent.
- **`RateCheckerJob`**: A Spring `@Scheduled` job that periodically triggers the `check()` method in `RateCheckerService`.
- **`TelegramBotSender`**: Handles communication with the Telegram Bot API to send text messages and generated graphs.

## Core Logic: Notification Triggering

**Dynamic Threshold:**
The threshold starts at a base value (default `0.005`) and decays exponentially based on the time since the last notification (`threshold = base * (factor ^ (days / decay_days))`). This ensures that if the market is stagnant, the bot will eventually notify about smaller improvements.

## How to Contribute

- **Adding a Provider**:
  1. Implement the `ExchangeRateProvider` interface in a new subpackage under `provider`.
  2. Register it as a Spring `@Component`.
  3. The `RateCheckerService` will automatically pick it up via dependency injection.
- **Modifying Logic**:
  - Improvement logic resides in `FxImprovementDetector`.
  - Persistence logic resides in `FileDb`.

## Rules for AI Agents

0. **Concise Answers**: Provide concise answers without trivial comments.
1. **Keep it Simple**: Never over-engineer. Focus on readability and maintainability.
2. **Clean Code**: Follow SOLID principles and Java best practices.
