pipeline {
    agent any

    environment {
        DOCKER_HUB = 'bhumi1441'
        BACKEND_IMAGE = 'ems-backend'
        FRONTEND_IMAGE = 'ems-frontend'
    }

    stages {

        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Bhumi145/ems-project.git'
            }
        }

        stage('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('taskask-ui') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                bat 'docker build -t %DOCKER_HUB%/%BACKEND_IMAGE%:latest .'
                bat 'docker build -t %DOCKER_HUB%/%FRONTEND_IMAGE%:latest taskask-ui'
            }
        }

        stage('Push Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    bat 'echo %PASS% | docker login -u %USER% --password-stdin'
                    bat 'docker push %DOCKER_HUB%/%BACKEND_IMAGE%:latest'
                    bat 'docker push %DOCKER_HUB%/%FRONTEND_IMAGE%:latest'
                }
            }
        }

        stage('Deploy Containers') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }
}
