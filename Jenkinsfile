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
                git 'https://github.com/Bhumi145/ems-project.git'
            }
        }

        stage('Build Backend') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('taskask-ui') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker build -t $DOCKER_HUB/$BACKEND_IMAGE:latest .'
                sh 'docker build -t $DOCKER_HUB/$FRONTEND_IMAGE:latest ./taskask-ui'
            }
        }

        stage('Push Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh 'echo $PASS | docker login -u $USER --password-stdin'
                    sh 'docker push $DOCKER_HUB/$BACKEND_IMAGE:latest'
                    sh 'docker push $DOCKER_HUB/$FRONTEND_IMAGE:latest'
                }
            }
        }

        stage('Deploy Containers') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
    }
}
