![Lint/Fmt](https://github.com/ziblic/hwr-oop-project-gruppe-3/actions/workflows/lint_and_format_kt.yml/badge.svg?branch=main)
![Testing](https://github.com/ziblic/hwr-oop-project-gruppe-3/actions/workflows/test.yml/badge.svg?branch=main)

# Kotlin Pokémon Clone

This repository contains a student project created for an ongoing lecture on object-oriented
programming with Kotlin at HWR Berlin (summer term 2025).

> [!WARNING]
> This code is for educational purposes only. Do not rely on it!

## Local Development

This project uses [Apache Maven](https://maven.apache.org/) as build tool.

To build from your shell (without an additional local installation of Maven), ensure that `./mvnw`
is executable:

```
chmod +x ./mvnw
```

I recommend not to dive into details about Maven at the beginning.
Instead, you can use [just][just] to build the project.
It reads the repositories `justfile` which maps simplified commands to corresponding sensible Maven
calls.

With _just_ installed, you can simply run this command to perform a build of this project and run
all of its tests:

```
just build
```

To run the command line interface use the `./tnp` command. Make sure that it is executable:

```
chmod +x ./tnp
```

## Abstract

A simple Pokémon clone written in Kotlin with OOP and test-driven development in mind.

The most important features are listed in the [feature list](#feature-list)

During our Pokémon clone project, we faced the challenge of designing a robust object-oriented architecture that encapsulated Pokémon attributes, moves, and interactions, ensuring scalability and expendability for future expansions.
Balancing game mechanics involved complex algorithms and iterative testing to replicate fair battle dynamics while maintaining engaging gameplay.
Additionally, we prioritized intuitive user interface design through usability tests, enhancing player experience and capturing the essence of traditional Pokémon games.


## Feature List

| Number |    Feature   | Tests |
|--------|--------------|-------|
| 1      | BattleStats  | 6     |
| 2      | GameLoader   | 0     |
| 3      | GameParser   | 20    |
| 4      | Game         | 0     |
| 5      | Monster      | 8     |
| 6      | Trainer      | 4     |
| 7      | Type         | 5     |

## Additional Dependencies

| Number | Dependency Name | Dependency Description | Why is it necessary? |
|--------|-----------------|------------------------|----------------------|
| 1      | /               | /                      | /                    |
