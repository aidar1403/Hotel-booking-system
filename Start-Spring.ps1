param([switch]$NoBrowser, [switch]$NoWait, [int]$Port = 0)
$ErrorActionPreference = 'Stop'
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12.1'
$java = Join-Path $env:JAVA_HOME 'bin\java.exe'
if (!(Test-Path -LiteralPath $java)) { throw 'Java 21 не найдена. Проверь установку JDK.' }
Set-Location -LiteralPath $PSScriptRoot
Write-Host 'Собираю проект...' -ForegroundColor Cyan
& "$PSScriptRoot\mvnw.cmd" -B -ntp '-DskipTests' package
if ($LASTEXITCODE -ne 0) { throw 'Сборка завершилась ошибкой. Причина показана выше.' }
[xml]$pom = Get-Content -LiteralPath "$PSScriptRoot\pom.xml"
$jar = Join-Path $PSScriptRoot ("target\"+$pom.project.artifactId+'-'+$pom.project.version+'.jar')
if (!(Test-Path -LiteralPath $jar)) { throw "Не найден файл приложения: $jar" }
if ($Port -eq 0) {
    $line = Get-Content -LiteralPath "$PSScriptRoot\src\main\resources\application.properties" -ErrorAction SilentlyContinue | Where-Object { $_ -match '^server.port=\d+$' } | Select-Object -First 1
    $Port = if ($line) { [int]$line.Split('=')[1] } else { 8080 }
}
if ($Port -lt 1024 -or $Port -gt 65535) { throw 'Порт должен быть от 1024 до 65535.' }
$chosen = $false
for($candidate=$Port; $candidate -le [Math]::Min($Port+100,65535); $candidate++) {
    $listener = New-Object Net.Sockets.TcpListener([Net.IPAddress]::Loopback,$candidate)
    try { $listener.Start(); $Port=$candidate; $chosen=$true; break } catch {} finally { $listener.Stop() }
}
if (!$chosen) { throw 'Не удалось найти свободный порт.' }
$url = "http://127.0.0.1:$Port/"
$app = Start-Process -FilePath $java -ArgumentList @('-jar',('"'+$jar+'"'),"--server.port=$Port",'--server.address=127.0.0.1') -WorkingDirectory $PSScriptRoot -WindowStyle Hidden -RedirectStandardOutput "$PSScriptRoot\target\spring-running.log" -RedirectStandardError "$PSScriptRoot\target\spring-running-error.log" -PassThru
$keepRunning = $false
try {
    $ready = $false
    for($i=0;$i -lt 40;$i++) {
        if ($app.HasExited) { throw 'Сервер остановился. Подробности: target\spring-running.log и spring-running-error.log' }
        try {
            $request = [Net.HttpWebRequest]::Create($url)
            $request.Proxy = $null
            $request.Timeout = 1000
            $response = $request.GetResponse()
            $response.Close()
            $ready = $true
            break
        } catch { Start-Sleep -Milliseconds 300 }
    }
    if (!$ready) { throw 'Сервер не ответил. Подробности: target\spring-running.log' }
    Set-Content -LiteralPath "$PSScriptRoot\target\spring-running.pid" -Value $app.Id
    Set-Content -LiteralPath "$PSScriptRoot\target\spring-running.url" -Value $url
    Write-Host "Готово: $url" -ForegroundColor Green
    if (!$NoBrowser) { Start-Process $url }
    if ($NoWait) { $keepRunning=$true }
    else { [void](Read-Host 'Нажми Enter, чтобы остановить сервер') }
} finally {
    if (!$keepRunning -and !$app.HasExited) { Stop-Process -Id $app.Id }
}
