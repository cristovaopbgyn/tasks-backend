pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage ('Test Unit') {
            steps {
                sh 'mvn test'
            }
        }
        stage ('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_SERVER') {
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=backend -Dsonar.host.url=http://localhost:9000 -Dsonar.login=e0a3b97a3822d7ed358d1c1efccb33c326ac8022 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }
    }
}

