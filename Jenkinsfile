pipeline {
    agent any
    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        PREVIOUS_VERSION_FILE = "/backup/jenkins_builds/${JOB_NAME}/last_build_version.txt" // 이전 빌드 기록 파일
        DOCKER_IMAGE = "auth-image:${BUILD_NUMBER}" // Docker 이미지 태그
    }
    stages {
        stage('Prepare Version') {
            steps {
                script {
                    def timestamp = new Date().format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
                    def commitHash = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'manual'
                    env.BUILD_VERSION = "${BUILD_NUMBER}-${commitHash}-${timestamp}"
                    echo "Current Build Version: ${env.BUILD_VERSION}"

                    if (fileExists(env.PREVIOUS_VERSION_FILE)) {
                        env.PREVIOUS_BUILD_VERSION = readFile(env.PREVIOUS_VERSION_FILE).trim()
                        echo "Previous Build Version: ${env.PREVIOUS_BUILD_VERSION}"
                    } else {
                        echo "No previous build version found."
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Build Docker image using the JAR file
                    sh '''
                    docker build -t ${DOCKER_IMAGE} --build-arg JAR_FILE=build/libs/*.jar .
                    '''
                }
            }
        }
        stage('Docker Run') {
            steps {
                script {
                    // Stop and remove existing container if it exists
                    sh '''
                    docker stop myapp || true
                    docker rm myapp || true
                    docker run -d --name auth-container -p 8081:8081 ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }
    post {
        failure {
            script {
                if (env.PREVIOUS_BUILD_VERSION) {
                    echo "Restoring previous build version: ${env.PREVIOUS_BUILD_VERSION}"
                    sh '''
                    if [ -d "${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}" ]; then
                        cp -r ${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}/* build/libs/
                        echo "Restored previous build artifacts from ${PREVIOUS_BUILD_VERSION}"
                    else
                        echo "No backup available for previous build version: ${PREVIOUS_BUILD_VERSION}"
                    fi
                    '''
                } else {
                    echo "No previous build version available to restore."
                }
            }
        }
        success {
            script {
                echo "Build succeeded with version: ${env.BUILD_VERSION}"
                sh '''
                mkdir -p ${BACKUP_DIR}/${BUILD_VERSION}
                cp build/libs/*.jar ${BACKUP_DIR}/${BUILD_VERSION}/
                echo "${BUILD_VERSION}" > ${PREVIOUS_VERSION_FILE}
                echo "Current build artifacts saved with version: ${BUILD_VERSION}"
                '''
            }
        }
    }
}
