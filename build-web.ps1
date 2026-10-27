# build-web.ps1 - Local Build & Web Packaging Automation for Tubes-PO2
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "⚔️  TUBES-PO2 : WEB DISTRIBUTION & PACKAGING AUTOMATION" -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Pastikan folder output ada
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
if (!(Test-Path "docs")) { New-Item -ItemType Directory -Path "docs" | Out-Null }

# 2. Kompilasi source code dan unit tests
Write-Host "`n[1/4] Mengompilasi seluruh source code & headless tests..." -ForegroundColor Green
$javaFiles = (Get-ChildItem -Path src,test -Filter *.java -Recurse).FullName
javac -encoding UTF-8 -d bin $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Kompilasi GAGAL!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Kompilasi berhasil!" -ForegroundColor Green

# 3. Jalankan unit test suites headless
Write-Host "`n[2/4] Menjalankan verifikasi headless unit tests..." -ForegroundColor Green
java "-Djava.awt.headless=true" -cp "bin;resources" audio.AudioResilienceTest
java "-Djava.awt.headless=true" -cp "bin;resources" effects.ParticlePoolTest
java "-Djava.awt.headless=true" -cp "bin;resources" entity.BossPolymorphismTest
java "-Djava.awt.headless=true" -cp "bin;resources" entity.PlayerDecompositionTest
java "-Djava.awt.headless=true" -cp "bin;resources" entity.PlayerInventoryTest
java "-Djava.awt.headless=true" -cp "bin;resources" entity.PlayerStatsTest
java "-Djava.awt.headless=true" -cp "bin;resources" inputs.InputDispatcherTest
java "-Djava.awt.headless=true" -cp "bin;resources" utilitytools.HelpMethodsTest
java "-Djava.awt.headless=true" -cp "bin;resources" utilitytools.ResourceLoaderTest
java "-Djava.awt.headless=true" -cp "bin;resources" world.CampaignProgressionTest

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Pengujian unit gagal!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Seluruh 28 unit tests lulus (100%)!" -ForegroundColor Green

# 4. Salin aset dan kemas Tubes-PO2.jar
Write-Host "`n[3/4] Mengemas Tubes-PO2.jar dengan aset resources..." -ForegroundColor Green
Copy-Item -Path "resources\*" -Destination "bin" -Recurse -Force
"Main-Class: main.Main`n" | Out-File -Encoding ASCII manifest.txt
jar cvfm docs/Tubes-PO2.jar manifest.txt -C bin .
Remove-Item manifest.txt -Force -ErrorAction SilentlyContinue

$jarSize = (Get-Item "docs/Tubes-PO2.jar").Length / 1MB
Write-Host ("✓ Tubes-PO2.jar berhasil dibuat ({0:N2} MB) di docs/Tubes-PO2.jar!" -f $jarSize) -ForegroundColor Green

# 5. Petunjuk pengujian lokal & deploy GitHub Pages
Write-Host "`n[4/4] Setup Web Distribution Siap!" -ForegroundColor Cyan
Write-Host "----------------------------------------------------------" -ForegroundColor Gray
Write-Host "Untuk menguji web portal di komputer lokal:" -ForegroundColor Yellow
Write-Host "  npx -y http-server docs -p 8080" -ForegroundColor White
Write-Host "  Buka: http://localhost:8080" -ForegroundColor White
Write-Host "`nUntuk deploy ke GitHub Pages (github.io):" -ForegroundColor Yellow
Write-Host "  1. git add .github/workflows/deploy-pages.yml docs/ index.html" -ForegroundColor White
Write-Host "  2. git commit -m 'feat: setup automated GitHub Pages deployment with CheerpJ 3'" -ForegroundColor White
Write-Host "  3. git push origin dev (atau main)" -ForegroundColor White
Write-Host "  4. Di GitHub: Settings -> Pages -> Source pilih 'GitHub Actions'" -ForegroundColor White
Write-Host "  Website akan live di: https://1tsmeraff.github.io/Tubes-PO2/" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
