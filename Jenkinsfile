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
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=backend -Dsonar.host.url=http://localhost:9000 -Dsonar.login=551e55e1667352b30fe8f7a9863d13249d1e2023 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/mvn/**,**/src/test/**,**/model/**,**Application.java"
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
        stage ('Deploy Prod'){
            steps{
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }
        stage ('Health Check') {
            steps {
                sleep(59)
                dir('functional-test'){
                    sh 'mvn verify -Dskip.surefire.tests'
                }
            }
        }

    }
    post{
        always{
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: 'target/tasks-backend.war, frontend/target/tasks.war', followSymlinks: false, onlyIfSuccessful: true
        }
        unsuccessful{
            emailext attachLog: true, body: 'Olhar o log de erro', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'cristovaopb@gmail.com'
        }
        fixed{
            emailext attachLog: true, body: 'Olhar o log', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'cristovaopb@gmail.com'
        }
    }
}
