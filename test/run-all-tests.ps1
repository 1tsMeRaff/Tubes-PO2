# run-all-tests.ps1 - Headless Test Runner
$ErrorActionPreference = "Stop"
Write-Host "Compiling test sources..." -ForegroundColor Cyan
$javaFiles = (Get-ChildItem -Path src,test -Filter *.java -Recurse).FullName
javac -encoding UTF-8 -d bin $javaFiles

Write-Host "Running 14 test suites (Core Engine + Design Division UI)..." -ForegroundColor Cyan
$suites = @(
    "audio.AudioResilienceTest",
    "effects.ParticlePoolTest",
    "entity.BossPolymorphismTest",
    "entity.PlayerDecompositionTest",
    "entity.PlayerInventoryTest",
    "entity.PlayerStatsTest",
    "entity.WitchCurseMechanicTest",
    "inputs.InputDispatcherTest",
    "inputs.KeybindingsWiringTest",
    "ui.GameOverOverlayTest",
    "ui.SelectLevelOverlayTest",
    "utilitytools.HelpMethodsTest",
    "utilitytools.ResourceLoaderTest",
    "world.CampaignProgressionTest"
)

$allPassed = $true
foreach ($suite in $suites) {
    & java "-Djava.awt.headless=true" -cp "bin;resources" $suite
    if ($LASTEXITCODE -ne 0) {
        Write-Host "FAILED: $suite" -ForegroundColor Red
        $allPassed = $false
    }
}

if ($allPassed) {
    Write-Host "`nAll 12 test suites passed successfully!" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`nSome test suites failed!" -ForegroundColor Red
    exit 1
}
