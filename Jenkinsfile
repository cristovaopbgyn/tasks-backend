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
        stage ('Quality Gate') {
            steps {
                sleep(30)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage ('Deploy Backend') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcatid', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage ('API Test') {
            steps {
                dir('api-test') {
                    git branch: 'main', credentialsId: 'gitid', url: 'https://github.com/cristovaopbgyn/apirestassured.git'
                    sh 'mvn test'
                }
            }
        }
        stage ('Deploy FrontEnd') {
            steps {
                dir('frontend'){
                    git branch: 'master', credentialsId: 'gitid', url: 'https://github.com/cristovaopbgyn/tasks-frontend.git'
                    sh 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'tomcatid', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
        stage ('Functional Test') {
            steps {
                dir('functional-test'){
                    git branch: 'main', credentialsId: 'gitid', url: 'https://github.com/cristovaopbgyn/test-funcional.git'
                    sh 'mvn test'
                }
            }
        }
    }
}
