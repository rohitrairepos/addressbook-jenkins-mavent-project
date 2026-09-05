pipeline {
    agent { label 'ubuntu-agent' }

    parameters {
        string(name: 'BRANCH', defaultValue: 'master', description: 'Git branch to build')
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'production'], description: 'Deployment environment')
        choice(name: 'JAVA_VERSION', choices: ['25', '21', '17'], description: 'Java version configured on the Jenkins agent')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Run Maven tests')
        booleanParam(name: 'ARCHIVE_ARTIFACT', defaultValue: true, description: 'Archive the generated JAR')
    }

    tools {
        maven 'Maven-3'
        jdk 'jdk-25'
    }

    environment {
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
                sh 'echo "Deploy environment: ${ENVIRONMENT}"'
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
            when { expression { params.ARCHIVE_ARTIFACT } }
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
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
