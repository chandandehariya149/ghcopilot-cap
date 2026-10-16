$inputText = [Console]::In.ReadToEnd()
if (-not [string]::IsNullOrWhiteSpace($inputText)) {
    $null = $inputText | ConvertFrom-Json -ErrorAction SilentlyContinue
}

$response = @{
    additionalContext = 'Agentic SDLC safety reminder: run only after an explicit user trigger; ask for pasted story text or a readable local file; use Jira/Confluence only when an authorized integration is configured; require explicit user approval at each gate; keep setup separate from execution; never publish or create a remote PR without separate authorization.'
}
$response | ConvertTo-Json -Compress