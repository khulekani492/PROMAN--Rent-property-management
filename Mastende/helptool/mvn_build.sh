cd "$(dirname "$0")/.."
mvn clean

mvn package -DskipTests