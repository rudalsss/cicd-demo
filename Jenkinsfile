pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-northeast-2'
        ECR_REPO = '605134473022.dkr.ecr.ap-northeast-2.amazonaws.com/jenkins-images'
        IMAGE_TAG = 'latest'  
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('backend') {  // backend 디렉토리로 이동
                    script {
                        // Docker 이미지 빌드
                        def app = docker.build("backend:${IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Push to ECR') {
            steps {
                script {
                    // ECR에 로그인
                    withCredentials([aws(credentialsId: '40c6115b-2ce0-48a7-a2b6-bf1fb4fbacb3')]) {
                        // ECR에 이미지 푸시
                        withDockerRegistry([credentialsId: '40c6115b-2ce0-48a7-a2b6-bf1fb4fbacb3', url: "${ECR_REPO}"]) {
                            app.push("${IMAGE_TAG}")
                        }
                    }

                    // 푸시 후 이미지 확인 (output 콘솔)
                    sh "aws ecr describe-images --repository-name jenkins-images --region ${AWS_REGION}"
                }
            }
        }
    }
}
