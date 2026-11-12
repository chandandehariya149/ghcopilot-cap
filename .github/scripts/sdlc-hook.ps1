$inputText = [Console]::In.ReadToEnd()
if (-not [string]::IsNullOrWhiteSpace($inputText)) {
    $null = $inputText | ConvertFrom-Json -ErrorAction SilentlyContinue
}

$response = @{
    additionalContext = 'Agentic SDLC safety reminder: run only after an explicit user trigger; fetch the supplied Jira issue key or URL through authorized Atlassian MCP before requirements; if retrieval fails, request source text and stop; require explicit user approval at each gate; keep setup separate from execution; never publish or create a remote PR without separate authorization.'
}
$response | ConvertTo-Json -Compress
