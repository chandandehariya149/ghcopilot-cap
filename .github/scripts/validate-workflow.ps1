$ErrorActionPreference = 'Stop'
$workspaceRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$requiredFiles = @(
    '.github/copilot-instructions.md',
    '.github/hooks/agentic-sdlc.json',
    '.github/agents/sdlc-orchestrator.agent.md',
    '.github/agents/sdlc-requirements.agent.md',
    '.github/agents/sdlc-architecture.agent.md',
    '.github/agents/sdlc-design-review.agent.md',
    '.github/agents/sdlc-implementation-planning.agent.md',
    '.github/agents/sdlc-implementation.agent.md',
    '.github/agents/sdlc-code-review.agent.md',
    '.github/agents/sdlc-testing-verification.agent.md',
    '.github/agents/sdlc-pr-preparation.agent.md',
    '.github/prompts/start-agentic-sdlc.prompt.md',
    '.github/prompts/continue-agentic-sdlc.prompt.md',
    '.github/instructions/java-spring-boot.instructions.md',
    '.github/instructions/sdlc-artifacts.instructions.md',
    '.github/skills/agentic-sdlc/SKILL.md',
    '.github/skills/agentic-sdlc/user-story-input.md',
    '.github/skills/agentic-sdlc/templates/requirements.md',
    '.github/skills/agentic-sdlc/templates/architecture.md',
    '.github/skills/agentic-sdlc/templates/design-review.md',
    '.github/skills/agentic-sdlc/templates/impl-plan.md',
    '.github/skills/agentic-sdlc/templates/implementation-report.md',
    '.github/skills/agentic-sdlc/templates/code-review.md',
    '.github/skills/agentic-sdlc/templates/verification-report.md',
    '.github/skills/agentic-sdlc/templates/pr-description.md',
    '.github/scripts/sdlc-hook.ps1'
)

foreach ($relativePath in $requiredFiles) {
    $path = Join-Path $workspaceRoot $relativePath
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) {
        throw "Missing required workflow file: $relativePath"
    }
}

$metadataFiles = @(
    Get-ChildItem -LiteralPath (Join-Path $workspaceRoot '.github/agents') -Filter '*.agent.md' -File
    Get-ChildItem -LiteralPath (Join-Path $workspaceRoot '.github/prompts') -Filter '*.prompt.md' -File
    Get-ChildItem -LiteralPath (Join-Path $workspaceRoot '.github/instructions') -Filter '*.instructions.md' -File
    Get-Item -LiteralPath (Join-Path $workspaceRoot '.github/skills/agentic-sdlc/SKILL.md')
)
$agentNames = @()
foreach ($file in $metadataFiles) {
    $content = Get-Content -LiteralPath $file.FullName -Raw
    if ($content -notmatch '(?s)\A---\r?\n(?<frontmatter>.*?)\r?\n---\r?\n') {
        throw "Missing valid frontmatter boundaries: $($file.FullName)"
    }
    $frontmatter = $Matches.frontmatter
    if ($frontmatter -notmatch '(?m)^description:\s*.+$') {
        throw "Missing description metadata: $($file.FullName)"
    }
    if ($file.Name -like '*.agent.md') {
        if ($frontmatter -notmatch '(?m)^name:\s*["'']?(?<name>[^"''\r\n]+)["'']?\s*$') {
            throw "Missing agent name: $($file.FullName)"
        }
        $agentNames += $Matches.name.Trim()
    }
}
if (($agentNames | Select-Object -Unique).Count -ne $agentNames.Count) {
    throw 'Agent names must be unique.'
}

$coordinatorPath = Join-Path $workspaceRoot '.github/agents/sdlc-orchestrator.agent.md'
$coordinator = Get-Content -LiteralPath $coordinatorPath -Raw
$expectedSpecialists = @(
    'SDLC Requirements', 'SDLC Architecture', 'SDLC Design Review',
    'SDLC Implementation Planning', 'SDLC Implementation', 'SDLC Code Review',
    'SDLC Testing and Verification', 'SDLC PR Preparation'
)
foreach ($name in $expectedSpecialists) {
    if (-not $agentNames.Contains($name) -or -not $coordinator.Contains($name)) {
        throw "Coordinator registration or specialist file missing: $name"
    }
}

$hookConfigPath = Join-Path $workspaceRoot '.github/hooks/agentic-sdlc.json'
$hookConfig = Get-Content -LiteralPath $hookConfigPath -Raw | ConvertFrom-Json
if ($hookConfig.version -ne 1 -or -not $hookConfig.hooks.sessionStart -or -not $hookConfig.hooks.subagentStart) {
    throw 'Copilot hook configuration must use version 1 and define sessionStart and subagentStart.'
}
if ($hookConfig.hooks.sessionStart[0].powershell -notmatch 'sdlc-hook\.ps1' -or $hookConfig.hooks.subagentStart[0].powershell -notmatch 'sdlc-hook\.ps1') {
    throw 'Copilot lifecycle hooks must invoke the workflow safety-context script.'
}
$skill = Get-Content -LiteralPath (Join-Path $workspaceRoot '.github/skills/agentic-sdlc/SKILL.md') -Raw
if ($skill -notmatch '(?m)^name:\s*["'']?agentic-sdlc["'']?\s*$') {
    throw 'Skill name must match its folder.'
}
$startPrompt = Get-Content -LiteralPath (Join-Path $workspaceRoot '.github/prompts/start-agentic-sdlc.prompt.md') -Raw
if ($startPrompt -notmatch '\.github/skills/agentic-sdlc/user-story-input\.md' -or $startPrompt -notmatch '(?i)file-reading tool' -or $startPrompt -notmatch '(?i)empty' -or $startPrompt -notmatch '(?i)configured') {
    throw 'Start prompt must read the reusable story input, stop on empty input, and condition external integrations on configuration.'
}
$readme = Get-Content -LiteralPath (Join-Path $workspaceRoot 'README.md') -Raw
if ($readme -notmatch 'git rev-parse --show-toplevel' -or $readme -notmatch '(?i)base branch' -or $readme -notmatch '(?i)GitHub MCP is optional') {
    throw 'README must document repository/branch preflight and GitHub MCP prerequisites.'
}

Write-Output "Workflow asset validation passed: $($requiredFiles.Count) required files, $($agentNames.Count) unique agents, 2 prompts, 2 instruction files, story input, skill templates, and Copilot hooks."