# GitHub Actions Run Highlighter

[![CI](https://github.com/gha-workflow-plugins/run-syntax-support/actions/workflows/ci.yml/badge.svg)](https://github.com/gha-workflow-plugins/run-syntax-support/actions/workflows/ci.yml)

_Smart syntax highlighting for run steps in GitHub Actions workflows_

---

## Features

- Accurate syntax highlighting for `run:` steps in GitHub Actions YAML
- Recognizes `shell:` and applies Bash, Python, PowerShell, or other highlighting
- Supports GitHub Expressions (`${{ ... }}`)
- Works out-of-the-box in all JetBrains IDEs 2024.3+

## Installation

- In your IDE, go to **Settings → Plugins**
- Search for `GitHub Actions Run Highlighter`
- Install and restart

## Usage

Just open any `.github/workflows/*.yml` file and enjoy proper highlighting!

## Development

- Build: `./gradlew buildPlugin`
- Test: `./gradlew check`

## License

Apache License — see [LICENSE.txt](LICENSE.txt).

---

[JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27841-github-actions-run-highlighter) • [GitHub Organization](https://github.com/gha-workflow-plugins)
