$userName = $env:USERNAME

$directoryPath = "C:\users\" + $userName + "\tide"

if(-not (Test-Path -Path $directoryPath -PathType Container)) {
    mkdir $directoryPath

}
Invoke-WebRequest -Uri "https://github.com/TIDE-project/TIDE-CLI/releases/latest/download/tide-windows-latest.zip" -OutFile "tide.zip"

Expand-Archive .\tide.zip -DestinationPath $directoryPath

[Environment]::SetEnvironmentVariable("Path", $env:Path + $directoryPath,"Machine")

Set-ItemProperty -Path "$directoryPath + "\tide.exe" -Name IsReadOnly $false


