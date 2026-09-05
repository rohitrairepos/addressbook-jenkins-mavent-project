pipeline {
    agent { label 'ubuntu-agent' }

    parameters {
        string(name: 'BRANCH', defaultValue: 'master', description: 'Git branch to build')
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'production'], description: 'Target environment')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Run Maven tests')
        booleanParam(name: 'BUILD_DOCKER', defaultValue: true, description: 'Build the Docker image')
        booleanParam(name: 'PUSH_DOCKER', defaultValue: true, description: 'Push the Docker image to Docker Hub')
        string(name: 'IMAGE_TAG', defaultValue: 'latest', description: 'Docker image tag')
    }

    tools {
        maven 'Maven-3'
        jdk 'jdk-25'
    }

    environment {
        APP_NAME = 'addressbook-jenkins-maven-project'
        DOCKER_IMAGE = "rohitdocker13/deopslabrepo:${IMAGE_TAG}"
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
                    userRemoteConfigs: [[url: 'https://github.com/rohitrairepos/addressbook-jenkins-mavent-project.git']]
                ])
            }
        }

        stage('Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'docker --version'
                sh 'echo "Deploy environment: ${ENVIRONMENT}"'
                sh 'echo "Docker image: ${DOCKER_IMAGE}"'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            when { expression { params.RUN_TESTS } }
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            when { expression { params.BUILD_DOCKER } }
            steps {
                sh 'docker build -t "$DOCKER_IMAGE" .'
            }
        }

        stage('Docker Push') {
            when {
                allOf {
                    expression { params.BUILD_DOCKER }
                    expression { params.PUSH_DOCKER }
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
                        docker push "$DOCKER_IMAGE"
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "Simulating deployment to ${params.ENVIRONMENT}"
                sh 'mkdir -p deployment/${ENVIRONMENT}'
                sh 'cp target/*.jar deployment/${ENVIRONMENT}/addressbook.jar'
            }
        }
    }

    post {
        success { echo 'Addressbook pipeline completed successfully.' }
        failure { echo 'Addressbook pipeline failed. Check the stage logs.' }
        always { cleanWs() }
    }
}
