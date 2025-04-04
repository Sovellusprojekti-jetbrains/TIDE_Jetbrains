$userName = $env:USERNAME

$directoryPath = "C:\" + $userName + "\tide"

if(-not (Test-Path -Path $directoryPath -PathType Container)) {
    mkdir $directoryPath

} else {

}
