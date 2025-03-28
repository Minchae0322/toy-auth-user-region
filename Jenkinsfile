pipeline {
    agent any

    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        PREVIOUS_VERSION_FILE = "/backup/jenkins_builds/${JOB_NAME}/last_build_version.txt" // 이전 빌드 기록 파일
        DOCKER_IMAGE = "auth-image:${BUILD_NUMBER}" // Docker 이미지 태그
        DOCKER_NETWORK = "app-network" // Docker 네트워크 이름
        ENV_FILE = "toy.env" // 환경 변수 파일명
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

        stage('Load Environment Variables') {
            steps {
                script {
                    if (fileExists(ENV_FILE)) {
                        def envVars = readFile(ENV_FILE).trim().split("\n")
                        def envList = []
                        envVars.each { line ->
                            def keyValue = line.tokenize('=')
                            if (keyValue.size() == 2) {
                                def key = keyValue[0].trim()
                                def value = keyValue[1].trim()
                                envList.add("${key}=${value}")
                                echo "Loaded ENV: ${key}"
                            }
                        }
                        withEnv(envList) {
                            echo "✅ Environment variables successfully loaded"
                        }
                    } else {
                        error("🚨 Error: ${ENV_FILE} file not found!")
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
                    def jarFile = sh(script: "ls -1 build/libs/*.jar | grep -v plain | head -n 1", returnStdout: true).trim()
                    echo "Using JAR File: ${jarFile}"
                    sh """
                    docker build -t ${DOCKER_IMAGE} --build-arg JAR_FILE=${jarFile} .
                    """

                    // 🚀 Docker 이미지 빌드 성공 여부 확인
                    def dockerImageExists = sh(script: "docker images -q ${DOCKER_IMAGE}", returnStdout: true).trim()
                    if (!dockerImageExists) {
                        error "🚨 Docker build failed! Image not found!"
                    }
                }
            }
        }

        stage('Docker Run') {
            steps {
                script {
                    sh '''
                    if [ ! -f "${ENV_FILE}" ]; then
                        echo "🚨 Error: ${ENV_FILE} file not found!"
                        exit 1
                    fi
                    '''

                    sh '''
                    if ! docker network ls | grep -q ${DOCKER_NETWORK}; then
                        docker network create ${DOCKER_NETWORK}
                    fi
                    '''

                    // 🚀 실행 중인 컨테이너 체크 후 삭제
                    sh '''
                    if docker ps -a | grep -q auth-container; then
                        echo "Stopping and removing existing auth-container..."
                        docker stop auth-container || true
                        docker rm auth-container || true
                    fi
                    '''

                    // 🚀 Docker 실행
                    sh '''
                    docker run -d --name auth-container --network ${DOCKER_NETWORK} -p 8081:8081 --env-file ${ENV_FILE} ${DOCKER_IMAGE}
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
                        JAR_FILE=$(ls -1 ${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}/*-SNAPSHOT.jar | head -n 1)
                        if [ -f "$JAR_FILE" ]; then
                            cp $JAR_FILE build/libs/
                            echo "✅ Restored previous build artifacts from ${PREVIOUS_BUILD_VERSION}"
                        else
                            echo "⚠️ No valid JAR file found in backup!"
                        fi
                    else
                        echo "⚠️ No backup available for previous build version: ${PREVIOUS_BUILD_VERSION}"
                    fi
                    '''
                } else {
                    echo "No previous build version available to restore."
                }
            }
        }

        success {
            script {
                echo "✅ Build succeeded with version: ${env.BUILD_VERSION}"
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
