$url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar"
$output = "lib\mysql-connector-j-8.0.33.jar"

Write-Host "Downloading MySQL JDBC driver..."
Invoke-WebRequest -Uri $url -OutFile $output
Write-Host "Download complete!" 