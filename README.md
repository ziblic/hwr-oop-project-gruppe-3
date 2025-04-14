![Lint/Fmt](https://github.com/ziblic/hwr-oop-project-gruppe-3/actions/workflows/lint_and_format_kt.yml/badge.svg?branch=main)
![Testing](https://github.com/ziblic/hwr-oop-project-gruppe-3/actions/workflows/test.yml/badge.svg?branch=main)

# Kotlin Pokémon Clone

This repository contains a student project created for an ongoing lecture on object-oriented
programming with Kotlin at HWR Berlin (summer term 2025).

> [!WARNING]
> This code is for educational purposes only. Do not rely on it!

## Local Development

This project uses [Apache Maven][maven] as build tool.

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

<!-- TODO: State most important features. -->
<!-- TODO: State the most interesting problems you encountered during the project. -->

## Feature List

| Number | Feature | Tests |
|--------|---------|-------|
| 1      | /       | /     |

## Additional Dependencies

| Number | Dependency Name | Dependency Description | Why is it necessary? |
|--------|-----------------|------------------------|----------------------|
| 1      | /               | /                      | /                    |
